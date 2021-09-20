import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client implements Runnable {
    public final int PORT;
    public final String HOST;

    public Client(int PORT, String HOST) {
        this.PORT = PORT;
        this.HOST = HOST;
    }

    @Override
    public void run() {
        InetSocketAddress isa = new InetSocketAddress(HOST, PORT);
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.connect(isa);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Scanner scanner = new Scanner(System.in)) {
            ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
            String msg;
            while (true) {
                System.out.println("Строка для удаления пробелов (для выхода введите - end): ");
                msg = scanner.nextLine();
                if (msg.equals("end")) {
                    sc.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                    break;
                }
                sc.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                int bytesCount = sc.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
