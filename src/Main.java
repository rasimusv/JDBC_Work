import jdbc.SimpleDataSource;
import repositories.StudentsRepository;
import repositories.StudentsRepositoryJdbcImpl;

import java.sql.*;

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/JDBC_Work";
    private static final String USER = "postgres";
    private static final String PASSWORD = "YOUR_PSWD";


    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        StudentsRepository studentsRepository = new StudentsRepositoryJdbcImpl(connection);
        System.out.println(studentsRepository.findById(11L));
        connection.close();
    }
}
