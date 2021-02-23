package org.teamhavei.havei;

public class Habits {

    public static final int FREQUENCY_PER_DAY = 1;
    public static final int FREQUENCY_PER_WEEK = 2;
    public static final int FREQUENCY_PER_MONTH = 3;

    private String habitName;
    private String habitTag;
    private int habitFrequency;
    private int habitFrequencyPer;

    public Habits(String habitName, String habitTag, int habitFrequency, int habitFrequencyPer) {
        this.habitName = habitName;
        this.habitTag = habitTag;
        this.habitFrequency = habitFrequency;
        this.habitFrequencyPer = habitFrequencyPer;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public String getHabitTag() {
        return habitTag;
    }

    public void setHabitTag(String habitTag) {
        this.habitTag = habitTag;
    }

    public int getHabitFrequency() {
        return habitFrequency;
    }

    public void setHabitFrequency(int habitFrequency) {
        this.habitFrequency = habitFrequency;
    }

    public int getHabitFrequencyPer() {
        return habitFrequencyPer;
    }

    public void setHabitFrequencyPer(int habitFrequencyPer) {
        this.habitFrequencyPer = habitFrequencyPer;
    }
}
