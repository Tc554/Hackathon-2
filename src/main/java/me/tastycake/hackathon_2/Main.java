package me.tastycake.hackathon_2;

import me.tastycake.hackathon_2.calendar.PupilCalendar;
import me.tastycake.hackathon_2.chatgpt.imple.GPT;
import me.tastycake.hackathon_2.hobbies.Food;
import me.tastycake.hackathon_2.hobbies.Hobby;
import me.tastycake.hackathon_2.mysql.MySQL;
import me.tastycake.hackathon_2.serializer.Serializer;
import me.tastycake.hackathon_2.suggestion.Suggestion;
import me.tastycake.hackathon_2.user.School;
import me.tastycake.hackathon_2.user.User;
import me.tastycake.hackathon_2.user.imple.Pupil;
import me.tastycake.hackathon_2.user.imple.Teacher;
import me.tastycake.hackathon_2.user.school.SchoolClass;
import me.tastycake.hackathon_2.user.school.SchoolFloor;

import java.sql.SQLType;
import java.util.ArrayList;
import java.util.UUID;

public class Main {
    public static GPT gpt;
    public static MySQL mySQL;
    public static User activeUser;

    public static void main(String[] args) {
        try {
            mySQL = new MySQL();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        GPT.ResultListener resultListener = new GPT.ResultListener() {
            @Override
            public void sendResults(String v1, String v2, Pupil v3, Pupil v4) {


                String v3Suggestion = Suggestion.generate(Suggestion.Prompt.builder()
                        .self(v3)
                        .user2(v4)
                        .food(v1)
                        .hobby(v2)
                        .build());
                String v4Suggestion = Suggestion.generate(Suggestion.Prompt.builder()
                        .self(v4)
                        .user2(v3)
                        .food(v1)
                        .hobby(v2)
                        .build());

                System.out.println(v3.getName() + ":   " + v3Suggestion);
                System.out.println(v4.getName() + ":   " + v4Suggestion);
            }

            @Override
            public void sendSucc(String s) {
                System.out.println(s);
            }

            @Override
            public void sendErr(String s) {
                System.out.println(s);
            }
        };

        School school = new School(5, 7);

        SchoolClass schoolClass = new SchoolClass();
        SchoolFloor schoolFloor = new SchoolFloor();
        schoolFloor.getSchoolClasses().add(schoolClass);

        gpt = new GPT(resultListener);

        Teacher teacher = new Teacher("mora", "mora@gmail.com", "mora123", schoolClass, new ArrayList<>());

        schoolClass.setTeacher(teacher);
        schoolClass.setId(UUID.randomUUID().toString());

        Pupil user1 = new Pupil("עמית", "amit@gmail.com", "test123", new ArrayList<>() {{
            add(new Hobby("כדורגל", true));
            add(new Hobby("שחמט", true));
            add(new Hobby("פורטנייט", false));
        }}, new ArrayList<>() {{
            add(new Food("ציפס", true));
            add(new Food("פיצה", true));
            add(new Food("האמבורגר", true));
        }}, null, schoolClass, new ArrayList<>(), 0);

        user1.setPupilCalendar(new PupilCalendar(user1));

        // Serializer.newSerializable(user1);

        String id = UUID.randomUUID().toString();

        // JSONObject main = Serializer.serializeAndSave("mail", user1.getMail(), "",  user1);


        try {
            // byte[] s = Serializer.byteSerialize(user1);
            // mySQL.saveToKey("mail", user1.getMail(), s);

            // byte[] s = Serializer.byteSerialize(schoolClass);
            // mySQL.saveToKey("class_data", "id", schoolClass.getId(), s);

            // User deserialized = (User) mySQL.getById("mail", "tomer@gmail.com", "user_data");
            System.out.println(user1.getPassword());
            Pupil p = (Pupil) Serializer.byteDeserialize(Serializer.byteSerialize(user1));
            System.out.println("Clone password: " + p.getPassword());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        try {
//            Pupil deserialized = (Pupil) Serializer.deserialize("mail", user1.getMail(), Pupil.class);
//            System.out.println(deserialized);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

//    public void setupSchool() {
//        // Listener
//        GPT.ResultListener resultListener = new GPT.ResultListener() {
//            @Override
//            public void sendResults(String v1, String v2, Pupil v3, Pupil v4) {
//
//
//                String v3Suggestion = Suggestion.generate(Suggestion.Prompt.builder()
//                        .self(v3)
//                        .user2(v4)
//                        .food(v1)
//                        .hobby(v2)
//                        .build());
//                String v4Suggestion = Suggestion.generate(Suggestion.Prompt.builder()
//                        .self(v4)
//                        .user2(v3)
//                        .food(v1)
//                        .hobby(v2)
//                        .build());
//
//                System.out.println(v3.getName() + ":   " + v3Suggestion);
//                System.out.println(v4.getName() + ":   " + v4Suggestion);
//            }
//
//            @Override
//            public void sendSucc(String s) {
//                System.out.println(s);
//            }
//
//            @Override
//            public void sendErr(String s) {
//                System.out.println(s);
//            }
//        };
//
//
//        // School
//        School school = new School(5, 7);
//
//        SchoolClass schoolClass = new SchoolClass();
//        SchoolFloor schoolFloor = new SchoolFloor();
//        schoolFloor.getSchoolClasses().add(schoolClass);
//
//        gpt = new GPT(resultListener);
//
//        Teacher teacher = new Teacher(school, schoolFloor, schoolClass, new ArrayList<>());
//
//        schoolClass.setTeacher(teacher);
//
//
//        // Pupils
//        Pupil user1 = new Pupil("עמית", "test@gmail.com", new ArrayList<>() {{
//            add(new Hobby("כדורגל", true));
//            add(new Hobby("שחמט", true));
//            add(new Hobby("פורטנייט", false));
//        }}, new ArrayList<>() {{
//            add(new Food("ציפס", true));
//            add(new Food("פיצה", true));
//            add(new Food("האמבורגר", true));
//        }}, null, schoolClass, schoolFloor, school, new ArrayList<>(), 0);
//
//        user1.setPupilCalendar(new PupilCalendar(user1));
//
//        Pupil user2 = new Pupil("יוסי", "test@gmail.com", new ArrayList<>() {{
//            add(new Hobby("מיינקראפט", true));
//            add(new Hobby("שחמט", true));
//            add(new Hobby("פורטנייט", true));
//        }}, new ArrayList<>() {{
//            add(new Food("פסטה", true));
//            add(new Food("פיצה", false));
//            add(new Food("האמבורגר", true));
//        }}, null, schoolClass, schoolFloor, school, new ArrayList<>(), 0);
//
//        user2.setPupilCalendar(new PupilCalendar(user2));
//
//        Pupil user3 = new Pupil("דן", "test@gmail.com", new ArrayList<>() {{
//            add(new Hobby("מיינקראפט", true));
//            add(new Hobby("שחמט", true));
//            add(new Hobby("פורטנייט", false));
//        }}, new ArrayList<>() {{
//            add(new Food("פסטה", false));
//            add(new Food("פיצה", false));
//            add(new Food("האמבורגר", true));
//        }}, null, schoolClass, schoolFloor, school, new ArrayList<>(), 0);
//
//        user3.setPupilCalendar(new PupilCalendar(user3));
//
//        Pupil user4 = new Pupil("רועי", "test@gmail.com", new ArrayList<>() {{
//            add(new Hobby("מיינקראפט", true));
//            add(new Hobby("שחמט", false));
//            add(new Hobby("פורטנייט", true));
//        }}, new ArrayList<>() {{
//            add(new Food("פסטה", false));
//            add(new Food("פיצה", true));
//            add(new Food("האמבורגר", false));
//        }}, null, schoolClass, schoolFloor, school, new ArrayList<>(), 0);
//
//        user4.setPupilCalendar(new PupilCalendar(user4));
//
//        schoolClass.setPupils(new ArrayList<>() {{
//            add(user1);
//            add(user2);
//            add(user3);
//            add(user4);
//        }});
//
//        DateActivity.pick(schoolClass);
//    }
}