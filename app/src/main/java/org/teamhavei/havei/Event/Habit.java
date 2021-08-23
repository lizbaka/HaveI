package org.teamhavei.havei.Event;

public class Habit extends HaveIEvent{

    private int repeatUnit;//目标计数周期，天为单位
    private int repeatTimes;//目标周期内计数次数
    private String reminderTime;//提醒时间 格式HH:MM

    public int getRepeatUnit() {
        return repeatUnit;
    }

    public void setRepeatUnit(int repeatUnit) {
        this.repeatUnit = repeatUnit;
    }

    public int getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeatTimes(int repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }
}
