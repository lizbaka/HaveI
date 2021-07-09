package org.teamhavei.havei.Event;
public class Account {
    private int id;
    private int tagID;
    private String name;
    private String time;//格式HH:MM
    private int PM;//计算参数
    private int iconId;
    public int getid(){return id;}
    public int gettag(){
        return tagID;
    }
    public String getname(){
        return name;
    }
    public int getPM()
    {
        return PM;
    }
    public String gettime(){
        return time;
    }
    public void setId(int id){
        this.id=id;
    }
    public void settag(int tag){
        this.tagID=tag;
    }
    public void setname(String name)
    {
        this.name=name;
    }
    public void setPM(int pm)
    {
        this.PM=pm;
    }
    public void setTime(String time)
    {
        this.time=time;
    }
    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

}
