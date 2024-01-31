package me.tastycake.chatgpt;

public class GPTPrompt {
    public GPTPrompt(PromptType type, String data) {
        this.type = type;
        this.data = data;
    }

    public PromptType type;
    public String data;

    public static enum PromptType {
        system("system"),
        user("user"),
        assistant("assistant");

        String name;

        PromptType(String name) {
            this.name = name;
        }
    }
}
