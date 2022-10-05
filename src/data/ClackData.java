package data;

import java.util.Date;

public abstract class ClackData {
    private String username;
    private int type;
    private Date date;

    public ClackData(String username, int type) {
        this.username = username;
        this.type = type;
    }

    public ClackData(int type){
        this("Anon", type);
    }

    public ClackData(){
        this("Anon", 1);
    }

    public int getType(){
        return type;
    }
    public String getUsername(){
        return username;
    }

    public Date getDate() {
        return date;
    }

    public abstract String getData();
}
