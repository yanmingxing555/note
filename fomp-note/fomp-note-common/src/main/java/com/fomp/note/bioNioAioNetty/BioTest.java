package com.fomp.note.bioNioAioNetty;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * IO模型就是说用什么样的通道进行数据的发送和接收，Java共支持3种网络编程IO模式：BIO，NIO，AIO
 * BIO(Blocking IO) 同步阻塞模型，一个客户端连接对应一个处理线程
 * 缺点：
 *      1、IO代码里read操作是阻塞操作，如果连接不做数据读写操作会导致线程阻塞，浪费资源
 *      2、如果线程很多，会导致服务器线程太多，压力太大。
 * 应用场景： BIO方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，但程序简单易理解
 */
public class BioTest {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SocketServer server = new SocketServer();
                    server.server();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            SocketClient client = new SocketClient();
            client.client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//服务端示例
class SocketServer {
    public void server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true) {
            System.out.println("等待连接。。");
            Socket socket = serverSocket.accept(); //阻塞方法
            System.out.println("有客户端连接了。。");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        handler(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    private void handler(Socket socket) throws IOException {
        System.out.println("thread id = " + Thread.currentThread().getId());
        byte[] bytes = new byte[1024];
        System.out.println("准备read。。");
        //接收客户端的数据，阻塞方法，没有数据可读时就阻塞
        int read = socket.getInputStream().read(bytes);
        System.out.println("read完毕。。");
        if (read != -1) {
            System.out.println("接收到客户端的数据：" + new String(bytes, 0, read));
            System.out.println("thread id = " + Thread.currentThread().getId());
        }
        socket.getOutputStream().write("HelloClient".getBytes());
        socket.getOutputStream().flush();
    }
}
//客户端代码
class SocketClient {
    public void client() throws IOException {
        Socket socket = new Socket("localhost", 9000);
        //向服务端发送数据
        socket.getOutputStream().write("HelloServer".getBytes());
        socket.getOutputStream().flush();
        System.out.println("向服务端发送数据结束");
        byte[] bytes = new byte[1024];
        //接收服务端回传的数据
        socket.getInputStream().read(bytes);
        System.out.println("接收到服务端的数据：" + new String(bytes));
        socket.close();
    }
}