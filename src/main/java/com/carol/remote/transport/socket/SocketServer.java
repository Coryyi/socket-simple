package com.carol.remote.transport.socket;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 服务端
 */
public class SocketServer {
    // 日志
    private final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    // 线程池 每个请求创建一个线程 限制最大数
    private final ExecutorService threadPool;


    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.start(8888);
    }
    public SocketServer(){
        // 初始化线程池
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(10,100,1,TimeUnit.MICROSECONDS, new ArrayBlockingQueue<>(100), threadFactory);

    }

    public void start(int port){
        // 1.创建ServerSocket对象并绑定端口

        // 资源最终需要释放使用try-with-resource
        try(ServerSocket serverSocket = new ServerSocket(port);){
            Socket socket;

            // 2.通过accept方法监听客户端请求
            /**
             * 这里调用的accept()方法时阻塞方法
             * 也就是说 ServerSocket 在调用 accept(0 等待客户端的连接请求时会阻塞
             * 直到收到客户端发送的连接请求才会继续往下执行代码
             * 因此代码有一个严重的问题，只能同时处理一个客户端的连接，如果需要管理多个客户端的话，就需要为我们请求的客户端线独创建一个线程
             */
            while ((socket = serverSocket.accept())!=null){
                logger.error("连接成功");
                // 引入线程池处理
                threadPool.execute(new RequestHandlerRunnable(socket));
                // 3.创建输入输出流
                /*try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());){
                    // 4.通过输入流向读取客户端发送的信息
                    MyMessage message = (MyMessage) objectInputStream.readObject();
                    logger.error("Server receive message:"+message.getContent());

                    // 5.通过输出流向客户端发送消息
                    message.setContent("new message");
                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();

                } catch (ClassNotFoundException e) {
                    logger.error("接收&发送消息异常",e);
                    throw new RuntimeException(e);
                }*/
            }
        }catch (IOException e){
            logger.error("创建ServerSocket连接异常",e);
        }
    }
}
