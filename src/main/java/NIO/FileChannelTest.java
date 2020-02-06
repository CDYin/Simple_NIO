package NIO;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {

    public static void main(String[] args) throws IOException {
        String str = "hello";
        //  创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\11578\\IdeaProjects\\TrayTest\\src\\main\\java\\NIO\\sss");
         //通过fileOutputStream获取对应的FileChannel
        //这个filechannel真是类型是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
         //创建一个缓冲区ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str放入ByteBuffer
        byteBuffer.put(str.getBytes());
        //读写翻转
        byteBuffer.flip();
        //将bytebuffer数据写入到FileChannel
        fileChannel.write(byteBuffer);

        fileOutputStream.close();
    }
}
