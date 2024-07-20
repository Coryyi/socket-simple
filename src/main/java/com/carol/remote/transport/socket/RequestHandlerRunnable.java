package com.carol.remote.transport.socket;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestHandlerRunnable implements Runnable{
    private final Logger logger = LoggerFactory.getLogger(RequestHandlerRunnable.class);
    //socket连接
    private final Socket socket;

    public RequestHandlerRunnable(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ) {
            MyMessage message = (MyMessage) objectInputStream.readObject();
            logger.error("收到客户端消息："+message);
            message.setContent("response服务端返回消息");
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("处理异常",e);
            throw new RuntimeException(e);
        }
    }
}
