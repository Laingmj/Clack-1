public class TestClackData {
    public static void main(String[] args) {
        ClackData file = new FileClackData();
        ClackData message = new MessageClackData();

        System.out.println(file.toString());
        System.out.println(message.toString());
        System.out.println(file.hashCode());
        System.out.println(message.hashCode());
        System.out.println(file.equals(message));
        System.out.println(file.getData());
        System.out.println(message.getData());
    }
}
