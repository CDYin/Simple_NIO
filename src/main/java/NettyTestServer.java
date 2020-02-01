import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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
