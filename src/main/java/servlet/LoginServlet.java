package servlet;

import core.HttpRequest;
import core.HttpResponse;
import database.ToDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: dreamMaker
 **/
public class LoginServlet extends ToDatabase implements IHttpServlet {

    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        String username = request.getRequestParamters().get("username");
        String password = request.getRequestParamters().get("password");
        boolean result = checkLogin(username,password);
        if (result) {
            System.out.println("验证通过");
            response.setFile(new File("web/login_success.html"));
        } else {
            System.out.println("用户名或密码错误");
            response.setFile(new File("web/login_failed.html"));
        }
    }

    /**
     * 工具类
     * 检查用户名和密码
     * @param username
     * @return
     */
    private boolean checkLogin(String username,String password) {
        boolean result = false;
        String sql = "select count(*) count from user where username=? and password=?";
        List<String> list = new ArrayList<>();
        list.add(username);
        list.add(password);
        List<Map<String, String>> resultList = query(sql, list);
        if (resultList != null && resultList.size() > 0) {
            int count = Integer.parseInt(resultList.get(0).get("count"));
            if (count > 0) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }
}



