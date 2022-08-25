package com.fomp.note.bioNioAioNetty.netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
 
public class NettyServer {
 
    public static void main(String[] args) throws Exception{
        
        EventLoopGroup bossGroup = new NioEventLoopGroup(); //线程组：用来处理网络事件处理（接受客户端连接）
        EventLoopGroup workGroup = new NioEventLoopGroup(); //线程组：用来进行网络通讯读写
        
        //Bootstrap用来配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class) //注册服务端channel
         /**
          * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
          * 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，将使用默认值50。
          * 服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，多个客户端来的时候，
          * 服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
          */
         .option(ChannelOption.SO_BACKLOG, 1024)
         //设置日志
         .handler(new LoggingHandler(LogLevel.INFO))
         .childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel sc) throws Exception {
                //marshalling的编解码操作,要传输对象，必须编解码
                sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                //5s没有交互，就会关闭channel
                sc.pipeline().addLast(new ReadTimeoutHandler(60));
                sc.pipeline().addLast(new NettyServerHandler());   //服务端业务处理类
            }
        });
        ChannelFuture cf = bootstrap.bind(8765).sync();
        
        cf.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}