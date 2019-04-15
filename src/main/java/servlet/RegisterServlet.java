package servlet;

import core.HttpRequest;
import core.HttpResponse;
import database.DatabaseConnectionPool;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author: dreamMaker
 **/
public class RegisterServlet implements IHttpServlet{
    private boolean result = false;
    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        System.out.println("处理注册业务");
        String sql = "insert into user (username,password) value (?,?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,request.getRequestParamters().get("username"));
            statement.setString(2,request.getRequestParamters().get("password"));
            int sum = statement.executeUpdate();
            if (sum>0) {
                System.out.println("数据插入成功");
                response.setFile(new File("web/register_success.html"));
            }else{
                System.out.println("数据插入失败");
                response.setFile(new File("web/register_fail.html"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
