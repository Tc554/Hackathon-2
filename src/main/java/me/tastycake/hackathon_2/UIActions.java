package me.tastycake.hackathon_2;

import me.tastycake.hackathon_2.calendar.PupilCalendar;
import me.tastycake.hackathon_2.hobbies.Food;
import me.tastycake.hackathon_2.hobbies.Hobby;
import me.tastycake.hackathon_2.mysql.MySQL;
import me.tastycake.hackathon_2.serializer.Serializer;
import me.tastycake.hackathon_2.user.User;
import me.tastycake.hackathon_2.user.imple.Pupil;
import me.tastycake.hackathon_2.user.imple.Teacher;
import me.tastycake.hackathon_2.user.school.SchoolClass;

import java.util.ArrayList;
import java.util.List;

public class UIActions {
//    public String login(String mail, String password) throws Exception {
//        MySQL mySQL = Main.mySQL;
//
//        if (mySQL.keyExists("mail", mail)) {
//            return "Incorrect mail! Maybe try to Sign Up.";
//        }
//
//        Object object = mySQL.getById("mail", mail, "user_data");
//
//        User user = ((User) object);
//        mySQL.saveToKey("mail", mail, Serializer.byteSerialize(object));
//
//        if (!user.getPassword().equalsIgnoreCase(password)) {
//            return "Incorrect password!";
//        }
//
//        Main.activeUser = user;
//
//        return "Successfully logged in!";
//    }
//
//    public String signUp(String name, String mail, String password, String schoolClassId) throws Exception {
//        MySQL mySQL = Main.mySQL;
//
//        if (mySQL.keyExists("mail", mail)) {
//            return "Mail already in use, try using another one.";
//        }
//
//        SchoolClass schoolClass = (SchoolClass) mySQL.getById("id", schoolClassId, "class_data");
//
//        User user;
//
//        if (mail.equalsIgnoreCase(schoolClass.getTeacherMail())) {
//            user = new Teacher(name, mail, password, schoolClass, new ArrayList<>());
//        } else {
//            Pupil pupil = new Pupil(name, mail, password, new ArrayList<>(), new ArrayList<>(), null, schoolClass, new ArrayList<>(), 0);
//            pupil.setPupilCalendar(new PupilCalendar(pupil));
//            user = pupil;
//        }
//
//        mySQL.saveToKey("mail", mail, Serializer.byteSerialize(user));
//
//        Main.activeUser = user;
//
//        return "Account created successfully";
//    }
//
//    public void submitAnswers(List<Hobby> hobbies, List<Food> foods) {
//        User user = Main.activeUser;
//
//        if (!(user instanceof Pupil)) return;
//
//        ((Pupil) user).setHobbies(hobbies);
//        ((Pupil) user).setFoods(foods);
//    }
//
//    public String autoLogin() throws Exception {
//        String mail = null, password = null; // TODO From AndroidStudio using SharedPreferences:
//        return login(mail, password);
//    }
}
