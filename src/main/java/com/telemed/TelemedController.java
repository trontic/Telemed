package com.telemed;

import com.telemed.model.*;
import com.telemed.model.Record;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@Controller
public class TelemedController {

    User currentUser = null;


    @Autowired
    UserRepositoryDB userRepository;
    @Autowired
    RecordRepositoryDB recordRepository;
    @Autowired
    TherapyPlanRepositoryDB therapyPlanRepository;
    @Autowired
    TherapyRepositoryDB therapyRepository;


    public TelemedController() {

    }


    @GetMapping("/patients")
    public String showPatients(Model model) {
        model.addAttribute(userRepository.findByType(0));
        model.addAttribute("currentUser", currentUser);
        return "doctor_home.html";
    }

    @GetMapping("/addNewPatient")
    String addNewUser(@RequestParam("fname") String fname, @RequestParam("lname") String lname,
                      @RequestParam("birthday") String birthday, @RequestParam("mbo") int mbo, @RequestParam("number") String number,
                      @RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        userRepository.save(new User(fname, lname, birthday, mbo, number, email, password));
        return "redirect:/patients";
    }

    @GetMapping("/showEditPatient")
    String showEditUser(int id, Model model) {
        User user = userRepository.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        return "doctor_edit_patient.html";
    }

    @GetMapping("/editPatient")
    String editUser(int id, String fname, String lname, String birthday, int mbo, String number, String email, String password, Model model) {
        User user = userRepository.findUserById(id);
        user.setFname(fname);
        user.setLname(lname);
        user.setBirthday(birthday);
        user.setMbo(mbo);
        user.setNumber(number);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
        System.out.println("Korisnik " + fname + " " + lname + " je ažuriran.");
        return "redirect:/patients";
    }

    @GetMapping("/showPatientOverview")
    String showPatientOverview(int id, Model model) {
        User user = userRepository.findUserById(id);
        model.addAttribute(recordRepository.findAllByUser(user));
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", user);
        return "doctor_patient_overview.html";
    }



    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/")
    public String home() {
        return "login.html";
    }

    @GetMapping("/loginProcess")
    public String loginProcess(@RequestParam("email") String email,
                               @RequestParam("password") String password, Model model){

        User u = userRepository.findByEmailAndPassword(email, password);

        if (u == null) {
            model.addAttribute("userMessage", "Korisnik nije pronađen!");
            return "login.html";
        } else {
            currentUser = u;

            if (u.getType() == 0) {
                return "redirect:/records";
            } else {
                return "redirect:/patients";
            }
        }
    }


    @GetMapping("/doctorNewPatient")
    public String doctorNewPatient(Model model) {
        model.addAttribute("currentUser", currentUser);
        return "doctor_new_patient.html";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") int id){
        User user = userRepository.findUserById(id);
        List<Record> recordList = recordRepository.findByUser(user);
        recordRepository.deleteAll(recordList);
        userRepository.delete(user);
        return "redirect:/patients";
    }

    @GetMapping("/records")
    public String records(Model model) {
        model.addAttribute("currentUser", currentUser);
        model.addAttribute(recordRepository.findAllByUser(currentUser));
        return "patient_home.html";
    }

    @GetMapping("/addNewRecord")
    String addNewRecord( int sysPressure, int diasPressure, int heartRate, String note,
                         String date, String time, User user) {
        Record newRecord = new Record(sysPressure, diasPressure, heartRate, note, date, time, user);
        newRecord.setUser(currentUser);
        recordRepository.save(newRecord);

        /*
        List<TherapyPlan> therapyPlanList = therapyPlanRepository.findByUserAndDayPart(currentUser, dayPart);
        Therapy newTherapy = new Therapy(nameMedicine, dosage, quantity, dayPart, iregular, user, record);
        for(TherapyPlan therapyPlan1 : therapyPlanList) {
            newTherapy.setNameMedicine(therapyPlan1.getNameMedicine());
            newTherapy.setDosage(therapyPlan1.getDosage());
            newTherapy.setQuantity(therapyPlan1.getQuantity());
            newTherapy.setDayPart(therapyPlan1.getDayPart());
            newTherapy.setIregular(therapyPlan1.isIregular());
            newTherapy.setUser(currentUser);
            newTherapy.setRecord(newRecord);

        }
        therapyRepository.save(newTherapy);
         */

        return "redirect:/records";
    }


