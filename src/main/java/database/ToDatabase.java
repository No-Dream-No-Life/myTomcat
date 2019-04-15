package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作数据库的超类
 * @author: dreamMaker
 **/
public class ToDatabase {
    /**
     * 无返回集（增、删、改）
     * @param sql
     * @param paramterList
     */
    public int executeUpdate(String sql, List<String> paramterList){
        int result = 0;
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1;i <= paramterList.size();i++) {
                statement.setString(i,paramterList.get(i-1));
            }
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询
     * @param sql
     * @param paramterList
     * @return List
     */
    public List<Map<String,String>> query(String sql,List<String> paramterList){
        List<Map<String,String>> resultList = null;
        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1;i <= paramterList.size();i++) {
                statement.setString(i,paramterList.get(i-1));
            }
            ResultSet resultSet = statement.executeQuery();
            resultList = new ArrayList<>();
            Map<String,String> resultMap = new HashMap<>();
            while(resultSet.next()){
                //获取列集
                ResultSetMetaData metaData = resultSet.getMetaData();
                //列的总数
                int count = metaData.getColumnCount();
                for (int i=1;i <= count;i++) {
                    //遍历列名
                    String key = metaData.getColumnName(i);
                    resultMap.put(key,resultSet.getString(key));
                }
                resultList.add(resultMap);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultList;
    }

}
