package core;

import config.HttpsConfiguration;
import servlet.IHttpServlet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * 处理客户端请求的处理类
 * @author: dreamMaker
 **/
public class ClientHandler implements Runnable {
    /**
     * 通信socket
     */
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    /**
     * 处理流程
     */
    @Override
    public void run() {
        HttpRequest request = new HttpRequest(socket);
        HttpResponse response = new HttpResponse(socket);
        setResponseContent(request,response);
        response.send();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 设置响应信息
     */
    private void setResponseContent(HttpRequest request, HttpResponse response){
        String url = request.getUrl();
        System.out.println("url:"+url);
        //处理业务
        if(HttpsConfiguration.containsURL(url)){
            System.out.println("处理请求");
            try {
                Class classObject = Class.forName(HttpsConfiguration.getClassName(url));
                IHttpServlet servlet = (IHttpServlet) classObject.newInstance();
                servlet.execute(request,response);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            response.setStatusCode("200");
            response.setStatusDescription("SUCCESS");
        }else{
            System.out.println("暂不能处理该请求");
            response.setFile(new File("web/404.html"));
            response.setStatusCode("404");
            response.setStatusDescription("NOT FOUND!");
        }
    }
}
