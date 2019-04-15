package servlet;

import core.HttpRequest;
import core.HttpResponse;

import java.io.File;

/**
 * @author: dreamMaker
 **/
public class PreLoginServlet implements IHttpServlet {
    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        response.setFile(new File("web/login.html"));
    }

}
