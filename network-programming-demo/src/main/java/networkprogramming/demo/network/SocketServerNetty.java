package networkprogramming.demo.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: duyubo
 * @date: 2020年12月23日, 0023 14:01
 * @description:
 */
public class SocketServerNetty {
    private ServerSocketChannel ssc = null;
    private Selector selector = null;

    public void initServer() {
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(7000));

            //epoll模型下, open --> epoll_create --> fd3
            selector = Selector.open();

            /**
             * ssc 约等于listen状态下的 fd4
             * select、poll模型下：jvm开辟一个数组，将fd4放进去
             * epoll： epoll_ctl(fd3,ADD,fd4,EPOLLIN)
             */
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start() {
        initServer();
        System.out.println("服务器启动了~~~~~");
        try {
            while (true) {
                Set<SelectionKey> keys = selector.keys();
                System.out.println(keys.size() + "    size");

                while (selector.select(500) > 0) {
                    //内核调用select、poll、epoll等方法返回有状态的fd集合
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
//                            readHandler(key);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptHandler(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel client = ssc.accept();
            client.configureBlocking(false);

            ByteBuffer byteBuffer = ByteBuffer.allocate(7000);

            client.register(selector, SelectionKey.OP_READ, byteBuffer);
            System.out.println("-------------------------------");
            System.out.println("新客户端：" + client.getRemoteAddress());
            System.out.println("-------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
