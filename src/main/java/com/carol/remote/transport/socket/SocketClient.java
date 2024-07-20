package com.carol.remote.transport.socket;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 客户端
 */
public class SocketClient {
    private final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    public Object send(MyMessage message,String host,int port){
        // 1.创建Socket对象并指定服务器地址和端口号
        try(Socket socket = new Socket(host,port)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // 通过输出流向服务端发送请i去
            objectOutputStream.writeObject(message);
            // 3.从输入流获取服务器响应详细
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            return (MyMessage) objectInputStream.readObject();
        }catch (IOException|ClassNotFoundException e){
            logger.error("处理异常",e);
        }
        return null;
    }

    public static void main(String[] args) {
        SocketClient socketClient = new SocketClient();
        MyMessage message = (MyMessage) socketClient.send(new MyMessage("request客户端发送消息"),"127.0.0.1",8888);
        System.out.println(message);
    }
}
