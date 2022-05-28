package org.example.dao;

import org.example.exception.StudentAlreadyExistException;
import org.example.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> studentsAll() {
        return jdbcTemplate.query("SELECT * FROM students", new StudentMapper());
    }

    public Student findByNumber(String studentNumber) {
        return jdbcTemplate.query("SELECT * FROM students where student_number=?",
                        new StudentMapper(),
                        studentNumber).stream()
                .findAny()
                .orElse(null);
    }

    public void save(Student student) throws StudentAlreadyExistException {
        if (findByNumber(student.getStudentNumber()) != null) {
            throw new StudentAlreadyExistException("Student with such number already exists!");
        }

        jdbcTemplate.update("INSERT INTO students " +
                        "(first_name, last_name, middle_name, date_of_birth, group_name, student_number) " +
                        "VALUES(?, ?, ?, ?, ?, ?)",
                student.getFirstName(),
                student.getLastName(),
                student.getMiddleName(),
                student.getDateOfBirth(),
                student.getGroupName(),
                student.getStudentNumber());
    }

    public String deleteByNumber(String studentNumber) {
        jdbcTemplate.update("DELETE FROM students WHERE student_number=?", studentNumber);
        return studentNumber;
    }
}
