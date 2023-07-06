package com.example.demo.service;

import com.example.demo.dto.StudentScore;
import com.example.demo.entity.StudentEntity;
import com.example.demo.repository.StudentScoreRepository;
import org.springframework.stereotype.Service;

import static com.example.demo.dto.StudentScore.toScoreEntity;
import static com.example.demo.entity.StudentScoreEntity.toScoreDto;

@Service
public class StudentScoreService {
    private final StudentScoreRepository studentScoreRepository;
    private final StudentService studentService;

    public StudentScoreService(StudentScoreRepository studentScoreRepository,StudentService studentService) {
        this.studentScoreRepository = studentScoreRepository;
        this.studentService = studentService;
    }

    public StudentScore updateScore(StudentScore studentScore) {
        StudentEntity studentEntity = studentService.getStudentById(studentScore.getStudentId());
        studentEntity.setCurrentScore(studentScore.getScore());
        studentService.saveStudent(studentEntity);
        return toScoreDto.apply(studentScoreRepository.save(toScoreEntity.apply(studentScore)));
    }
}
