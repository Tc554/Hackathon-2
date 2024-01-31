package me.tastycake.chatgpt.imple;

import me.tastycake.chatgpt.GPTAPI;
import me.tastycake.chatgpt.GPTFunction;
import me.tastycake.chatgpt.GPTPrompt;
import me.tastycake.chatgpt.GPTRunnable;
import me.tastycake.suggestion.Suggestion;

public class GPT {
    private GPTAPI gpt = new GPTAPI("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", GPTAPI.Model.gpt_3_5_turbo_0613);
    private ResultListener resultListener;

    public GPT(ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public String send(Suggestion.Prompt prompt) {
        String en = "You are here to help 2 people choose what to eat and what hobby to do, Each person will tell you what he likes to eat and what are his hobbies and what they hate to eat and what are his hated hobbies then you need to call the function choose with the food both of them most likely would like and the hobby they most likely would like to do together";
        String he = "אתה כאן בשביל לעזור ל 2 אנשים לבחור מה לאכול ואיזה תחביב לעשות, כל איש יגיד לך מה הוא אוהב לאכול ומה התחביבים שלו ומה הוא לא אוהב לאכול ואיזה תחביבים הוא לא אוהב ואז אתה תבחר את המאכל ששניהם אוהבים לאכול ואת התחביב ששניהם אוהבים, תענה הכל בעברית";

        GPTPrompt[] prompts = new GPTPrompt[]{new GPTPrompt(GPTPrompt.PromptType.system, he),
                // new GPTPrompt(GPTPrompt.PromptType.user,"User 1 likes to eat Pizza and pasta and loves to play soccer, basketball and chess, User2 likes Hamburger and chips but hate pasta and loves to play chess and fortnite but hates soccer")
                new GPTPrompt(GPTPrompt.PromptType.user, prompt.getGPTPrompt())
        };

        gpt.setT(0);

        String output = gpt.sendFunction(prompts, new GPTFunction[]{new GPTFunction("choose", new GPTRunnable() {
            @Override
            public String[] run(String parms) {
                System.out.println("They should eat " + parms + " together");
                return new String[] {parms};
            }

            @Override
            public String[] run(String v1, String v2) {
                try {
                    String result = Suggestion.generate(Suggestion.Prompt.builder()
                            .self(prompt.getSelf())
                            .user2(prompt.getUser2())
                            .food(v1)
                            .hobby(v2)
                            .build());
                    resultListener.sendSucc(result);
                } catch (Exception e) {
                    resultListener.sendErr(e.getMessage());
                }
                Suggestion.generate(Suggestion.Prompt.builder()
                        .self(prompt.getSelf())
                        .user2(prompt.getUser2())
                        .food(v1)
                        .hobby(v2)
                        .build());
                return new String[] {v1, v2};
            }
        }, "איזה אוכל ותחביב הם שניהם אוהבים", "food type", "hobby type", "המאכל ששניהם אוהבים והתחביב שהם אוהבים")});
        // ,"Lets us know what food and hobby to pick","food type", "hobby type","should be the food they should eat and the hobby they should do together")});

//        System.out.println("GPT RESPONSE:");
//        System.out.println(output.replace("\\n","\n"));

        return output;
    }

    public interface ResultListener {
        void sendSucc(String s);
        void sendErr(String s);
    }
}
