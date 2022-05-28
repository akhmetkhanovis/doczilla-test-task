package org.example.controller;

import org.example.exception.StudentAlreadyExistException;
import org.example.model.Student;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<String> saveStudent(@RequestBody Student student) {
        try {
            studentService.save(student);
            return ResponseEntity.ok("Student added");
        } catch (StudentAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Student>> students() {
        List<Student> students = studentService.studentsAll();
        return ResponseEntity.of(Optional.of(students));
    }

    @DeleteMapping("/{studentNumber}")
    public ResponseEntity<String> deleteStudent(@PathVariable String studentNumber) {
        return ResponseEntity.ok(studentService.delete(studentNumber));
    }

}
