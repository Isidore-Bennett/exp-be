package com.example.demo.controller;

import com.example.demo.dto.StudentScore;
import com.example.demo.service.StudentScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/experian/score")
@Slf4j
@CrossOrigin
public class StudentScoreController {
    private final StudentScoreService studentScoreService;

    public StudentScoreController(StudentScoreService studentScoreService) {
        this.studentScoreService = studentScoreService;
    }

    @PutMapping("")
    public ResponseEntity<?> addScore(@Valid @RequestBody StudentScore studentScore) {
        try {
            log.info("Attempting to add student score: {}", studentScore);
            studentScoreService.updateScore(studentScore);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to add student score: {}", studentScore);
            log.error("Failed to add student score error: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
