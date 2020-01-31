package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {

        //创建ServerSocketChannel -》 ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定一个端口6666，在服务器监听
        serverSocketChannel.bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把ServerSocketChannel注册到Selector关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("注册后的selectionkey 数量 = " + selector.keys().size());
        //循环等待客户端连接
        while (true){

            //这里我们循环等待1秒；如果没有时间发生，返回
            if (selector.select(1000) == 0){//没有事件发生
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }

            /*
            如果返回的>0，就获取到相关的selectionkey集合
            1，如果返回的>0，表示已经获取到关注的事件
            2，selector.selectedkeys（）返回关注事件的集合
            通过selectionkeys反向获取通道
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionkeys数量(触发事件的) = " + selectionKeys.size());

            //遍历set<Selevtionkey>,使用迭代器遍历
            Iterator<SelectionKey> keyiterator = selectionKeys.iterator();

            while (keyiterator.hasNext()){
                //获取到Selectionkey
                SelectionKey key = keyiterator.next();
                //根据ket对应的通道发生的事件做相应处理
                if (key.isAcceptable()){//如富欧式OP_ACCEPT，有新的客户端连接
                    //给该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功 生成了一个socketchannel：" + socketChannel.hashCode());
                    //将socketchannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketchannel注册到selector关注时间为OP_READ
                    // 同时给socketcahnnel关联一个Buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    System.out.println("客户端连接后，注册的selectionkey的数量(所有) = " + selector.keys().size());
                }

                if (key.isReadable()){//发生OP_READ
                    //通过key反向获取到对应的channel
                    SocketChannel channel =(SocketChannel)key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端 " + new String(buffer.array()));
                }
                //手动从集合中移动当前的selectionkey，防止重复操作
                keyiterator.remove();
            }

        }
    }
}
