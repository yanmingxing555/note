package com.fomp.note.bioNioAioNetty.netty.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
 
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Server is Active");
    }
 
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接受客户端对象
        UserParam user = (UserParam)msg;
        System.out.println("客户端发来的消息 : " + user.getId() + ", " + user.getName() + ", " + user.getRequestMessage());
        //给客户端返回对象
        UserData response = new UserData();
        response.setId(user.getId());
        response.setName("response" + user.getId());
        response.setResponseMessage("响应内容" + user.getId());
        ctx.writeAndFlush(response);
        //处理完毕，关闭服务端
        //ctx.addListener(ChannelFutureListener.CLOSE);
    }
 
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }
 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}