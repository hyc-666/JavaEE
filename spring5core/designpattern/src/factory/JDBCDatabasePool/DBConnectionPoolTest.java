package factory.JDBCDatabasePool;

/**
 * @author hyc
 * @date 2021/3/29
 */
public class DBConnectionPoolTest {
    public static void main(String[] args) {
        Pool connectionPool = DBConnectionPool.getInstance();
        System.out.println(connectionPool);
    }
}
