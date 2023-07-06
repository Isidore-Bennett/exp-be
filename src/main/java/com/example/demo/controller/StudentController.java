package com.example.demo.controller;

import com.example.demo.dto.request.CreateStudentRequest;
import com.example.demo.dto.request.CriteriaSearch;
import com.example.demo.dto.request.UpdateStudentRequest;
import com.example.demo.dto.response.StudentResponse;
import com.example.demo.entity.StudentEntity;
import com.example.demo.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/experian/student")
@Slf4j
@CrossOrigin
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        try {
            log.info("Attempting to retrieve all students");
            return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);
        } catch (Exception e) {
            log.info("Failed to retrieve all students");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentEntity> getStudentById(@PathVariable String id) {
        try {
            log.info("Attempting to retrieve student: {}", id);
            return new ResponseEntity<>(studentService.getStudentById(id), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to retrieve student: {}", id);
            log.error("Error", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest student) {
        try {
            log.info("Attempting to create student: {}", student);
            return new ResponseEntity<>(studentService.createStudent(student), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to create student: {}", student);
            log.error("Error", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<StudentResponse> updateStudent(@Valid @RequestBody UpdateStudentRequest student) {
        try {
            log.info("Attempting to create student: {}", student);
            return new ResponseEntity<>(studentService.updateStudent(student), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to create student: {}", student);
            log.error("Error", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        try {
            log.info("Attempting to delete student: {}", id);
            studentService.deleteStudent(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to delete student: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("search")
    public ResponseEntity<List<StudentEntity>> searchByCriteria(@RequestBody CriteriaSearch criteriaSearch) {
        try {
            log.info("Attempting to find students by: {}", criteriaSearch);
            return new ResponseEntity<>(studentService.findStudentsByCriteria(criteriaSearch), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to find students by: {}", criteriaSearch);
            log.error("Error", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
