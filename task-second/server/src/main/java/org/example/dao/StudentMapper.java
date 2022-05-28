package org.example.dao;


import org.example.model.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();

        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setMiddleName(rs.getString("middle_name"));
        student.setDateOfBirth((rs.getDate("date_of_birth")));
        student.setGroupName(rs.getString("group_name"));
        student.setStudentNumber((rs.getString("student_number")));

        return student;
    }
}
