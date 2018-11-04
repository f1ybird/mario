package com.kevin.mario.db;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * describe  : sql2o ORM
 *
 * @author : kevin
 * @date : 2018/11/4 14:16
 **/
public final class MarioDb {

    private static Sql2o sql2o = null;

    private MarioDb() {

    }

    /**
     * 初始化sql2o对象
     *
     * @param url      连接url 如：jdbc:mysql://localhost:3306
     * @param userName
     * @param password
     */
    public static void init(String url, String userName, String password) {
        sql2o = new Sql2o(url, userName, password);
    }

    /**
     * 初始化数据库配置
     *
     * @param dataSource
     */
    public static void init(DataSource dataSource) {
        sql2o = new Sql2o(dataSource);
    }

    /**
     * 查询一个对象
     *
     * @param sql   执行sql
     * @param clazz 返回的类型
     * @param <T>   泛型
     * @return 查询的结果
     */
    public static <T> T get(String sql, Class<T> clazz) {
        return get(sql, clazz, null);
    }

    /**
     * 查询一个列表
     *
     * @param sql   执行sql
     * @param clazz 返回类型
     * @param <T>   泛型
     * @return 返回查询到的列表
     */
    public static <T> List<T> getList(String sql, Class<T> clazz) {
        return (List<T>) getList(sql, clazz, null);
    }

    /**
     * 返回一个map结果
     *
     * @param sql 执行sql
     * @return map形式的结果
     */
    public static Map<String, Object> getMap(String sql) {
        return getMap(sql, null);
    }

    /**
     * 返回一个列表list<map>类型
     *
     * @param sql 执行sql
     * @return
     */
    public static List<Map<String, Object>> getMapList(String sql) {
        return getMapList(sql, null);
    }

    /**
     * 插入一条记录
     *
     * @param sql    执行的sql
     * @param params 注入的参数
     * @return 插入的行数
     */
    public static int insert(String sql, Object... params) {
        StringBuffer sqlBuf = new StringBuffer(sql);
        sqlBuf.append(" values (");

        int start = sql.indexOf("(") + 1;
        int end = sql.indexOf(")");
        String a = sql.substring(start, end);
        String[] fields = a.split(",");

        Map<String, Object> map = new HashMap<>(params.length * 2);

        int i = 0;
        for (String name : fields) {
            sqlBuf.append(":" + name.trim() + " ,");
            map.put(name.trim(), params[i]);
            i++;
        }
        String newSql = sqlBuf.substring(0, sqlBuf.length() - 1) + ")";
        Connection con = sql2o.open();
        Query query = con.createQuery(newSql);
        executeQuery(query, map);
        int res = query.executeUpdate().getResult();
        con.close();
        return res;
    }

    /**
     * 更新记录
     *
     * @param sql 执行的sql
     * @return 更新的行数
     */
    public static int update(String sql) {
        return update(sql, null);
    }

    /**
     * 带参数更新
     *
     * @param sql
     * @param params
     * @return
     */
    public static int update(String sql, Map<String, Object> params) {
        Connection con = sql2o.open();
        Query query = con.createQuery(sql);
        executeQuery(query, params);
        int res = query.executeUpdate().getResult();
        con.close();
        return res;
    }

    public static List<Map<String, Object>> getMapList(String sql, Map<String, Object> params) {
        Connection con = sql2o.open();
        Query query = con.createQuery(sql);
        executeQuery(query, params);
        List<Map<String, Object>> t = query.executeAndFetchTable().asList();
        con.close();
        return t;
    }

    public static Map<String, Object> getMap(String sql, Map<String, Object> params) {
        Connection con = sql2o.open();
        Query query = con.createQuery(sql);
        executeQuery(query, params);
        Map<String, Object> t = (Map<String, Object>) query.executeScalar();
        con.close();
        return t;
    }

    public static <T> List<T> getList(String sql, Class<T> clazz, Map<String, Object> params) {
        Connection con = sql2o.open();
        Query query = con.createQuery(sql);
        executeQuery(query, params);
        List<T> list = query.executeAndFetch(clazz);
        con.close();
        return list;
    }

    public static <T> T get(String sql, Class<T> clazz, Map<String, Object> params) {
        Connection conn = sql2o.open();
        Query query = conn.createQuery(sql);
        executeQuery(query, params);
        T t = query.executeAndFetchFirst(clazz);
        conn.close();
        return t;
    }

    private static void executeQuery(Query query, Map<String, Object> params) {
        if (null != params && params.size() > 0) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                query.addParameter(key, params.get(key));
            }
        }
    }

}
