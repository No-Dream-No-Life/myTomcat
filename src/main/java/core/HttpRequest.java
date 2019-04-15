package core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dreamMaker
 **/
public class HttpRequest {
    /**
     * 输入流
     */
    private InputStream in;

    /**
     * 请求类型(GET/POST/...)
     */
    private String requestType;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求协议
     */
    private String protocal;

    /**
     * 请求头
     */
    private Map<String,String> requestHeadMap = new HashMap<>();

    /**
     * 请求数据
     */
    private Map<String,String> requestParamters = new HashMap<>();

    public String getUrl() {
        return url;
    }

    public Map<String, String> getRequestParamters() {
        return requestParamters;
    }

    public HttpRequest(){

    }
    /**
     * 构造方法
     * @param socket
     */
    public HttpRequest(Socket socket) {
        try {
            in = socket.getInputStream();
            parseRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析请求
     */
    private void parseRequest(){
        try{
            parseRequestLine();
            parseRequestHead();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     *解析请求行
     */
    private void parseRequestLine() throws Exception{
        String[] requestLineArray = readLine().split("\\s");
        if(requestLineArray.length == 3){
            this.requestType = requestLineArray[0];
            String[] urlAndParamters = requestLineArray[1].split("\\?");
            //如果有参数，需要解析
            if(urlAndParamters != null&&urlAndParamters.length > 1){
                parseStringToParamters(urlAndParamters[1]);
            }
            this.url = urlAndParamters[0];
            this.protocal = requestLineArray[2];
        }else{
            throw new Exception("空请求");
        }
    }

    /**
     * 解析请求头
     */
    private void parseRequestHead(){
        String line;
        String[] entry;
        while(!(line = readLine()).equals("")){
            entry = line.split(":");
            if (entry != null&&entry.length == 2) {
                requestHeadMap.put(entry[0].trim(),entry[1].trim());
            }
        }
        //如果消息头中含有ceontent-type和content-length,则有请求正文
        if(requestHeadMap.containsKey("Content-Length")&&requestHeadMap.containsKey("Content-Type")){
            parseRequestContent();
        }
    }

    /**
     * 解析请求正文
     */
    private void parseRequestContent(){
        int length = Integer.parseInt(requestHeadMap.get("Content-Length"));
        String dataType = requestHeadMap.get("Content-Type");
        if("application/x-www-form-urlencoded".equals(dataType)){
            byte[] datas = new byte[length];
            try {
                in.read(datas);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String paramterString = null;
            try {
                paramterString = new String(datas,"ISO8859-1");
                URLDecoder.decode(paramterString,"UTF-8");
                parseStringToParamters(paramterString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 工具方法
     * 从url或者消息正文中解析请求参数
     * @param paramterString
     */
    private void parseStringToParamters(String paramterString){
        String[] paramters = paramterString.split("&");
        for(String s:paramters){
            String[] entry = s.split("=");
            if(entry != null&&entry.length == 2){
                requestParamters.put(entry[0],entry[1]);
            }
        }
        System.out.println("请求参数："+requestParamters.toString());
    }

    /**
     * 工具方法
     * 从输入流中读取一行
     * @return
     */
    private String readLine() {
        try {
            /*
             * 顺序从in中读取每个字符，当连续读到了CR，LF时停止，并将之前读取到的字符以一个字符串形式返回即可。
             */
            StringBuilder builder=new StringBuilder();
            int d=-1;
            char a='a',b='b';
            while((d=in.read())!=-1) {
                b=(char)d;
                if(a==13&&b==10) {
                    break;
                }
                builder.append(b);
                a=b;
            }
            return builder.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    class d extends HttpRequest {
    }
}
