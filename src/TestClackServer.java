public class TestClackServer {
    public static void main(String[] args) {
        //constructor and accessor tests
        main.ClackServer test1 = new main.ClackServer(100);
        System.out.println("port is: " + test1.getPort());

        main.ClackServer test2 = new main.ClackServer();
        System.out.println(test2);

        //hashCode and equals tests
        main.ClackServer test3 = test1;
        if (test1.equals(test2)) {
            System.out.println("The two objects are equal" + '\n' +
                    "Hashcode of test1: " + test1.hashCode() + '\n' +
                    "Hashcode of test2: " + test3.hashCode());
        }
        else {
            System.out.println("The two objects are not equal" + '\n' +
                    "Hashcode of test1: " + test1.hashCode() + '\n' +
                    "Hashcode of test2: " + test3.hashCode());
        }

        //toString test
        System.out.println(test1);
    }
}
