package com.fomp.note.bioNioAioNetty.netty.demo;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;
 
public class NettyClient {
    
    private static class SingletonHolder {
        static final NettyClient instance = new NettyClient();
    }
    
    public static NettyClient getInstance(){
        return SingletonHolder.instance;
    }
    
    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private ChannelFuture channelFuture ;
    
    private NettyClient(){
            group = new NioEventLoopGroup();
            bootstrap = new Bootstrap();

            bootstrap.group(group)
             .channel(NioSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                        socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        //超时handler（当服务器端与客户端在指定时间以上没有任何进行通信，则会关闭响应的通道，主要为减小服务端资源占用）
                        socketChannel.pipeline().addLast(new ReadTimeoutHandler(60));
                        socketChannel.pipeline().addLast(new NettyClientHandler());  //客户端业务处理类
                    }
            });
    }
    
    public void connect(){
        try {
            this.channelFuture = bootstrap.connect("127.0.0.1", 8765).sync();
            System.out.println("远程服务器已经连接, 可以进行数据交换..");                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ChannelFuture getChannelFuture(){
        //如果管道没有被开启或者被关闭了，那么重连
        if(this.channelFuture == null){
            this.connect();
        }
        if(!this.channelFuture.channel().isActive()){
            this.connect();
        }
        return this.channelFuture;
    }
    
    public static void main(String[] args) throws Exception{
        final NettyClient client = NettyClient.getInstance();
        
        ChannelFuture channelFuture = client.getChannelFuture();
        for(int i = 1; i <= 3; i++ ){
            //客户端发送的数据
            UserParam request = new UserParam();
            request.setId("" + i);
            request.setName("pro" + i);
            request.setRequestMessage("数据信息" + i);

            channelFuture.channel().writeAndFlush(request);
            System.out.println("主线程发送完成："+i+"==>"+ JSONObject.toJSONString(request));
            TimeUnit.SECONDS.sleep(1);
        }
        //当5s没有交互，就会异步关闭channel
        channelFuture.channel().closeFuture().sync();
        
        //再模拟一次传输
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ChannelFuture cf = client.getChannelFuture();
                    //System.out.println(cf.channel().isActive());
                    //System.out.println(cf.channel().isOpen());
                    
                    //再次发送数据
                    UserParam request = new UserParam();
                    request.setId("" + 4);
                    request.setName("pro" + 4);
                    request.setRequestMessage("数据信息" + 4);
                    
                    cf.channel().writeAndFlush(request);
                    cf.channel().closeFuture().sync();
                    System.out.println("子线程结束：4==>"+ JSONObject.toJSONString(request));
                    System.out.println("子线程结束.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        System.out.println("断开连接,主线程结束..");
    }
}