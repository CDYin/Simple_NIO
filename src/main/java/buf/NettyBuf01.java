package buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyBuf01 {

    //创建一个ByteBuf
    //说明
    //1、创建对象，该对象包含一个数组arr，是一个byte[]
    //2、在netty的buffer中，不需要使用flip进行反转
    //底层维护了readerIndex和writerIndex
    //3、通过readerindex和writerindex和capacity，将buffer分成3个区域
    //0---readerindex 已读取的区域
    //readerindex---writerindex 可读取的区域
    //writerindex---capacity 可写区域
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer(10);

        for (int i =0;i<10;i++){
            byteBuf.writeByte(i);
        }

        //输出
        //for(int i=0;i<buffer.capacity;i++){
        //      System.Out>println(byteBuf.getByte(i));
        // }
        System.out.println("capacity = " + byteBuf.capacity());
        for (int j = 0;j<byteBuf.capacity();j++){
            System.out.println(byteBuf.readByte());
        }
        System.out.println("执行完毕");
    }



}
