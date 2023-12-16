package com.telemed;

import com.telemed.model.Record;
import com.telemed.model.RecordRepositoryMemory;
import com.telemed.model.User;
import com.telemed.model.UserRepositoryMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TelemedController {
    @Autowired
    RecordRepositoryMemory recordRepo = new RecordRepositoryMemory();
    @Autowired
    UserRepositoryMemory userRepo = new UserRepositoryMemory();



    public TelemedController() {

    }


    @GetMapping("/patients")
    public String showPatients(Model model) {
        model.addAttribute(userRepo.getpatientList());
        return "doctor_home.html";
    }

    @GetMapping("/addNewPatient")
    String addNewUser(@RequestParam("fname") String fname, @RequestParam("lname") String lname,
                      @RequestParam("birthday") String birthday, @RequestParam("mbo") int mbo,
                      @RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        userRepo.getUserList().add(new User(fname, lname, birthday, mbo, email, password));
        model.addAttribute(userRepo.getpatientList());
        return "redirect:/patients";
    }

    @GetMapping("/showEditPatient")
    String showEditUser(int id, Model model) {
        User user = userRepo.getUserById(id);
        model.addAttribute("user", user);
        return "doctor_edit_patient.html";
    }

    @GetMapping("/editPatient")
    String editUser(int id, String fname, String lname, String birthday, int mbo, String email, String password, Model model) {
        User user = userRepo.getUserById(id);
        user.setFname(fname);
        user.setLname(lname);
        user.setBirthday(birthday);
        user.setMbo(mbo);
        user.setEmail(email);
        user.setPassword(password);

        System.out.println("Korisnik " + fname + " " + lname + " je ažuriran.");
        return "redirect:/patients";
    }

    @GetMapping("/showPatientOverview")
    String showPatientOverview(int id, Model model) {
        User user = userRepo.getUserById(id);
        model.addAttribute("user", user);
        return "doctor_patient_overview.html";
    }



    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/loginProcess")
    public String loginProcess(@RequestParam("email") String email,
                               @RequestParam("password") String password, Model model){

        User u = userRepo.getUserByEmailAndPassword(email, password);

        if (u == null) {
            model.addAttribute("userMessage", "Korisnik nije pronađen!");
            return "login.html";
        } else {
            if (u.getType() == 0) {
                return "redirect:/records";
            } else {
                return "redirect:/patients";
            }
        }
    }


    @GetMapping("/doctorNewPatient")
    public String doctorNewPatient() {
        return "doctor_new_patient.html";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") int id){
        User user = userRepo.getUserById(id);
        userRepo.getUserList().remove(user);
        return "redirect:/patients";
    }

    @GetMapping("/records")
    public String records(Model model) {
        model.addAttribute(recordRepo.getRecordList());
        return "patient_home.html";
    }

    @GetMapping("/addNewRecord")
    String addNewRecord(@RequestParam("sysPressure") int sysPressure,
                        @RequestParam("diasPressure") int diasPressure,
                        @RequestParam("heartRate") int heartRate,
                        @RequestParam("bodyTemperature") float bodyTemperature,
                        @RequestParam("date") String date,
                        @RequestParam("time") String time, Model model) {
        recordRepo.getRecordList().add(new Record(sysPressure, diasPressure, heartRate, bodyTemperature, date, time));
        model.addAttribute(recordRepo.getRecordList());
        return "redirect:/records";
    }

    @GetMapping("/patientNewRecord")
    public String patientNewData() {
        return "patient_new_record.html";
    }

    @GetMapping("/deleteRecord")
    public String deleteRecord(@RequestParam("id") int id){
        Record record = recordRepo.getRecordById(id);
        recordRepo.getRecordList().remove(record);
        return "redirect:/records";
    }

    @GetMapping("/showEditRecord")
    String showEditRecord(int id, Model model) {
        Record record = recordRepo.getRecordById(id);
        model.addAttribute("record", record);
        return "patient_edit_record.html";
    }

    @GetMapping("/editRecord")
    String editRecord(int id, int sysPressure, int diasPressure, int heartRate, float bodyTemperature, String date, String time, Model model) {
        Record record = recordRepo.getRecordById(id);
        record.setSysPressure(sysPressure);
        record.setDiasPressure(diasPressure);
        record.setHeartRate(heartRate);
        record.setBodyTemperature(bodyTemperature);
        record.setDate(date);
        record.setTime(time);
        System.out.println("Zapis je ažuriran");
        return "redirect:/records";
    }



}