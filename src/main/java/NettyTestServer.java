import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyTestServer {
    public static void main(String[] args) {
        /*
        bossgroup只是处理请求，真正和客户端业务处理，会交给workgroup完成
        两个都是无限循环
        bossgroup和workergroup含有的子线程（nioeventloop）的个数默认是cpu核数的个数

        channel中流添加并通数据
        pipeline中添加Handler
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap Bootstrap = new ServerBootstrap();

        Bootstrap.group(bossGroup,workerGroup)//设置两个线程组
                .channel(NioServerSocketChannel.class)//使用NioSocketChannel作为服务器通道实现
                .option(ChannelOption.SO_BACKLOG,128)//设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个匿名通道测试对象
                    //给pipline设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyServerrHandler());

                        //加入一个netty提供的IdleStateHandler
                        /*
                        1、IdleStateHandler是netty提供的处理空闲状态的处理器
                        2、Long readerIdleTime：表示多长时间没有读，就会发送一个心跳检测包检测是否继续连接
                        3、Long writeIdleTime：表示多长时间没有写，就会发送一个心跳检测包检测是否继续连接
                        4、Long allIdleTime：表示多长时间没有读写，就会发送一个心跳检测包检测是否继续连接
                        5、文档说明
                        trigger an{@Link IdleStateEvent} when a {@Link channel} has not performd
                        read,write,or both operation for a while
                        6、当IdleStateEvent触发后，就会传递给管道的下一个Handler去处理
                        通过调用（触发）下一个handler的userEventTrigger
                         */
                        socketChannel.pipeline().addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                        socketChannel.pipeline().addLast(null);

                    }
                });//给我们的workergroupde1Eventloop对应的管道设置处理器

        System.out.println("服务器OK");
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
