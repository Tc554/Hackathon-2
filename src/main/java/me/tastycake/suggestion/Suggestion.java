package me.tastycake.suggestion;

import lombok.Builder;
import lombok.Getter;
import me.tastycake.hobbies.Food;
import me.tastycake.hobbies.Hobby;
import me.tastycake.user.imple.Pupil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Suggestion {
    public static Random random = new Random();

    public static String generate(Prompt prompt) {
        return prompt.getString();
    }

    public static String generate(Prompt prompt, Prompt.Premade premade) {
        return prompt.getString(premade);
    }

    public static String generate(Prompt prompt, String style) {
        return prompt.getString(style);
    }

    @Getter
    @Builder
    public static class Prompt {
        private Pupil self;
        private Pupil user2;
        private String food;
        private String hobby;
        private boolean normalMeeting = true;
        private List<Pupil> additional = new ArrayList<>();

        public String getString() {
            return getString(Premade.values()[random.nextInt(0, Premade.values().length)]);
        }

        public String getString(Premade premade) {
            return getString(premade.getPrompt());
        }

        public String getString(String style) {
            style = style.replaceAll("\\{self}", self.getName())
                    .replaceAll("\\{u}", user2.getName())
                    .replaceAll("\\{hobby}", hobby)
                    .replaceAll("\\{food}", food);
            return style;
        }

        public String getGPTPrompt() {
            return getGPTPrompt(self.getFoods(), user2.getFoods(), self.getHobbies(), user2.getHobbies());
        }

        public String getGPTPrompt(List<Food> u1Food, List<Food> u2Food, List<Hobby> u1Hobby, List<Hobby> u2Hobby) {
            StringBuilder f1 = new StringBuilder("User 1 רשימת אוכל: ");
            StringBuilder f2 = new StringBuilder("User 2 רשימת אוכל: ");
            StringBuilder h1 = new StringBuilder("User 2 רשימת תחביבים: ");
            StringBuilder h2 = new StringBuilder("User 2 רשימת תחביבים: ");

            int i = 0;
            for (Food food : u1Food) {
                String subfix = ", ";
                if (i + 1 == u1Food.size()) {
                    subfix = "ו ";
                }

                if (food.isLikeIt()) {
                    f1.append(subfix).append("אוהב ").append(food.getFood());
                } else {
                    f1.append(subfix).append("לא אוהב ").append(food.getFood());
                }

                i++;
            }

            i = 0;
            for (Food food : u2Food) {
                String subfix = ", ";
                if (i + 1 == u2Food.size()) {
                    subfix = "ו ";
                }

                if (food.isLikeIt()) {
                    f2.append(subfix).append("אוהב ").append(food.getFood());
                } else {
                    f2.append(subfix).append("לא אוהב ").append(food.getFood());
                }

                i++;
            }

            i = 0;
            for (Hobby hobby : u1Hobby) {
                String subfix = ", ";
                if (i + 1 == u1Hobby.size()) {
                    subfix = "ו ";
                }

                if (hobby.isLikeIt()) {
                    h1.append(subfix).append("אוהב ").append(hobby.getHobby());
                } else {
                    h1.append(subfix).append("לא אוהב ").append(hobby.getHobby());
                }

                i++;
            }

            i = 0;
            for (Hobby hobby : u2Hobby) {
                String subfix = ", ";
                if (i + 1 == u2Hobby.size()) {
                    subfix = "ו ";
                }

                if (hobby.isLikeIt()) {
                    h2.append(subfix).append("אוהב ").append(hobby.getHobby());
                } else {
                    h2.append(subfix).append("לא אוהב ").append(hobby.getHobby());
                }

                i++;
            }

            f1.append(". ");
            f2.append(". ");
            h1.append(". ");
            h2.append(".");

            return String.valueOf(f1) + f2 + h1 + h2;
        }

        public enum Premade {
            FUN("איזה כיף! נבחרת להפגש עם {u} אולי תנסו לשחק ב{hobby} ולאכול {food}"),
            CHOSEN("והנבחר הוא {u}! שמעתי שאולי כדי לכם לשחק ב{hobby} ולאכול {food}");

            String prompt;

            Premade(String prompt) {
                this.prompt = prompt;
            }

            public String getPrompt() {
                return prompt;
            }
        }
    }
}
