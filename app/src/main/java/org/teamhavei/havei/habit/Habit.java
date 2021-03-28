package org.teamhavei.havei.habit;

public class Habit {

    private int habitID;
    private String habitName;//名称
    private String habitTag;//标签

    public int getHabitID() {
        return habitID;
    }

    public void setHabitID(int habitID) {
        this.habitID = habitID;
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
}
