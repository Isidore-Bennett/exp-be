package com.example.demo.service;

import com.example.demo.entity.StudentEntity;
import com.example.demo.entity.StudentScoreEntity;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.StudentScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentScoreServiceTests {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentScoreRepository studentScoreRepository;
    @Mock
    private StudentService studentService;
    private StudentScoreService studentScoreService;

    @Captor
    private ArgumentCaptor<StudentEntity> studentEntityArgumentCaptor;
    @Captor
    private ArgumentCaptor<StudentScoreEntity> studentScoreEntityArgumentCaptor;

    StudentScoreServiceTests() {
    }

    @BeforeEach
    public void setUp() {
        studentScoreService = new StudentScoreService(studentScoreRepository, studentService);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateScore() {
//        StudentScore studentScore = new StudentScore("string-id", 28);
//
//        StudentEntity studentEntity = StudentEntity.builder().id("string-id").build();
//        Mockito.when(studentService.getStudentById(Mockito.anyString())).thenReturn(studentEntity);
//
//        StudentResponse studentResponse = StudentResponse.builder().build();
//        Mockito.when(studentService.saveStudent(Mockito.any(StudentEntity.class))).thenReturn(studentResponse);
//
//        StudentScoreEntity studentScoreEntity = new StudentScoreEntity();
//        Mockito.when(studentScoreRepository.save(Mockito.any(StudentScoreEntity.class))).thenReturn(studentScoreEntity);
//
//        StudentScore updatedScore = studentScoreService.updateScore(studentScore);
//
//        Mockito.verify(studentService).getStudentById(studentScore.getStudentId());
//        Mockito.verify(studentEntity).setCurrentScore(studentScore.getScore());
//        Mockito.verify(studentService).saveStudent(studentEntityArgumentCaptor.capture());
//        Mockito.verify(studentScoreRepository).save(studentScoreEntityArgumentCaptor.capture());
//
//        Assertions.assertEquals(studentScoreEntity, studentScoreEntityArgumentCaptor.getValue());
//        Assertions.assertEquals(studentScore, updatedScore);
    }
}