package core;

import config.HttpsConfiguration;

import java.io.*;
import java.net.Socket;

/**
 * @author: dreamMaker
 **/
public class HttpResponse {
    /**
     * socket中的输出流
     */
    private OutputStream out;
    /**
     * 响应状态码
     */
    private String statusCode;
    /**
     * 响应状态描述
     */
    private String statusDescription;
    /**
     * 响应正文类型
     */
    private String contentType;
    /**
     * 响应正文长度
     */
    private String contentLength;
    /**
     * 响应文件
     */
    private File file;
    /**
     * 返回字符串
     */
    private Object object;

    public void setObject(Object object) {
        this.object = object;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    public void setFile(File file) {
        this.file = file;
        setContentLength(String.valueOf(file.length()));
        setContentType(HttpsConfiguration.getContentType(file.getName().split("\\.")[1]));
    }

    public HttpResponse(Socket socket){
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送响应信息
     */
    public void send(){
        sendResponseLine();
        if(file != null&&file.exists()){
            sendResponseHead();
            sendResponseContent();
        }
    }

    /**
     * 发送响应状态行
     */
    private void sendResponseLine(){
        String responseLine = "HTTP/1.1"+" "+statusCode+" "+statusDescription;
        sendStringLine(responseLine);
    }

    /**
     * 发送响应头
     */
    private void sendResponseHead(){
        sendStringLine("Content-Type:"+contentType);
        sendStringLine("Content-Length:"+contentLength);
        sendStringLine("");
    }

    /**
     * 发送响应正文
     */
    private void sendResponseContent(){
        try (FileInputStream in = new FileInputStream(file)) {
            byte[] bytes = new byte[1024*10];
            int length = -1;
            while ((length=in.read(bytes)) != -1) {
                out.write(bytes,0,length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 工具方法
     * 通过输出流发送一行字符串
     * @param line
     */
    private void sendStringLine(String line){
        try {
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
