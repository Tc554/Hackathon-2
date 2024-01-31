package me.tastycake;

import me.tastycake.calendar.DateActivity;
import me.tastycake.calendar.PupilCalendar;
import me.tastycake.chatgpt.GPTAPI;
import me.tastycake.chatgpt.GPTFunction;
import me.tastycake.chatgpt.GPTPrompt;
import me.tastycake.chatgpt.GPTRunnable;
import me.tastycake.chatgpt.imple.GPT;
import me.tastycake.hobbies.Food;
import me.tastycake.hobbies.Hobby;
import me.tastycake.suggestion.Suggestion;
import me.tastycake.user.School;
import me.tastycake.user.imple.Pupil;
import me.tastycake.user.imple.Teacher;
import me.tastycake.user.school.SchoolClass;
import me.tastycake.user.school.SchoolFloor;

import java.util.ArrayList;

public class Main {
    public static GPT gpt;

    public static void main(String[] args) {
        // Listener
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


        // School Setup
        School school = School.builder()
                .pointsPerActivity(5)
                .defTimeToFinishActivity(7)
                .build();

        SchoolClass schoolClass = new SchoolClass();
        SchoolFloor schoolFloor = new SchoolFloor();
        schoolFloor.getSchoolClasses().add(schoolClass);

        gpt = new GPT(resultListener);

        Teacher teacher = Teacher.builder()
                .schoolClass(schoolClass)
                .schoolFloor(schoolFloor)
                .school(school)
                .build();

        schoolClass.setTeacher(teacher);


        // Pupils
        if (true) {
            Pupil user1 = Pupil.builder()
                    .name("עמית")
                    .school(school)
                    .schoolClass(schoolClass)
                    .schoolFloor(schoolFloor)

                    .foods(new ArrayList<>() {{
                        add(new Food("ציפס", true));
                        add(new Food("פיצה", true));
                        add(new Food("האמבורגר", true));
                    }})
                    .hobbies(new ArrayList<>() {{
                        add(new Hobby("כדורגל", true));
                        add(new Hobby("שחמט", true));
                        add(new Hobby("פורטנייט", false));
                    }})
                    .build();
            user1.setPupilCalender(new PupilCalendar(user1));

            Pupil user2 = Pupil.builder()
                    .name("יוסי")
                    .school(school)
                    .schoolClass(schoolClass)
                    .schoolFloor(schoolFloor)

                    .foods(new ArrayList<>() {{
                        add(new Food("פסטה", true));
                        add(new Food("פיצה", false));
                        add(new Food("האמבורגר", true));
                    }})
                    .hobbies(new ArrayList<>() {{
                        add(new Hobby("מיינקראפט", true));
                        add(new Hobby("שחמט", true));
                        add(new Hobby("פורטנייט", true));
                    }})
                    .build();
            user2.setPupilCalender(new PupilCalendar(user2));

            Pupil user3 = Pupil.builder()
                    .name("משה")
                    .school(school)
                    .schoolClass(schoolClass)
                    .schoolFloor(schoolFloor)

                    .foods(new ArrayList<>() {{
                        add(new Food("פסטה", false));
                        add(new Food("פיצה", false));
                        add(new Food("האמבורגר", true));
                    }})
                    .hobbies(new ArrayList<>() {{
                        add(new Hobby("מיינקראפט", true));
                        add(new Hobby("שחמט", true));
                        add(new Hobby("פורטנייט", false));
                    }})
                    .build();
            user3.setPupilCalender(new PupilCalendar(user3));

            Pupil user4 = Pupil.builder()
                    .name("רועי")
                    .school(school)
                    .schoolClass(schoolClass)
                    .schoolFloor(schoolFloor)

                    .foods(new ArrayList<>() {{
                        add(new Food("פסטה", false));
                        add(new Food("פיצה", true));
                        add(new Food("האמבורגר", false));
                    }})
                    .hobbies(new ArrayList<>() {{
                        add(new Hobby("מיינקראפט", true));
                        add(new Hobby("שחמט", false));
                        add(new Hobby("פורטנייט", true));
                    }})
                    .build();
            user4.setPupilCalender(new PupilCalendar(user4));

            schoolClass.setPupils(new ArrayList<>() {{
                add(user1);
                add(user2);
                add(user3);
                add(user4);
            }});
        }

        DateActivity.pick(schoolClass);
    }
}