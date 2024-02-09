package com.telemed;

import com.lowagie.text.DocumentException;
import com.telemed.model.*;
import com.telemed.model.Record;
import com.telemed.tools.EmailSender;
import com.telemed.model.AdviceRepositoryDB;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.swing.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class TelemedController {


    @Autowired
    UserRepositoryDB userRepository;
    @Autowired
    RecordRepositoryDB recordRepository;
    @Autowired
    TherapyPlanRepositoryDB therapyPlanRepository;
    @Autowired
    TherapyRepositoryDB therapyRepository;
    @Autowired
    AdviceRepositoryDB adviceRepositoryDB;


    private EmailSender emailSender;


    public TelemedController() {
        this.emailSender = new EmailSender();
    }


    @GetMapping("/patients")
    public String showPatients(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "0") int type, HttpSession session) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")); // You can change the sorting as needed
        Page<User> userPage = userRepository.findByType(type, pageable);
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("userPage", userPage);
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
                               Model model, HttpSession session) {

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

        session.setAttribute("currentUser", newUser);



        // Set the current user (this depends on how you manage user sessions)
        //currentUser = newUser;

        // Redirect to a different page after successful registration
        return "redirect:/records";
    }

    @GetMapping("/changePassword")
    String showChangePassword(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);

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
                                Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);

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
    String showEditUser(int id, Model model, HttpSession session) {
        User user = userRepository.findUserById(id);
        model.addAttribute("user", user);
        User currentUser = (User) session.getAttribute("currentUser");
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
    String showPatientOverview(int id, Model model, @RequestParam(defaultValue = "0") int page, HttpSession session) {
        User user = userRepository.findUserById(id);
        int pageSize = 10;

        // Create a composite sort object to sort by both date and time in descending order
        Sort sort = Sort.by(
                Sort.Order.desc("date"),
                Sort.Order.desc("time")
        );

        // Create the pageable object with the composite sort
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Record> recordPage = recordRepository.findAllByUser(user, pageable);

        // Create a map to store irregular therapies for each record
        Map<Record, Therapy> recordTherapiesMap = new HashMap<>();

        // Iterate over the records in the page and fetch irregular therapies for each record
        recordPage.forEach(record -> {
            // Fetch the irregular therapy associated with the record
            Therapy irregularTherapy = therapyRepository.findFirstByRecordAndIregularIsTrue(record);

            // Add the irregular therapy to the map if it exists
            if (irregularTherapy != null) {
                recordTherapiesMap.put(record, irregularTherapy);
            }
        });

        // Add the fetched data to the model
        model.addAttribute("recordPage", recordPage);
        model.addAttribute("recordTherapiesMap", recordTherapiesMap);
        model.addAttribute("id", id); // Add user ID to use in the view
        ;
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", user);
        model.addAttribute(therapyPlanRepository.findAllByUser(user));
        return "doctor_patient_overview.html";
    }



    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/graph")
    public String graph() {
        return "graph.html";
    }

    @GetMapping("/")
    public String home() {
        return "login.html";
    }

    @GetMapping("/loginProcess")
    public String loginProcess(@RequestParam("email") String email,
                               @RequestParam("password") String password, Model model, HttpSession session){

        User user = userRepository.findByEmailAndPassword(email, password);

        if (user == null) {
            model.addAttribute("userMessage", "Pogrešna email adresa ili lozinka!");
            model.addAttribute("email", email);
            model.addAttribute("password", password);
            return "login.html";
        } else {
            session.setAttribute("currentUser", user);

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
    public String doctorNewPatient(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
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
    public String records(Model model, @RequestParam(defaultValue = "0") int page, Record record, HttpSession session) {
        int pageSize = 10;

        // Create a composite sort object to sort by both date and time in descending order
        Sort sort = Sort.by(
                Sort.Order.desc("date"),
                Sort.Order.desc("time")
        );

        // Create the pageable object with the composite sort
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        User currentUser = (User) session.getAttribute("currentUser");
        Page<Record> recordPage = recordRepository.findAllByUser(currentUser, pageable);


        model.addAttribute("currentUser", currentUser);
        model.addAttribute("recordPage", recordPage);


        return "patient_home.html";
    }

    @GetMapping("/addNewRecord")
    String addNewRecord(@RequestParam("sysPressure") int sysPressure, @RequestParam("diasPressure") int diasPressure,
                        @RequestParam(value = "heartRate", required = false) Integer heartRate, @RequestParam("note") String note,
                        @RequestParam("date") String date, @RequestParam("time") String time, User user,
                        @RequestParam(value = "selectedTherapyPlanIds", required = false) List<Integer> selectedIds,
                        @RequestParam(value = "name", required = false) String name,
                        @RequestParam(value = "time_med") String time_med,
                        @RequestParam(value = "quantity", required = false) Float quantity,
                        @RequestParam(value = "emergencyCheck", required = false) boolean emergencyCheck,
                        @RequestParam(value = "iregularCheck", required = false) boolean iregularCheck,
                        Model model, HttpSession session) {

        User currentUser = (User) session.getAttribute("currentUser");
        // Case 1: emergencyCheck is checked and iregularCheck is not checked
        if (emergencyCheck && !iregularCheck) {
            // Your logic for case 1
            Record newRecord = new Record(sysPressure, diasPressure, note, date, time, user);
            if (heartRate != null) {
                newRecord.setHeartRate(heartRate);
            }
            newRecord.setEmergency(true);
            newRecord.setUser(currentUser);
            recordRepository.save(newRecord);
        }

        // Case 2: both emergencyCheck and iregularCheck are not checked
        else if (!emergencyCheck && !iregularCheck) {
            // Your logic for case 2
            Record newRecord = new Record(sysPressure, diasPressure, note, date, time, user);
            if (heartRate != null) {
                newRecord.setHeartRate(heartRate);
            }
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
            Record newRecord = new Record(sysPressure, diasPressure, note, date, time, user);
            if (heartRate != null) {
                newRecord.setHeartRate(heartRate);
            }
            newRecord.setEmergency(true);
            newRecord.setUser(currentUser);
            recordRepository.save(newRecord);
            Therapy newTherapy = new Therapy();
            newTherapy.setNameMedicine(name);
            newTherapy.setQuantity(quantity);
            newTherapy.setDayPart(null);
            newTherapy.setTime(time_med);
            newTherapy.setIregular(true);
            newTherapy.setUser(currentUser);
            newTherapy.setRecord(newRecord);
            therapyRepository.save(newTherapy);
        }

        return "redirect:/records";
    }


    @GetMapping("/patientNewRecord")
    public String patientNewData(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        model.addAttribute("currentDate", currentDate.format(formatter));
        LocalTime currentTime = LocalTime.now();
        LocalTime currentTimePlusOneHour = currentTime.plusHours(1);
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
        model.addAttribute("currentTime", currentTimePlusOneHour.format(formatter2));


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
    String showEditRecord(int id, Model model, HttpSession session) {
        Record record = recordRepository.findRecordById(id);
        model.addAttribute("record", record);
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);
        // Calculate seven days ago
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);

        // Add the attribute to the Thymeleaf model
        model.addAttribute("sevenDaysAgo", sevenDaysAgo);
        return "patient_edit_record.html";
    }

    @GetMapping("/editRecord")
    String editRecord(int id, int sysPressure, int diasPressure, Integer heartRate, String note, String date, String time) {
        Record record = recordRepository.findRecordById(id);
        record.setId(id);
        record.setSysPressure(sysPressure);
        record.setDiasPressure(diasPressure);
        if (heartRate != null) {
            record.setHeartRate(heartRate);
        }
        record.setNote(note);
        record.setDate(date);
        record.setTime(time);
        recordRepository.save(record);
        System.out.println("Zapis je ažuriran");
        return "redirect:/records";
    }


    @GetMapping("/searchPatient")
    public String searchPatient(@RequestParam("name") String name, @PageableDefault(size = 10) Pageable pageable, Model model, HttpSession session) {
        List<User> searchResults = userRepository.findByFnameContainingIgnoreCaseOrLnameContainingIgnoreCase(name, name);


        // Manually create a Page object for pagination in the template
        int pageSize = pageable.getPageSize();
        int currentPage = Math.min(pageable.getPageNumber(), searchResults.size() / pageSize);
        int totalItems = searchResults.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        Page<User> searchPage = new PageImpl<>(searchResults.subList(currentPage * pageSize, Math.min((currentPage + 1) * pageSize, totalItems)),
                PageRequest.of(currentPage, pageSize), totalItems);

        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("searchResults", searchPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("searchPhrase", name);

        return "doctor_home.html";
    }


    @GetMapping("/patientEnterTherapy")
    public String patientEnterTherapy(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);
        model.addAttribute(therapyPlanRepository.findAllByUser(currentUser));
        return "patient_enter_therapy.html";
    }

    @GetMapping("/addNewTherapy")
    String addNewTherapy(String name, float quantity, String dayPart, boolean iregular, User user, HttpSession session) {
        TherapyPlan newTherapyPlan = new TherapyPlan(name, quantity, dayPart, iregular, user);
        User currentUser = (User) session.getAttribute("currentUser");
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
    String showEditUserData(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        User user = currentUser;
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        return "patient_edit_data.html";
    }

    @GetMapping("/editPatientData")
    String editUserData(String fname, String lname, String birthday, int mbo, String number, String email, Model model,
                        HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        User user = currentUser;
        user.setFname(fname);
        user.setLname(lname);
        user.setBirthday(birthday);
        user.setMbo(mbo);
        user.setNumber(number);
        user.setEmail(email);
        userRepository.save(user);
        System.out.println("Korisnik " + fname + " " + lname + " je ažuriran.");
        return "redirect:/records";
    }

    @GetMapping("/doctorAdvice")
    public String showDoctorAdvice(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);
        return "patient_advice.html";
    }

    @GetMapping("/getRandomHealthAdvice")
    @ResponseBody
    public String getRandomHealthAdvice() {
        List<Advice> adviceList = (List<Advice>) adviceRepositoryDB.findAll();
        if (!adviceList.isEmpty()) {
            int randomIndex = new Random().nextInt(adviceList.size());
            Advice randomAdvice = adviceList.get(randomIndex);
            return randomAdvice.getAdvice();
        } else {
            return "Savjet doktora nije dostupan.";
        }
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