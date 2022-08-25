package com.fomp.note.bioNioAioNetty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * AIO(NIO 2.0)
 * 异步非阻塞，由操作系统完成后回调通知服务端程序启动线程去处理，一般适用于连接数较多且连接时间较长的应用
 * 应用场景： AIO方式适用于连接数目多且连接比较长（重操作）的架构，JDK7 开始支持
 */
public class AioTest {
    public static void main(String[] args) {

    }
}
//服务端
class AIOServer {
    public void server() throws Exception {
        //创建异步通道
        final AsynchronousServerSocketChannel serverChannel=AsynchronousServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(9000));
        System.out.println("等待连接中");
        //在AIO中，accept有两个参数，
        //第一个参数是一个泛型，可以用来控制想传递的对象
        //第二个参数CompletionHandler，用来处理监听成功和失败的逻辑
        //如此设置监听的原因是因为这里的监听是一个类似于递归的操作，每次监听成功后要开启下一个监听
        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            //请求成功处理逻辑
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {
                try {
                    System.out.println("连接成功，处理数据中");
                    //再此接收客户端连接，如果不写这行代码后面的客户端连接连不上服务端
                    //开启新的监听
                    serverChannel.accept(attachment, this);
                    System.out.println(socketChannel.getRemoteAddress());
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    //通道的read方法也带有三个参数
                    //1.目的地：处理客户端传递数据的中转缓存，可以不使用
                    //2.处理客户端传递数据的对象
                    //3.处理逻辑，也有成功和不成功的两个写法
                    socketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {
                            /*if (result>0){
                                buffer.flip();
                                byte[] array = buffer.array();
                                System.out.println(new String(array));
                            }*/
                            buffer.flip();
                            System.out.println(new String(buffer.array(), 0, result));
                            socketChannel.write(ByteBuffer.wrap("HelloClient".getBytes()));
                        }
                        @Override
                        public void failed(Throwable exc, ByteBuffer buffer) {
                            exc.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
                System.out.println("失败");
            }
     });
     Thread.sleep(Integer.MAX_VALUE);
    }
}
//客户端
class AIOClient {
    public void client() throws Exception {
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9000)).get();
        socketChannel.write(ByteBuffer.wrap("HelloServer".getBytes()));
        ByteBuffer buffer = ByteBuffer.allocate(512);
        Integer len = socketChannel.read(buffer).get();
        if (len != -1) {
            System.out.println("客户端收到信息：" + new String(buffer.array(), 0, len));
        }
    }
}