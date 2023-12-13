package com.telemed;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Controller
public class TelemedController {

    List<Record> recordList = new ArrayList<>();
    List<User> userList = new ArrayList<>();

    List<User> getpatientList() {
        List<User> patientList = new ArrayList<>();
        for (User u : userList) {
            if (u.getType() == 0) {
                patientList.add(u);
            }
        }
        return patientList;
    }

    List<Record> getRecordList() {
        List<Record> recordList1 = new ArrayList<>();
        for (Record r : recordList) {
            recordList1.add(r);
        }
        return recordList1;
    }

    public TelemedController() {

        Record r = new Record(122, 81, 85, 36, "", "10.12.2023.", "07:44");
        recordList.add(r);

        recordList.add(new Record(125, 76, 80,35,"", "11.12.2023", "18:30"));

        User u = new User("Zdravko", "Zdravić","01.01.1980.",11111111, "zdravko@gmail.com","zdravko");
        u.setType(1);
        userList.add(u);

        userList.add (new User("Marko", "Marković","23.07.1986.",23232323, "marko@gmail.com","marko"));
        userList.add (new User("Ivan", "Ivanović","01.10.2000.",24242424, "ivan@gmail.com","ivan"));
        userList.add (new User("Željko", "Željković","25.09.1998.",32323232, "zeljko@gmail.com","zeljko"));
        userList.add (new User("Lucija", "Lucić","21.08.1967.",42424242, "lucija@gmail.com","lucija"));
        userList.add (new User("Mirko", "Mirković","15.09.1977.",53535353, "mirko@gmail.com","mirko"));
        userList.add (new User("Marija", "Marić","02.08.1969",56565656, "marija@gmail.com","marija"));
        userList.add (new User("Ivana", "Ivanović","09.10.1974.",38383838, "ivana@gmail.com","ivana"));
        userList.add (new User("Petra", "Petrović","29.01.1988.",97979797, "petra@gmail.com","petra"));
    }

    @GetMapping("/telemedcalculate")
    public void calculate(@RequestParam("a") int a, @RequestParam("b") int b, @RequestParam("op") String op,
                          HttpServletResponse response) throws IOException {
        int result = 0;
        switch (op) {
            case "+" -> result = a + b;
            case "-" -> result = a - b;
            case "*" -> result = a * b;
            case "/" -> result = a / b;
        }
        response.getWriter().println("Result is: " + result);
    }

    @GetMapping("/patients")
    public String showPatients(Model model) {
        model.addAttribute(getpatientList());
        return "doctor_home.html";
    }

    @GetMapping("/addNewPatient")
    String addNewUser(@RequestParam("fname") String fname, @RequestParam("lname") String lname,
                      @RequestParam("birthday") String birthday, @RequestParam("mbo") int mbo,
                      @RequestParam("email") String email, @RequestParam("password") String password) {
        userList.add(new User(fname, lname, birthday, mbo, email, password));
        return "redirect:/patients";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/loginProcess")
    public String loginProcess(@RequestParam("email") String email,
                               @RequestParam("password") String password, Model model){
        User user = null;
        for (User u : userList) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                user = u;
                break;
            }
        }
        if (user != null){
            if(user.getType() == 0){
                return "redirect:/records";
            }
            else {
                return "redirect:/patients";
            }
        }
        else {
            model.addAttribute("userMessage", "User not found");
            return "login";
        }
    }


    @GetMapping("/doctorNewPatient")
    public String doctorNewPatient() {
        return "doctor_new_patient.html";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("mbo") int mbo){
        for (User u: userList) {
            if(u.getMbo() == mbo){
                userList.remove(u);
                break;
            }
        }
        return "redirect:/patients";
    }

    @GetMapping("/records")
    public String records(Model model) {
        model.addAttribute(getRecordList());
        return "patient_home_lucija_lucic.html";
    }

    @GetMapping("/addNewRecord")
    String addNewRecord(@RequestParam("sysPressure") int sysPressure,
                        @RequestParam("diasPressure") int diasPressure,
                        @RequestParam("heartRate") int heartRate,
                        @RequestParam("bodyTemperature") float bodyTemperature,
                        @RequestParam("note") String note,
                        @RequestParam("date") String date,
                        @RequestParam("time") String time) {
        recordList.add(new Record(sysPressure, diasPressure, heartRate, bodyTemperature, note, date, time));
        return "redirect:/records";
    }

    @GetMapping("/patientNewData")
    public String patientNewData() {
        return "patient_new_data.html";
    }



}