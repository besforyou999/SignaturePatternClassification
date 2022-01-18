package jhs.signserver;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;


public class Test_DB {
    @Test
    public void test() throws Exception {
        Class.forName("org.mariadb.jdbc.Driver"); // 마리아DB
        // Class.forName("com.mysql.jdbc.Driver")


        //Schema 이름 : jhs  ,   username : root, passwd : 1234
        Connection con = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/jhs", "sh", "1234"); // 마리아 DB

        System.out.println(con);
    }
}
