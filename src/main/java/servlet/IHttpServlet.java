package servlet;


import core.HttpRequest;
import core.HttpResponse;

public interface IHttpServlet{
    /**
     * 处理逻辑
     * @param request
     * @param response
     */
    void execute(HttpRequest request, HttpResponse response);

}

