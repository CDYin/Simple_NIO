package Test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class NettyTestClient {
    public static void main(String[] args) {
        //客户端需要一个时间循环组
        EventLoopGroup group = new NioEventLoopGroup();

        //创建客户端启动对象
        //客户端是BootStrap，服务器是ServerBootStrap
        Bootstrap b = new Bootstrap();

        //设置相关参数
        b.group(group)//设置线程组
                .channel(NioSocketChannel.class)//设置客户端通道的实现类（反射）
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new HttpObjectAggregator(8088));
                            socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        System.out.println("客户端准备好了");
        System.out.println("启动客户端去连接服务器");
        try {
            ChannelFuture channelFuture = b.connect("127.0.0.1",8088).sync();
            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
