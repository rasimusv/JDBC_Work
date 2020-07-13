package repositories;

import models.*;
import java.sql.*;
import java.util.*;

public class StudentsRepositoryJdbcImpl implements StudentsRepository {

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from student where id = ";
    private static final String SQL_SELECT_ALL_STUDENTS =
            "select  * from student right join (select id as mentor_id, first_name as mentor_first_name, last_name as mentor_last_name, student_id from mentor) as m on student.id = m.student_id";
    private static final String SQL_INSERT_STUDENT =
            "insert into student (first_name, last_name, age, group_number) values ('%s','%s',%d,%d)";
    private static final String SQL_UPDATE_STUDENT =
            "update student set first_name = '%s', last_name = '%s', age = %d, group_number = %d where id = %d";

    private Connection connection;

    public StudentsRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Student> findAllByAge(int age) {
        return null;
    }


    /* Произшел небольшой костыль - для того, чтобы понять, есть ли студент в списке студентов я использую
       метод contains, который работает через equals. Из-за того, что списки менторов у сравниваемых объектов разные
       (в данном случае это так и задумано), получается что студенты разные. Я убрал из метода equals проверку списка
       менторов, чтобы всё работало. По сути это очень неправильно и можно реализовать конкретно для этой задачи свой
       алгоритм сравнения, но тогда получилась бы сложность по памяти O(m*n), что тоже нехорошо. В данном случае я
       решил изменить equals, так как база данных сконфигурирована так, что в ней не можгут существовать две строки с
       одинаковым id. То есть вероятность того, что в будущем, при использовании equals, будет получен неверный
       резульат, довольно небольшая. Полагаю, что эта проблема возникла из-за условия задачи, которое требует, чтобы
       был только один запрос. Поэтому, полагаю, эта проблема несущественна, так как это учебная задача. При работе
       над реальной задачей, естественно следует не нарушать принципы SOLID
    */


    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        List<Mentor> mentors= new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_ALL_STUDENTS);

            while (result.next()) {

                List<Mentor> studMentors = new ArrayList<>();

                Student student = new Student(
                        result.getLong("id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getInt("age"),
                        result.getInt("group_number"),
                        studMentors);

                Mentor mentor = new Mentor(
                        result.getLong("mentor_id"),
                        result.getString("mentor_first_name"),
                        result.getString("mentor_last_name"),
                        student);

                mentors.add(mentor);

                if (!students.contains(student)) {
                    students.add(student);
                } else {
                    student = students.get(students.indexOf(student));
                }

                studMentors = student.getMentors();
                studMentors.add(mentor);
                student.setMentors(studMentors);
            }
            return students;

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    @Override
    public Student findById(Long id) {
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_BY_ID + id);

            if (result.next()) {
                return new Student(
                        id,
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getInt("age"),
                        result.getInt("group_number"),
                        null);
            } else return null;

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    @Override
    public void save(Student entity) {
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = connection.createStatement();

            String insertStudentStatement = String.format(SQL_INSERT_STUDENT,
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getAge(),
                    entity.getGroupNumber());
            statement.executeUpdate(insertStudentStatement, Statement.RETURN_GENERATED_KEYS);

            result = statement.getGeneratedKeys();
            result.next();

            entity.setId(result.getLong("id"));

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    @Override
    public void update(Student entity) {
        Statement statement = null;

        try {
            statement = connection.createStatement();

            String updateStudentStatement = String.format(SQL_UPDATE_STUDENT,
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getAge(),
                    entity.getGroupNumber(),
                    entity.getId());

            statement.executeUpdate(updateStudentStatement);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }
}
