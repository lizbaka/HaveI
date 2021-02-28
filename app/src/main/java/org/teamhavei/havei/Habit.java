package org.teamhavei.havei;

import androidx.annotation.Nullable;

public class Habit {

    public static final int FREQUENCY_PER_DAY = 1;
    public static final int FREQUENCY_PER_WEEK = 2;
    public static final int FREQUENCY_PER_MONTH = 3;

    private String habitName;//名称
    private String habitTag;//标签
    private int habitFrequency;//频率：次数
    private int habitFrequencyPer;//频率：时间单位，对应上方的3个常量
    private int habitExecTimes;//已执行次数

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

    public int getHabitExecTimes() {
        return habitExecTimes;
    }

    public void setHabitExecTimes(int habitExecTimes) {
        this.habitExecTimes = habitExecTimes;
    }
}