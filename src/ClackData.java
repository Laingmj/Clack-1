import java.util.Date;

public abstract class ClackData {
    
    public final int CONSTANT_LISTUSERS = 0;
    public final int CONSTANT_LOGOUT = 1;
    public final int CONSTANT_SENDMESSAGE = 2;
    public final int CONSTANT_SENDFILE = 3;
    
    private String username;
    private int type;
    private Date date;

    //constructors
    public ClackData(String username, int type) {
        this.username = username;
        this.type = type;
    }

    public ClackData(int type){
        this("Anon", type);
    }

    public ClackData(){
        this("Anon", 0);
    }
//getters
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
