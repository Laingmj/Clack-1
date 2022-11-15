package test;

import data.ClackData;
import data.FileClackData;
import data.MessageClackData;

import java.io.IOException;

/**
 * You don't have to have a Javadoc for a test class.
 * You should test all implemented functions in the data classes,
 * whether they are in the superclass or in the subclasses.
 */
public class TestClackData {
    public static void main(String[] args) {
        // The key for encryption and decryption.
        final String KEY = "TIME";

        // Tests both constructors of MessageClackData.
        MessageClackData messageClackData1 = new MessageClackData();
        MessageClackData messageClackData2 =
                new MessageClackData("testUser1", "testMessage", ClackData.CONSTANT_SENDMESSAGE);
        MessageClackData messageClackData3 =
                new MessageClackData("testUser2", "testMessage", KEY, ClackData.CONSTANT_SENDMESSAGE);

        // Tests both constructors of FileClackData.
        FileClackData fileClackData1 = new FileClackData();
        FileClackData fileClackData2 =
                new FileClackData("testUser2", "filename0", ClackData.CONSTANT_SENDFILE);

        // Tests int getType().
        System.out.println("messageClackData1 getType(): " + messageClackData1.getType());
        System.out.println("messageClackData2 getType(): " + messageClackData2.getType());
        System.out.println("fileClackData1 getType(): " + fileClackData1.getType());
        System.out.println("fileClackData2 getType(): " + fileClackData2.getType());
        System.out.println();

        // Tests String getUserName().
        System.out.println("messageClackData1 getUserName(): " + messageClackData1.getUserName());
        System.out.println("messageClackData2 getUserName(): " + messageClackData2.getUserName());
        System.out.println("fileClackData1 getUserName(): " + fileClackData1.getUserName());
        System.out.println("fileClackData2 getUserName(): " + fileClackData2.getUserName());
        System.out.println();

        // Tests Date getDate().
        System.out.println("messageClackData1 getDate(): " + messageClackData1.getDate());
        System.out.println("messageClackData2 getDate(): " + messageClackData2.getDate());
        System.out.println("fileClackData1 getDate(): " + fileClackData1.getDate());
        System.out.println("fileClackData2 getDate(): " + fileClackData2.getDate());
        System.out.println();

        // Tests String getData().
        System.out.println("messageClackData1 getData(): " + messageClackData1.getData());
        System.out.println("messageClackData2 getData(): " + messageClackData2.getData());
        System.out.println("fileClackData1 getData(): " + fileClackData1.getData());
        System.out.println("fileClackData2 getData(): " + fileClackData2.getData());
        System.out.println();

        // Tests String getData(String key) of MessageClackData.
        System.out.println("messageClackData3 getData(): " + messageClackData3.getData());
        System.out.println("Using the key: " + KEY);
        System.out.println("messageClackData3 getData(key): " + messageClackData3.getData(KEY));
        System.out.println();

        // Tests int hashCode().
        System.out.println("messageClackData1 hashCode(): " + messageClackData1.hashCode());
        System.out.println("messageClackData2 hashCode(): " + messageClackData2.hashCode());
        System.out.println("fileClackData1 hashCode(): " + fileClackData1.hashCode());
        System.out.println("fileClackData2 hashCode(): " + fileClackData2.hashCode());
        System.out.println();

        // Tests boolean equals(Object other).
        System.out.println("messageClackData1 equals null: "
                + messageClackData1.equals(null));
        System.out.println("messageClackData1 equals messageClackData1: "
                + messageClackData1.equals(messageClackData1));
        System.out.println("messageClackData1 equals messageClackData2: "
                + messageClackData1.equals(messageClackData2));
        System.out.println("messageClackData2 equals messageClackData1: "
                + messageClackData2.equals(messageClackData1));
        System.out.println("messageClackData1 equals fileClackData1: "
                + messageClackData1.equals(fileClackData1));
        System.out.println("fileClackData1 equals null: "
                + fileClackData1.equals(null));
        System.out.println("fileClackData1 equals fileClackData1: "
                + fileClackData1.equals(fileClackData1));
        System.out.println("fileClackData1 equals fileClackData2: "
                + fileClackData1.equals(fileClackData2));
        System.out.println("fileClackData2 equals fileClackData1: "
                + fileClackData2.equals(fileClackData1));
        System.out.println("fileClackData1 equals messageClackData1: "
                + fileClackData1.equals(messageClackData1));
        System.out.println();

        // Tests String toString().
        System.out.println("messageClackData1 toString():\n" + messageClackData1);
        System.out.println("messageClackData2 toString():\n" + messageClackData2);
        System.out.println("fileClackData1 toString():\n" + fileClackData1);
        System.out.println("fileClackData2 toString():\n" + fileClackData2);
        System.out.println();

        // Tests String getFileName() and void setFileName(String fileName) of FileClackData.
        System.out.println("fileClackData1 getFileName(): " + fileClackData1.getFileName());
        String filename1 = "filename1";
        System.out.println("Sets the filename of fileClackData1 to be " + filename1);
        fileClackData1.setFileName(filename1);
        System.out.println("fileClackData1 getFileName(): " + fileClackData1.getFileName());
        System.out.println("fileClackData1 hashCode(): " + fileClackData1.hashCode());
        System.out.println("fileClackData1 equals fileClackData1: " + fileClackData1.equals(fileClackData1));
        System.out.println("fileClackData1 equals fileClackData2: " + fileClackData1.equals(fileClackData2));
        System.out.println("fileClackData1 toString():\n" + fileClackData1);
        System.out.println();

        System.out.println("fileClackData2 getFileName(): " + fileClackData2.getFileName());
        String filename2 = "filename2";
        System.out.println("Sets the filename of fileClackData2 to be " + filename2);
        fileClackData2.setFileName(filename2);
        System.out.println("fileClackData2 getFileName(): " + fileClackData2.getFileName());
        System.out.println("fileClackData2 hashCode(): " + fileClackData2.hashCode());
        System.out.println("fileClackData2 equals fileClackData2: " + fileClackData2.equals(fileClackData2));
        System.out.println("fileClackData2 equals fileClackData1: " + fileClackData2.equals(fileClackData1));
        System.out.println("fileClackData2 toString():\n" + fileClackData2);
        System.out.println();

        // Tests void readFileContents() of FileClackData.
        try {
            System.out.println("fileClackData1 readFileContents():");
            fileClackData1.setFileName("Part2_document.txt");
            System.out.println("Reading from the file: " + fileClackData1.getFileName());
            fileClackData1.readFileContents();
            System.out.println("fileClackData1 getData(): " + fileClackData1.getData());
            System.out.println();

            System.out.println("fileClackData2 readFileContents():");
            fileClackData2.setFileName("wrong_file");
            System.out.println("Error should be printed out when reading from the file: "
                    + fileClackData2.getFileName());
            fileClackData2.readFileContents();
            System.out.println();

        } catch (Exception ioe) {
            System.err.println("Exception should not be thrown.");
        }

        // Tests void writeFileContents() of FileClackData.
        System.out.println("fileClackData1 writeFileContents():");
        fileClackData1.setFileName("test_file_write.txt");
        System.out.println("Writing to the file: " + fileClackData1.getFileName());
        fileClackData1.writeFileContents();
        System.out.println();

        System.out.println("fileClackData2 writeFileContents():");
        fileClackData2.setFileName("./wrong_path/wrong_file");
        System.out.println("Error should be printed out when writing to the file: "
                + fileClackData2.getFileName());
        fileClackData2.writeFileContents();
        System.out.println();

        // Tests void readFileContents(String key) and String getData(String key) of FileClackData.
        // Indirectly tests String encrypt(String inputStringToEncrypt, String key) and
        // String decrypt(String inputStringToDecrypt, String key) of ClackData.
        try {
            System.out.println("fileClackData1 readFileContents(key):");
            System.out.println("Using the key: " + KEY);
            fileClackData1.setFileName("Part2_document.txt");
            System.out.println("Reading from the file: " + fileClackData1.getFileName());
            fileClackData1.readFileContents(KEY);
            System.out.println("fileClackData1 getData(): " + fileClackData1.getData());
            System.out.println("fileClackData1 getData(key): " + fileClackData1.getData(KEY));
            System.out.println();

            System.out.println("fileClackData2 readFileContents(key):");
            System.out.println("Using the key: " + KEY);
            fileClackData2.setFileName("wrong_file");
            System.out.println("Error should be printed out when reading from the file: "
                    + fileClackData2.getFileName());
            fileClackData2.readFileContents(KEY);
            System.out.println();

        } catch (Exception ioe) {
            System.err.println("Exception should not be thrown.");
        }

        // Tests void writeFileContents(String key) of FileClackData.
        // Indirectly tests String encrypt(String inputStringToEncrypt, String key) and
        // String decrypt(String inputStringToDecrypt, String key) of ClackData.
        System.out.println("fileClackData1 writeFileContents() for encrypted file contents:");
        fileClackData1.setFileName("test_file_write_encrypted.txt");
        System.out.println("Writing to the file: " + fileClackData1.getFileName());
        fileClackData1.writeFileContents();
        System.out.println();

        System.out.println("fileClackData1 writeFileContents(key) for encrypted file contents:");
        System.out.println("Using the key: " + KEY);
        fileClackData1.setFileName("test_file_write_decrypted.txt");
        System.out.println("Writing to the file: " + fileClackData1.getFileName());
        fileClackData1.writeFileContents(KEY);
        System.out.println();

        System.out.println("fileClackData2 writeFileContents(key):");
        System.out.println("Using the key: " + KEY);
        fileClackData2.setFileName("./wrong_path/wrong_file");
        System.out.println("Error should be printed out when writing to the file: "
                + fileClackData2.getFileName());
        fileClackData2.writeFileContents(KEY);
    }
}
