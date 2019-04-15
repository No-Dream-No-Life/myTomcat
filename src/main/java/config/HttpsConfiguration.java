package config;

import core.HttpRequest;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取配置文件
 * @author: dreamMaker
 **/
public class HttpsConfiguration {
    /**
     * 将文件扩展名与Content-Type的映射关系放入集合中
     */
    private static Map<String,String> contentTypeMap = new HashMap<>();
    /**
     * 请求路径与处理类的映射关系
     */
    private static Map<String,String> handlerMapping = new HashMap<>();
    private static void initContentTypeMap(){
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read("config/web.xml");
            Element root = document.getRootElement();
            List<Element> elements = root.elements("mime-mapping");
            for(Element e:elements){
                String key = e.elementText("extension");
                String value = e.elementText("mime-type");
                contentTypeMap.put(key,value);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    private static void initHandlerMapping(){
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read("config/servlets.xml");
            Element root = document.getRootElement();
            List<Element> elements = root.elements("servlet");
            for(Element e:elements){
                String key = e.elementText("url");
                String value = e.elementText("className");
                handlerMapping.put(key,value);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    static{
        initContentTypeMap();
        initHandlerMapping();
    }

    /**
     * 根据文件后缀名获取文件类型
     * @param extensionName
     * @return
     */
    public static String getContentType(String extensionName){
        return contentTypeMap.get(extensionName);
    }

    /**
     * 是否含有预定义的url
     * @param url
     * @return
     */
    public static boolean containsURL(String url){
        return handlerMapping.containsKey(url);
    }

    /**
     * 根据Url获取相应类名
     * @param url
     * @return
     */
    public static String getClassName(String url){
        return handlerMapping.get(url);
    }
    private HttpRequest request;
    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(HttpsConfiguration.getContentType("html"));
        System.out.println(HttpsConfiguration.getClassName("/tomcat/toRegister.do"));
    }
}
