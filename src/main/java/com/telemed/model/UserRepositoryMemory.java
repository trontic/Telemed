package com.telemed.model;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryMemory {

    List<User> userList = new ArrayList<>();

    public UserRepositoryMemory() {
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

    public List<User> getUserList() {
        return userList;
    }

    public List<User> getpatientList() {
        List<User> patientList = new ArrayList<>();
        for (User u : userList) {
            if (u.getType() == 0) {
                patientList.add(u);
            }
        }
        return patientList;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        User user = null;
        for (User u : userList) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                user = u;
            }
        }
        return user;
    }

    public User getUserById (int id) {
        User user = null;
        for (User u : userList) {
            if (u.getId() == id) {
                user = u;
            }
        }
        return user;
    }



}
