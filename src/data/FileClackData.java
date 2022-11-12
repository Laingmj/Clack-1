package data;

import java.io.*;
import java.util.Objects;

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

    @Override
    public String getData() {
        return this.fileContents;
    }

    @Override
    public String getData(String key) {
        return decrypt(this.fileContents, key);
    }

    /**
     * Does non-secure file reads. Opens the file pointed to by the this.fileName,
     * reads the contents of the file, and closes the file.
     * Does not return anything.
     *
     * @throws IOException if the file reads fail for any I/O issues other than file not found
     */
    public void readFileContents() throws IOException {
        this.fileContents = readFileContentsHelper();
    }

    /**
     * Does secure file reads. Opens the file pointed to by the this.fileName,
     * reads the contents of the file, encrypts the contents to this.fileContents
     * using the given key, and closes the file.
     * Does not return anything.
     *
     * @param key a string used as the key to encrypt the file contents
     * @throws IOException if the file reads fail for any I/O issues other than file not found
     */
    public void readFileContents(String key) throws IOException {
        this.fileContents = encrypt(readFileContentsHelper(), key);
    }

    /**
     * Does non-secure file writes. Opens the file pointed to by the this.fileName,
     * writes the contents of this.fileContents to the file, and closes the file.
     * Does not return anything.
     */
    public void writeFileContents() {
        writeFileContentsHelper(this.fileContents);
    }

    /**
     * Does secure file writes. Opens the file pointed to by the this.fileName,
     * decrypts the contents of this.fileContents and writes them to the file,
     * and closes the file.
     * Does not return anything.
     *
     * @param key a string used as the key to decrypt the file contents
     */
    public void writeFileContents(String key) {
        writeFileContentsHelper(decrypt(this.fileContents, key));
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

    /**
     * The helper method for both readFileContents(). Opens the file pointed to
     * by the this.fileName, reads the contents of the file, closes the file, and
     * returns it as a string.
     *
     * @return a string representing the file contents read
     * @throws IOException if file reads fail for any reason other than file not found
     */
    private String readFileContentsHelper() throws IOException {
        StringBuilder fileContentsReadBuilder = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(this.fileName));
            String line;

            while ((line = br.readLine()) != null) {
                fileContentsReadBuilder.append(line).append("\n");
            }

            br.close();

        } catch (FileNotFoundException fnfe) {
            System.err.println("FileNotFoundException occurs when reading a file: " + fnfe.getMessage());
        }

        return fileContentsReadBuilder.toString();
    }

    /**
     * The helper method for both writeFileContents(). Opens the file pointed to
     * by the this.fileName, writes the given string to the file, and closes the file.
     *
     * @param fileContentsToWrite a string representing the file contents to write
     */
    private void writeFileContentsHelper(String fileContentsToWrite) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName));
            bw.write(fileContentsToWrite);
            bw.close();

        } catch (FileNotFoundException fnfe) {
            System.err.println("FileNotFoundException occurs when writing to a file: " + fnfe.getMessage());

        } catch (IOException ioe) {
            System.err.println("IOException occurs when writing to a file: " + ioe.getMessage());
        }
    }
}
