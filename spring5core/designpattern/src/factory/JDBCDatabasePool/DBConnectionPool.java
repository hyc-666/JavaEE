package factory.JDBCDatabasePool;

import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Date;

/**
 * 数据库连接池管理类
 * @author hyc
 * @date 2021/3/29
 */
public final class DBConnectionPool extends Pool {

    private int checkOut;//正在使用的连接数
    private List<Connection> freeConnections = new ArrayList<>();//存放产生的连接对象容器
    private String passWord = null;//密码
    private String url = null;//连接字符串
    private String userName = null;//用户名
    private static int num = 0;//空闲连接数
    private static int numActive = 0;//当前可用连接数
    private static DBConnectionPool pool = null;//连接池实例变量

    //产生数据连接池
    public static synchronized DBConnectionPool getInstance(){
        if (pool == null){
            pool = new DBConnectionPool();
        }
        return pool;
    }
    //获得一个数据库连接池的实例
    private DBConnectionPool(){
        try{
            init();
            for (int i = 0; i < normalConnect; i++) {//初始 normalConnect 个连接
                Connection c = newConnection();
                if (c != null){
                    freeConnections.add(c);//往容器中添加一个连接对象
                    num++;//记录总连接数
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //初始化
    private void init() throws IOException{
        InputStream is = DBConnectionPool.class.getClassLoader().getResourceAsStream(propertiesName);
        Properties p = new Properties();
        p.load(is);
        this.userName = p.getProperty("userName");
        this.passWord = p.getProperty("passWord");
        this.driverName = p.getProperty("driverName");
        this.url = p.getProperty("url");
        this.maxConnect = Integer.parseInt(p.getProperty("maxConnect"));
        this.normalConnect = Integer.parseInt(p.getProperty("normalConnect"));
    }
    //创建一个新连接
    private Connection newConnection(){
        Connection con = null;
        try{
            if (userName == null){//用户,密码都为空
                con = DriverManager.getConnection(url);
            } else {
                con = DriverManager.getConnection(url,userName,passWord);
            }
            System.out.println("连接池创建一个新的连接");
        } catch (SQLException e){
            System.out.println("无法创建这个URL的连接:" + url );
            return null;
        }
        return con;
    }
    //如果不在使用某个连接对象,可调用此方法将该对象释放到连接池
    public synchronized void freeConnection(Connection con){
        freeConnections.add(con);
        num++;
        checkOut--;
        numActive--;
        notify();//解锁
    }

    /**
     * 建立连接池
     */
    @Override
    public void createPool() {
        pool = new DBConnectionPool();
        if (pool != null){
            System.out.println("创建连接池成功");
        }else {
            System.out.println("创建连接池失败");
        }
    }

    /**
     * (单例模式)获取一个可用连接
     * @return
     */
    @Override
    public synchronized Connection getConnection() {
        Connection con = null;
        if (freeConnections.size() > 0){//若还有空闲连接
            num--;
            con = freeConnections.remove(0);
            try{
                if (con.isClosed()){
                    System.out.println("从连接池中删除一个无效连接");
                    con = getConnection();
                }
            } catch (SQLException e){
                System.out.println("从连接池中删除一个无效连接");
                con = getConnection();
            }
        } else if(maxConnect == 0 || checkOut < maxConnect){
            //没有控线连接且当前连接小于最大允许值,最大值为0则不限制
            con = newConnection();
        }
        if (con != null){//当前连接数+1
            checkOut++;
        }
        numActive++;
        return con;
    }

    /**
     * 获取一个连接,并加上等待时间限制,单位为毫秒
     * @param time 最大等待时间,单位是毫秒
     * @return 返回一个连接
     */
    @Override
    public Connection getConnection(long time) {
        long startTime = new Date().getTime();
        Connection con;
        while ((con = getConnection()) == null){
            try{
                wait(time);
            }catch ( InterruptedException e){

            }
            if ((new Date().getTime() - startTime) >= time){
                return null;//如果超时,则返回
            }
        }
        return con;
    }

    /**
     * 关闭所有连接
     */
    @Override
    public synchronized void release(){
        try{
           //使用循环关闭所用连接
            while (!freeConnections.isEmpty()){
                Connection con = freeConnections.remove(0);
                try{
                    con.close();
                    num--;
                }catch (SQLException e){
                    System.out.println("无法关闭连接池中的连接");
                }
            }
            numActive = 0;
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            super.release();
        }
    }
    @Override
    public void freeConnection() {

    }

    /**
     * 返回当前空闲连接数
     * @return
     */
    @Override
    public int getNum() {
        return num;
    }

    /**
     * 返回当前连接数
     * @return
     */
    @Override
    public int getNumActive() {
        return numActive;
    }
}