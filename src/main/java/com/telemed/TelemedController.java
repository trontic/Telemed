package com.telemed;

import com.telemed.model.*;
import com.telemed.model.Record;
import com.telemed.tools.EmailSender;
import io.micrometer.common.util.StringUtils;
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
import java.util.*;

import org.springframework.data.domain.Sort;



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

    private EmailSender emailSender;


    public TelemedController() {
        this.emailSender = new EmailSender();
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
        User newUser = new User(fname, lname, birthday, mbo, number, email, password, false);
        userRepository.save(newUser);
        emailSender.sendEmail(email, "Registracija na eDnevnik tlaka", "Vaš liječnik kreirao je za Vas račun na eDnevnik-tlaka.net.\n" +
                "Za prvu prijavu koristite Vašu email adresu i privremenu lozinku " + password + ". Kod prve prijave bit će potrebno promijeniti lozinku.\n" +
                "Molimo Vas da novu lozinku zapišete kako je ne biste zaboravili. Hvala ");
        return "redirect:/patients";
    }

    @GetMapping("/patientRegistration")
    String patientRegistration(@RequestParam("fname") String fname,
                               @RequestParam("lname") String lname,
                               @RequestParam("birthday") String birthday,
                               @RequestParam("mbo") int mbo,
                               @RequestParam("number") String number,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword,
                               @RequestParam(value = "allowAccess", required = false) Boolean allowAccess,
                               Model model) {

        // Add form field values to the model to repopulate the form in case of an error
        model.addAttribute("fname", fname);
        model.addAttribute("lname", lname);
        model.addAttribute("birthday", birthday);
        model.addAttribute("mbo", mbo);
        model.addAttribute("number", number);
        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("confirmPassword", confirmPassword);
        model.addAttribute("allowAccess", allowAccess);



        // Check if the password length is less than 5 characters
        if (StringUtils.isBlank(password) || password.length() < 5) {
            model.addAttribute("passwordLengthError", true);
            return "patient_registration.html";
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("passwordMismatch", true);
            return "patient_registration.html";
        }

        // Check if the checkbox for terms and conditions is unchecked
        if (allowAccess == null || !allowAccess) {
            model.addAttribute("accessError", true);
            return "patient_registration.html";
        }

        // Create a new user and save it to the database
        User newUser = new User(fname, lname, birthday, mbo, number, email, password, false);
        newUser.setPasswordChanged();
        userRepository.save(newUser);

        // Set the current user (this depends on how you manage user sessions)
        currentUser = newUser;

        // Redirect to a different page after successful registration
        return "redirect:/records";
    }

    @GetMapping("/changePassword")
    String showChangePassword(Model model) {

        //ispituje se mijenja li se password 1. put (obavezno) ili nakon toga

        /* oba htmla - patient_password_change_after i patient_password_change zovu u formi metodu changePasswordAction */

        // ako se ne mijenja prvi put, otvara se stranica za promjenu bez dopuštenja doktoru za privolom za podacima
        if (currentUser.hasUpdatedPassword()) {
            return "patient_password_change_after.html";
        }
        // mijenja se lozinka 1. put
        return "patient_password_change.html";
    }

    @GetMapping("/changePasswordAction")
    String changePasswordAction(@RequestParam("password") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                @RequestParam(value = "allowAccess", required = false) Boolean allowAccess,
                                Model model) {

        if (StringUtils.isBlank(newPassword) || newPassword.length() < 5) {
            model.addAttribute("passwordLengthError", true);
            if(currentUser.hasUpdatedPassword()) {
                return "patient_password_change_after.html";
            }
            return "patient_password_change.html";
        } else if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("passwordMismatch", true);
            if(currentUser.hasUpdatedPassword()) {
                return "patient_password_change_after.html";
            }
            return "patient_password_change.html";
        } else if (newPassword.equals(currentUser.getPassword())) {
            model.addAttribute("samePasswordError", true);
            if(currentUser.hasUpdatedPassword()) {
                return "patient_password_change_after.html";
            }
            return "patient_password_change.html";
            // prijavio se korisnik po prvi puta i mijenja password (passwordUpdated=false)
        } else if (!currentUser.hasUpdatedPassword()) {
            System.out.println("Passw:" + currentUser.hasUpdatedPassword());
            if (allowAccess == null || !allowAccess) {
                model.addAttribute("accessError", true);
                model.addAttribute("newPassword", newPassword);
                model.addAttribute("confirmPassword", confirmPassword);
                return "patient_password_change.html";
            }
        }
        currentUser.setPassword(newPassword);
        // set password postaje true
        currentUser.setPasswordChanged();
        userRepository.save(currentUser);
        return "redirect:/records";
    }

    @GetMapping("/registration")
    public String registration() {
        return "patient_registration.html";
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
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        model.addAttribute("recordList", recordRepository.findAllByUser(user, sort));
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", user);
        model.addAttribute(therapyPlanRepository.findAllByUser(user));
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

        User user = userRepository.findByEmailAndPassword(email, password);

        if (user == null) {
            model.addAttribute("userMessage", "Pogrešna email adresa ili lozinka!");
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            return "login.html";
        } else {
            currentUser = user;

            if (user.getType() == 0) {
                if (!user.hasUpdatedPassword()) {
                    return "redirect:/changePassword";
                } else {
                    return "redirect:/records";
                }
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
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        model.addAttribute("recordList", recordRepository.findAllByUser(currentUser, sort));
        return "patient_home.html";
    }

    @GetMapping("/addNewRecord")
    String addNewRecord(@RequestParam("sysPressure") int sysPressure, @RequestParam("diasPressure") int diasPressure,
                        @RequestParam("heartRate") int heartRate, @RequestParam("note") String note,
                        @RequestParam("date") String date, @RequestParam("time") String time, User user,
                        @RequestParam(value = "selectedTherapyPlanIds", required = false) List<Integer> selectedIds,
                        @RequestParam(value = "name", required = false) String name,
                        @RequestParam(value = "quantity", required = false) Float quantity,
                        @RequestParam(value = "emergencyCheck", required = false) boolean emergencyCheck,
                        @RequestParam(value = "iregularCheck", required = false) boolean iregularCheck,
                        Model model) {


        // Case 1: emergencyCheck is checked and iregularCheck is not checked
        if (emergencyCheck && !iregularCheck) {
            // Your logic for case 1
            Record newRecord = new Record(sysPressure, diasPressure, heartRate, note, date, time, user);
            newRecord.setEmergency(true);
            newRecord.setUser(currentUser);
            recordRepository.save(newRecord);
        }

        // Case 2: both emergencyCheck and iregularCheck are not checked
        else if (!emergencyCheck && !iregularCheck) {
            // Your logic for case 2
            Record newRecord = new Record(sysPressure, diasPressure, heartRate, note, date, time, user);
            newRecord.setEmergency(false);
            newRecord.setUser(currentUser);
            recordRepository.save(newRecord);

            List<TherapyPlan> selectedTherapyPlans;

            if (selectedIds != null && !selectedIds.isEmpty()) {
                selectedTherapyPlans = (List<TherapyPlan>) therapyPlanRepository.findAllById(selectedIds);
            } else {
                selectedTherapyPlans = new ArrayList<>();
            }

            for (TherapyPlan therapyPlan : selectedTherapyPlans) {
                Therapy newTherapy = createTherapyFromPlan(therapyPlan, currentUser, newRecord);
                newTherapy.setIregular(false);
                therapyRepository.save(newTherapy);
            }

            /*List<TherapyPlan> selectedTherapyPlans = (List<TherapyPlan>) therapyPlanRepository.findAllById(selectedIds);

            for (TherapyPlan therapyPlan : selectedTherapyPlans) {
                Therapy newTherapy = createTherapyFromPlan(therapyPlan, currentUser, newRecord);
                newTherapy.setIregular(false);
                therapyRepository.save(newTherapy);
            }

             */
        }

        // Case 3: emergencyCheck is checked and iregularCheck is checked
        else if (emergencyCheck && iregularCheck) {
            // Your logic for case 3
            Record newRecord = new Record(sysPressure, diasPressure, heartRate, note, date, time, user);
            newRecord.setEmergency(true);
            newRecord.setUser(currentUser);
            recordRepository.save(newRecord);
            Therapy newTherapy = new Therapy();
            newTherapy.setNameMedicine(name);
            newTherapy.setQuantity(quantity);
            newTherapy.setDayPart(null);
            newTherapy.setTime(time);
            newTherapy.setIregular(true);
            newTherapy.setUser(currentUser);
            newTherapy.setRecord(newRecord);
            therapyRepository.save(newTherapy);
        }

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


        List<TherapyPlan> therapyPlanList = therapyPlanRepository.findByUserAndDayPart(currentUser, calculateDayPart());
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
    String addNewTherapy(String name, float quantity, String dayPart, boolean iregular, User user) {
        TherapyPlan newTherapyPlan = new TherapyPlan(name, quantity, dayPart, iregular, user);
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

    public String calculateDayPart(){

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //model.addAttribute("currentDate", currentDate.format(formatter));
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
        //model.addAttribute("currentTime", currentTime.format(formatter2));

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
        return dayPart;
    }

    private Therapy createTherapyFromPlan(TherapyPlan plan, User user, Record record) {
        Therapy therapy = new Therapy();
        therapy.setNameMedicine(plan.getNameMedicine());
        therapy.setQuantity(plan.getQuantity());
        therapy.setDayPart(plan.getDayPart());
        therapy.setIregular(plan.isIregular());
        therapy.setUser(user);
        therapy.setRecord(record);
        return therapy;
    }


}