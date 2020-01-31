import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyTestServer {
    public static void main(String[] args) {
        /*
        bossgroup只是处理请求，真正和客户端业务处理，会交给workgroup完成
        两个都是无限循环
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap Bootstrap = new ServerBootstrap();

        Bootstrap.group(bossGroup,workerGroup)//设置两个线程组
                .channel(NioServerSocketChannel.class)//使用NioSocketChannel作为服务器通道实现
                .option(ChannelOption.SO_BACKLOG,128)//设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                .childHandler(new HelloServevrInitializer());//给我们的workergroupde1Eventloop对应的管道设置处理器
        try {
            //绑定一个端口并且同步，生成了一个ChannelFuture对象
            //启动服务器（并绑定端口）
            ChannelFuture channelFuture = Bootstrap.bind(8088).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
