package database;

import org.apache.commons.dbcp.BasicDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接池
 * @author: dreamMaker
 **/
public class DatabaseConnectionPool {
    private static Properties properties = new Properties();
    private static BasicDataSource dataSource = new BasicDataSource();
    static {
        readProperities();
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setDriverClassName(properties.getProperty("driver"));
        dataSource.setUsername(properties.getProperty("user"));
        dataSource.setPassword(properties.getProperty("password"));
        dataSource.setInitialSize(Integer.parseInt(properties.getProperty("initialSize")));
        dataSource.setMaxActive(Integer.parseInt(properties.getProperty("maxActive")));
    }

    /**
     * 读取配置文件
     */
    private static void readProperities(){
        try (InputStream in = DatabaseConnectionPool.class.getClassLoader().getResourceAsStream("database.properties")) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
    public static void main(String[] args) {
        /*String sql = "insert into user(id,username,password) values(null,?,?)";
        try (Connection connection = DatabaseConnectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,"jack");
            statement.setString(2,"123456");
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        String sql ="select * from place;";
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet set = statement.executeQuery();
            while (set.next()){
                System.out.println(set.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
