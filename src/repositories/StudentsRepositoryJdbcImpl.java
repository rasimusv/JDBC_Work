package repositories;

import models.*;
import java.sql.*;
import java.util.*;

public class StudentsRepositoryJdbcImpl implements StudentsRepository {

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from student where id = ";
    private static final String SQL_SELECT_BY_AGE = "select * from student where age = ";
    private static final String SQL_SELECT_MENTORS_BY_STUDENT_ID = "select * from mentor where student_id = ";
    private static final String SQL_SELECT_ALL_STUDENTS = "select  * from student";
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
        List<Student> students = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_BY_AGE + age);

            while (result.next()) {
                Long id = result.getLong("id");

                students.add(new Student(
                        id,
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getInt("age"),
                        result.getInt("group_number"),
                        findMentorsByStudentByID(id)));
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
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_ALL_STUDENTS);

            while (result.next()) {
                Long id = result.getLong("id");

                students.add(new Student(
                        id,
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getInt("age"),
                        result.getInt("group_number"),
                        findMentorsByStudentByID(id)));
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
                        findMentorsByStudentByID(id));
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

    private List<Mentor> findMentorsByStudentByID (long id) {
        List<Mentor> mentors = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(SQL_SELECT_MENTORS_BY_STUDENT_ID + id);

            while (result.next()) {
                mentors.add(new Mentor(
                        result.getLong("id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        null));
            }

            return mentors;

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
}
