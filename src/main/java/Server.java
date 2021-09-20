import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable {
    private final int PORT;
    private final String HOST;

    public Server(int PORT, String HOST) {
        this.PORT = PORT;
        this.HOST = HOST;
    }

    @Override
    public void run() {
        ServerSocketChannel ssc = null;
        try {
            ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress(HOST, PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Сервер запущен...");

        while (true) {
            try (SocketChannel sc = ssc.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                String outputText;
                while (sc.isConnected()) {
                    int byteCount = sc.read(inputBuffer);
                    if (byteCount == -1) break;
                    final String msg = new String(inputBuffer.array(), 0, byteCount, StandardCharsets.UTF_8);
                    if (msg.equals("end")) break;
                    outputText = new RemoveSpaces().remove(msg);
                    inputBuffer.clear();
                    sc.write(ByteBuffer.wrap(("Результат удаления пробелов:\n" + outputText).getBytes(StandardCharsets.UTF_8)));
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
