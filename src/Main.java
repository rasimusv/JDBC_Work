import models.Mentor;
import models.Student;
import repositories.StudentsRepository;
import repositories.StudentsRepositoryJdbcImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String URL = "jdbc:postgresql://localhost:5432/JDBC_Work";
    private static final String USER = "postgres";
    private static final String PASSWORD = "micron200104";


    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        StudentsRepository studentsRepository = new StudentsRepositoryJdbcImpl(connection);
        studentsRepository.update(new Student(13L, "Карим", "Валеев", 19, 903, new ArrayList<Mentor>()));
        System.out.println(studentsRepository.findAllByAge(19));
        connection.close();
    }
}
