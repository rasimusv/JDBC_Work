import models.*;
import repositories.*;
import java.sql.*;
import java.util.*;

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/JDBC_Work";
    private static final String USER = "postgres";
    private static final String PASSWORD = "YOUR_PSWD";


    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        StudentsRepository studentsRepository = new StudentsRepositoryJdbcImpl(connection);
        studentsRepository.update(new Student(13L, "Карим", "Валеев", 19, 903, new ArrayList<Mentor>()));
        System.out.println(studentsRepository.findAll());
        connection.close();
    }
}
