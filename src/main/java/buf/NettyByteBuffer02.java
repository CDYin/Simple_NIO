package buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyByteBuffer02 {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello World! 也", Charset.forName("UTF-8"));

        //使用相关的方法
        if (byteBuf.hasArray()){//true

            byte[] array = byteBuf.array();

            //将array转成字符串
            System.out.println(new String(array, CharsetUtil.UTF_8));
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());
            System.out.println(byteBuf.getCharSequence(0,4,CharsetUtil.UTF_8));
        }
    }
}
