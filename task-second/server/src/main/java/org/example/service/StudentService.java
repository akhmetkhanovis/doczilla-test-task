package org.example.service;

import org.example.dao.StudentDAO;
import org.example.exception.StudentAlreadyExistException;
import org.example.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentDAO studentDAO;

    @Autowired
    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public List<Student> studentsAll() {
        return studentDAO.studentsAll();
    }

    public void save(Student student) throws StudentAlreadyExistException {
        studentDAO.save(student);
    }

    public String delete(String studentNumber) {
        return studentDAO.deleteByNumber(studentNumber);
    }
}
