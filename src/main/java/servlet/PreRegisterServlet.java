package servlet;


import config.HttpsConfiguration;
import core.HttpRequest;
import core.HttpResponse;

import java.io.File;

/**
 * @author: dreamMaker
 **/
public class PreRegisterServlet implements IHttpServlet {

    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        response.setFile(new File("web/register.html"));
    }

}
