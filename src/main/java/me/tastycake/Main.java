package me.tastycake;

import me.tastycake.chatgpt.GPTAPI;
import me.tastycake.chatgpt.GPTFunction;
import me.tastycake.chatgpt.GPTPrompt;
import me.tastycake.chatgpt.GPTRunnable;
import me.tastycake.chatgpt.imple.GPT;
import me.tastycake.hobbies.Food;
import me.tastycake.hobbies.Hobby;
import me.tastycake.suggestion.Suggestion;
import me.tastycake.user.imple.Pupil;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        GPT.ResultListener resultListener = new GPT.ResultListener() {
            @Override
            public void sendSucc(String s) {
                System.out.println(s);
            }

            @Override
            public void sendErr(String s) {
                System.out.println(s);
            }
        };

        GPT gpt = new GPT(resultListener);

        Pupil user1 = Pupil.builder()
                .name("User 1")

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

        Pupil user2 = Pupil.builder()
                .name("User 2")

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

        Suggestion.Prompt prompt = Suggestion.Prompt.builder()
                .self(user1)
                .user2(user2)
                .build();

        // gptTest(prompt, user1, user2);

        gpt.send(prompt);
    }

//    public static String gptTest(String prompt, Pupil user1, Pupil user2) {
//        String en = "You are here to help 2 people choose what to eat and what hobby to do, Each person will tell you what he likes to eat and what are his hobbies and what they hate to eat and what are his hated hobbies then you need to call the function choose with the food both of them most likely would like and the hobby they most likely would like to do together";
//        String he = "אתה כאן בשביל לעזור ל 2 אנשים לבחור מה לאכול ואיזה תחביב לעשות, כל איש יגיד לך מה הוא אוהב לאכול ומה התחביבים שלו ומה הוא לא אוהב לאכול ואיזה תחביבים הוא לא אוהב ואז אתה תבחר את המאכל ששניהם אוהבים לאכול ואת התחביב ששניהם אוהבים, תענה הכל בעברית";
//
//        GPTPrompt[] prompts = new GPTPrompt[]{new GPTPrompt(GPTPrompt.PromptType.system, he),
//                // new GPTPrompt(GPTPrompt.PromptType.user,"User 1 likes to eat Pizza and pasta and loves to play soccer, basketball and chess, User2 likes Hamburger and chips but hate pasta and loves to play chess and fortnite but hates soccer")
//                new GPTPrompt(GPTPrompt.PromptType.user, prompt)
//        };
//
//        gpt.setT(0);
//
//        String output = gpt.sendFunction(prompts, new GPTFunction[]{new GPTFunction("choose", new GPTRunnable() {
//            @Override
//            public String[] run(String parms) {
//                System.out.println("They should eat " + parms + " together");
//                return new String[] {parms};
//            }
//
//            @Override
//            public String[] run(String v1, String v2) {
//                System.out.println(Suggestion.generate(Suggestion.Prompt.builder()
//                        .self(user1)
//                        .user2(user2)
//                        .food(v1)
//                        .hobby(v2)
//                        .build()));
//                return new String[] {v1, v2};
//            }
//        }, "איזה אוכל ותחביב הם שניהם אוהבים", "food type", "hobby type", "המאכל ששניהם אוהבים והתחביב שהם אוהבים")});
//                // ,"Lets us know what food and hobby to pick","food type", "hobby type","should be the food they should eat and the hobby they should do together")});
//
////        System.out.println("GPT RESPONSE:");
////        System.out.println(output.replace("\\n","\n"));
//
//        return output;
//    }
}