    @GetMapping("/patientNewRecord")
    public String patientNewData(Model model) {
        model.addAttribute("currentUser", currentUser);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        model.addAttribute("currentDate", currentDate.format(formatter));
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
        model.addAttribute("currentTime", currentTime.format(formatter2));

        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("HH:mm");
        String dayPart = null;

        String four = "04:00";
        String ten = "10:00";
        String sixteen = "16:00";
        String midnight = "23:59";

        LocalTime morning_low = LocalTime.parse(four, formatter3);
        LocalTime morning_high = LocalTime.parse(ten, formatter3);
        LocalTime noon_high = LocalTime.parse(sixteen, formatter3);
        LocalTime evening_high = LocalTime.parse(midnight, formatter3);

        if (currentTime.isAfter(morning_low) && currentTime.isBefore(morning_high)) {
            dayPart = "jutro";
        } else if (currentTime.isAfter(morning_high) && currentTime.isBefore(noon_high)) {
            dayPart = "podne";
        } else if (currentTime.isAfter(noon_high) && currentTime.isBefore(evening_high)) {
            dayPart = "večer";
        }

        List<TherapyPlan> therapyPlanList = therapyPlanRepository.findByUserAndDayPart(currentUser, dayPart);
        model.addAttribute(therapyPlanList);

        return "patient_new_record.html";
    }

    @GetMapping("/deleteRecord")
    public String deleteRecord(@RequestParam("id") int id){
        recordRepository.deleteById(id);
        return "redirect:/records";
    }

    @GetMapping("/showEditRecord")
    String showEditRecord(int id, Model model) {
        Record record = recordRepository.findRecordById(id);
        model.addAttribute("record", record);
        model.addAttribute("currentUser", currentUser);
        return "patient_edit_record.html";
    }

    @GetMapping("/editRecord")
    String editRecord(int id, int sysPressure, int diasPressure, int heartRate, String note, String date, String time) {
        Record record = recordRepository.findRecordById(id);
        record.setId(id);
        record.setSysPressure(sysPressure);
        record.setDiasPressure(diasPressure);
        record.setHeartRate(heartRate);
        record.setNote(note);
        record.setDate(date);
        record.setTime(time);
        recordRepository.save(record);
        System.out.println("Zapis je ažuriran");
        return "redirect:/records";
    }

    @GetMapping("/searchPatient")
    public String searchPatient(@RequestParam("lname") String lname, Model model) {
        model.addAttribute(userRepository.findByLname(lname));
        model.addAttribute("currentUser", currentUser);
        return "doctor_home.html";
    }


    @GetMapping("/patientEnterTherapy")
    public String patientEnterTherapy(Model model) {
        model.addAttribute("currentUser", currentUser);
        model.addAttribute(therapyPlanRepository.findAllByUser(currentUser));
        return "patient_enter_therapy.html";
    }

    @GetMapping("/addNewTherapy")
    String addNewTherapy(String name, float dosage, float quantity, String dayPart, boolean iregular, User user) {
        TherapyPlan newTherapyPlan = new TherapyPlan(name, dosage, quantity, dayPart, iregular, user);
        newTherapyPlan.setUser(currentUser);
        therapyPlanRepository.save(newTherapyPlan);
        return "redirect:/patientEnterTherapy";
    }

    @GetMapping("/deleteTherapy")
    public String deleteTherapy(@RequestParam("id") int id) {
        therapyPlanRepository.deleteById(id);
        return "redirect:/patientEnterTherapy";
    }

    @GetMapping("/showEditPatientData")
    String showEditUserData(Model model) {
        User user = currentUser;
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        return "patient_edit_data.html";
    }

    @GetMapping("/editPatientData")
    String editUserData(String fname, String lname, String birthday, int mbo, String number, String email,
                        String password, Model model) {
        User user = currentUser;
        user.setFname(fname);
        user.setLname(lname);
        user.setBirthday(birthday);
        user.setMbo(mbo);
        user.setNumber(number);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
        System.out.println("Korisnik " + fname + " " + lname + " je ažuriran.");
        return "redirect:/records";
    }
}