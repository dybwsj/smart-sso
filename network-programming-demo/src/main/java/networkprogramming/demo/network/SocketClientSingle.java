package networkprogramming.demo.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * @author: duyubo
 * @date: 2020年12月22日, 0022 15:09
 * @description: Socket通信客户端
 */
public class SocketClientSingle {
    public static void main(String[] args) throws IOException {
        int port = 7000;
        String host = "localhost";

        Socket socket = new Socket(host, port);

        DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        Scanner sc = new Scanner(System.in);

        boolean flag = false;

        while (!flag) {
            System.out.println("请输入边长：");
            double length = sc.nextDouble();

            dos.writeDouble(length);
            dos.flush();

            double area = dis.readDouble();

            System.out.println("服务器返回面积：" + area);

            while (true) {
                System.out.println("继续计算？（Y/N）");

                String str = sc.next();

                if (str.equalsIgnoreCase("N")) {
                    dos.writeInt(0);
                    dos.flush();
                    flag = true;
                    break;
                } else if (str.equalsIgnoreCase("Y")) {
                    dos.writeInt(1);
                    dos.flush();
                    break;
                } else {
                    System.out.println("输入错误！");
                }
            }
        }
        socket.close();
    }
}
