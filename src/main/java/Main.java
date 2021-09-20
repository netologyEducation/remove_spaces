public class Main {
    public static void main(String[] args) {

        final int PORT = 1234;
        final String HOST = "127.0.0.1";

        Thread server = new Thread(new Server(PORT, HOST), "server");
        Thread client = new Thread(new Client(PORT, HOST),"client");

        server.start();
        client.start();
    }



}
