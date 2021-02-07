package networkprogramming.demo.network;

import com.sun.security.ntlm.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: duyubo
 * @date: 2020年12月23日, 0023 10:40
 * @description:
 */
public class SocketServerNIO {
    public static void main(String[] args) throws IOException, InterruptedException {
        List<SocketChannel> clients = new LinkedList<>();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(7000));
        ssc.configureBlocking(false);

//        ssc.setOption(StandardSocketOptions.TCP_NODELAY, false);
//        StandardSocketOptions.TCP_NODELAY;
//        StandardSocketOptions.SO_KEEPALIVE;
//        StandardSocketOptions.SO_LINGER;
//        StandardSocketOptions.SO_RCVBUF;
//        StandardSocketOptions.SO_SNDBUF;
//        StandardSocketOptions.SO_REUSEADDR;

        while (true) {
            Thread.sleep(1000);
            SocketChannel client = ssc.accept();

            if (client == null) {
                System.out.println("null..........");
            } else {
                client.configureBlocking(false);
                int port = client.socket().getPort();
                System.out.println("client......port：" + port);
                clients.add(client);
            }
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4096);

            for (SocketChannel sc : clients) {
                int num = sc.read(byteBuffer);
                if (num > 0) {
                    byteBuffer.flip();
                    byte[] data = new byte[byteBuffer.limit()];
                    byteBuffer.get(data);

                    String message = new String(data);
                    System.out.println(sc.socket().getPort() + " ：" + message);
                    byteBuffer.clear();
                }
            }
        }


    }
}
