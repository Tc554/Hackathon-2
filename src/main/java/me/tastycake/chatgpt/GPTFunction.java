package me.tastycake.chatgpt;

public class GPTFunction {
    public GPTFunction(String name, GPTRunnable action, String description, String parm, String parm2, String parmsDescription) {
        this.name = name;
        this.action = action;
        this.description = description;
        this.parm = parm;
        this.parm2 = parm2;
        this.parmsDescription = parmsDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GPTRunnable getAction() {
        return action;
    }

    public void setAction(GPTRunnable action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParm() {
        return parm;
    }

    public String getParm2() {
        return parm2;
    }

    public void setParm(String parm) {
        this.parm = parm;
    }

    public String getParmsDescription() {
        return parmsDescription;
    }

    public void setParmsDescription(String parmsDescription) {
        this.parmsDescription = parmsDescription;
    }

    private String name;
    private GPTRunnable action;
    private String description;
    private String parm;
    private String parm2;
    private String parmsDescription;
}
