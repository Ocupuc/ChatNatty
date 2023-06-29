package ru.ocupuc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;


public class MainHandler extends SimpleChannelInboundHandler<String> {  // Inbound обрабатываем вход

    private static final List<Channel> channels = new ArrayList<Channel>();
    private static int newClientIndex;
    private String clientName;

    //ctrl+O выбираем методы для переопределения
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился " + ctx);
        channels.add(ctx.channel());
        clientName = "Клиент #" + newClientIndex;
        newClientIndex++;
    }

    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        System.out.println("Получено сообщение " + s);
        String out = String.format("[%s]: %s\n", clientName, s);
        for (Channel c : channels  ){
            c.writeAndFlush(out);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Клиент "+ clientName + " отвалился");
        channels.remove(ctx.channel());
        ctx.close(); // закрываем соединение в котором возникло исключение
    }
}
