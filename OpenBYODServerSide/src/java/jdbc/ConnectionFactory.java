/*
 * source: http://www.caelum.com.br/apostila-java-web/bancos-de-dados-e-jdbc/#2-4-fabrica-de-conexoes
 */
package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static util.Constants.DB_DRIVER_CLASS;
import static util.Constants.DB_PASS;
import static util.Constants.DB_URL_OPENBYOD;
import static util.Constants.DB_USER;

/**
 *
 * @author Castro
 */
public class ConnectionFactory {
    
    public ConnectionFactory(){
        try {
            Class.forName(DB_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());

        }
    }

    public Connection getConnectionOpenBYODDB() {
        try {
            return DriverManager.getConnection(
                    DB_URL_OPENBYOD, DB_USER, DB_PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnectionHmailDB() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/fj21", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
