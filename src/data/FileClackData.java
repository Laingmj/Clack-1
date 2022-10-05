package data;

public class FileClackData extends ClackData{
    private String fileName;
    private String fileContents;

    public FileClackData(String username, String fileName, int type){
        super(username,type);
        this.fileName=fileName;
    }
    public FileClackData(){
        this("Anon", "name", 3);
    }

    //getters

    public String getData(){
        return fileContents;
    }

    public String getFileName(){
        return fileName;
    }

    //setters

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    //unimplemented

    public void readFileContents(){}

    public void writeFileContents(){}

    //overrides

    public String toString(){
        return "Username: " + this.getUsername() + " File Name: " + this.getFileName();
    }

    public int hashCode(){
        return 10 * (this.getUsername().hashCode() + this.getFileName().hashCode() * this.getType());
    }

    public boolean equals(FileClackData f){
        return this.hashCode() == f.hashCode();
    }

}
