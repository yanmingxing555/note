package com.fomp.note.bioNioAioNetty.netty.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
 
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client is active");
    }
 
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            UserData user = (UserData)msg;
            System.out.println("服务器返回的消息  : " + user.getId() + ", " + user.getName() + ", " + user.getResponseMessage());            
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
 
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }
 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
        System.out.println("Client is close");
    }
}