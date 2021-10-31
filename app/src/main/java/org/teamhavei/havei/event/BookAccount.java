package org.teamhavei.havei.event;

public class BookAccount {

    private int id;//账户id
    private String name;//账户名称
    private int iconId;//账户图标id
    private double init;//账户初始存款

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public double getInit() {
        return init;
    }

    public void setInit(double init) {
        this.init = init;
    }
}
