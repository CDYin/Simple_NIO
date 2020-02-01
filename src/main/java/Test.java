import io.netty.util.NettyRuntime;

public class Test {
    public static void main(String[] args) {
        //cpu核数
        System.out.println(NettyRuntime.availableProcessors());
    }
}
