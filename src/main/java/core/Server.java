package core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: dreamMaker
 **/
public class Server {
    /**
     * 服务端serverSocket;
     */
    private ServerSocket serverSocket;

    /**
     * 线程池
     */
    private ExecutorService threadPool;

    public Server(){
        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("服务器启动成功。");
            //创建一个可重用固定线程数的线程池
            threadPool = Executors.newFixedThreadPool(4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动服务
     */
    public void start(){
            try {
                while(!serverSocket.isClosed()){
                    System.out.println("等待客户端连接。。。");
                    Socket socket = serverSocket.accept();
                    System.out.println("一个客户端连接了。");
                    ClientHandler handler = new ClientHandler(socket);
                    threadPool.execute(handler);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * 程序入口
     * @param args
     */
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
