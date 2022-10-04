public class MessageClackData extends ClackData{
    private String message;

    public MessageClackData(String username, String message, int type){
        super(username, type);
        this.message = message;
    }
    public MessageClackData(){
        this("Anon", "", 1);
    }
    public String getData(){
        return message;
    }

    public boolean equals(MessageClackData m){
        return this.hashCode()==(m.hashCode());
    }
    public String toString(){
        return this.getUsername() + " " + this.getType() + " " + message;
    }

    public int hashCode(){
        return 5 * this.getUsername().hashCode() + message.hashCode() * this.getType();
    }
}
