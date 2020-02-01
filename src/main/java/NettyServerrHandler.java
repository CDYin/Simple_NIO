import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

public class NettyServerrHandler extends ChannelInboundHandlerAdapter {
    /*
        1.我们自定义一个Handler需要继承netty规定好的HandlerAdapter（规范）
        2.这时我们自定义一个Handler，才能成为一个Handler
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);
        System.out.println("服务器读取线程: " + Thread.currentThread().getName());

        System.out.println("看看channel和pipeline的关系");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();//本质是一个双向链表，出栈入栈
        //pipeline和channel互相包含，ctx包含pipeline和channel


        //将msg转成一个ByteBuf
        //ByteBuf是Netty提供的，不是NIO的ByteBuffer
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送的消息 ：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址 :" + ctx.channel().remoteAddress());
    }
    //数据读取完毕

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello",CharsetUtil.UTF_8));
    }

    //处理异常

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
