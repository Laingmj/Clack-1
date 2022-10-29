package data;

import java.util.Objects;
import java.io.*;
import java.util.ArrayList;
/**
 * The child of ClackData, whose data is the name and contents of a file.
 *
 * @author Michael Laing
 */
public class FileClackData extends ClackData {
    private String fileName;  // A string representing the name of a file
    private String fileContents;  // A string representing the contents of a file

    /**
     * The constructor to set up the instance variables username, fileName, and type.
     * Should call the super constructor.
     * The instance variable fileContents should be initialized to be null.
     *
     * @param userName a string representing the name of the client user
     * @param fileName a string representing the name of a file
     * @param type     an int representing the data type
     */
    public FileClackData(String userName, String fileName, int type) {
        super(userName, type);
        this.fileName = fileName;
        this.fileContents = null;
    }

    /**
     * The default constructor.
     * This constructor should call the super constructor.
     */
    public FileClackData() {
        super(ClackData.CONSTANT_SENDFILE);
        this.fileName = "";
        this.fileContents = null;
    }

    /**
     * Sets the file name in this object.
     *
     * @param fileName a string representing the name of a file
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the file name.
     *
     * @return this.fileName
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Returns the file contents.
     *
     * @return this.fileContent
     */
    public String getData() {
        return this.fileContents;
    }

    /**
     * Returns decrypted data from an encrypted file
     * @param key
     * @return
     */
    public String getData(String key){ return decrypt(this.fileContents, key); }
    /**
     * Reads the file contents.
     */
    public void readFileContents() throws IOException {
        StringBuilder sb = new StringBuilder();
        try(FileInputStream istream = new FileInputStream(fileName)){

            // reads a byte at a time, if it has reached the end of the file, it returns -1
            while (istream.read() != -1) {
                sb.append((char)istream.read());
            }
        } catch (FileNotFoundException f) {
            System.err.println("File not found: " + fileName);
        }
        fileContents = sb.toString();
    }

    /**
     * reads file contents and encrypts them.
     * @param key
     * @throws IOException
     */
    public void readFileContents(String key) throws IOException {
        this.readFileContents();
        fileContents = encrypt(fileContents, key);
    }

    /**
     * Writes the file contents.
     */
    public void writeFileContents() throws IOException {

        try(FileOutputStream oStream = new FileOutputStream(fileName)) {
            byte[] strToBytes = fileContents.getBytes();
            oStream.write(strToBytes);
            oStream.flush();
        } catch (FileNotFoundException f){
            System.err.println("File Not Found: " + fileName);
        } catch (SecurityException s){
            System.err.println("Security Exception trying to write to file: " + fileName);
        }

    }

    /**
     * Decrypts file contents and writes them to a file.
     * @param key
     * @throws IOException
     */
    public void writeFileContents(String key) throws IOException {
        try (FileOutputStream oStream = new FileOutputStream(fileName)){
            byte[] strToBytes = decrypt(fileContents, key).getBytes();
            oStream.write(strToBytes);
            oStream.flush();
        } catch(FileNotFoundException f){
            System.err.println("File Not Found: " + fileName);
        } catch (SecurityException s){
            System.err.println("Security Exception trying to write to file: " + fileName);
        }
    }

    @Override
    public int hashCode() {
        // The following is only one of many possible implementations to generate the hash code.
        // See the hashCode() method in other classes for some different implementations.
        // It is okay to select only some of the instance variables to calculate the hash code
        // but must use the same instance variables with equals() to maintain consistency.
        return Objects.hash(this.userName, this.type, this.fileName, this.fileContents);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FileClackData)) {
            return false;
        }

        // Casts other to be a FileClackData to access its instance variables.
        FileClackData otherFileClackData = (FileClackData) other;

        // Compares the selected instance variables of both FileClackData objects that determine uniqueness.
        // It is okay to select only some of the instance variables for comparison but must use the same
        // instance variables with hashCode() to maintain consistency.
        return this.userName.equals(otherFileClackData.userName)
                && this.type == otherFileClackData.type
                && Objects.equals(this.fileName, otherFileClackData.fileName)
                && Objects.equals(this.fileContents, otherFileClackData.fileContents);
    }

    @Override
    public String toString() {
        // Should return a full description of the class with all instance variables,
        // including those in the super class.
        return "This instance of FileClackData has the following properties:\n"
                + "Username: " + this.userName + "\n"
                + "Type: " + this.type + "\n"
                + "Date: " + this.date.toString() + "\n"
                + "File Name: " + this.fileName + "\n"
                + "File Contents: " + this.fileContents + "\n";
    }
}
