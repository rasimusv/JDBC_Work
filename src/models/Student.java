package models;

import java.util.List;
import java.util.Objects;

public class Student {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private int groupNumber;
    private List<Mentor> mentors;

    public Student(Long id, String firstName, String lastName, int age, int groupNumber, List<Mentor> mentors) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.groupNumber = groupNumber;
        this.mentors = mentors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public List<Mentor> getMentors() {
        return mentors;
    }

    public void setMentors(List<Mentor> mentors) {
        this.mentors = mentors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return getAge() == student.getAge() &&
                getGroupNumber() == student.getGroupNumber() &&
                Objects.equals(getId(), student.getId()) &&
                Objects.equals(getFirstName(), student.getFirstName()) &&
                Objects.equals(getLastName(), student.getLastName()) &&
                Objects.equals(getMentors(), student.getMentors());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getAge(), getGroupNumber(), getMentors());
    }

    @Override
    public String toString() {
        return "\n\nStudent {" + "\n" +
                "id = " + id + "\n" +
                "firstName = " + firstName + "\n" +
                "lastName = " + lastName +  "\n" +
                "age = " + age + "\n" +
                "groupNumber = " + groupNumber + "\n" +
                "mentors = " + mentors +
                "}";
    }
}
