package com.example.demo.controller;

import com.example.demo.dto.StudentScore;
import com.example.demo.service.StudentScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentScoreController.class)
public class StudentScoreControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentScoreService studentScoreService;
    StudentScore studentScore1 = StudentScore.builder()
            .studentId("String-id")
            .score(10)
            .build();

    @Test
    public void testAddScoreAPI() throws Exception {
        StudentScore studentScore = StudentScore.builder()
                .studentId("student-id")
                .score(26)
                .build();
        given(studentScoreService.updateScore(studentScore)).willReturn(studentScore);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/experian/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(studentScore))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testScoreNoIdAPI() throws Exception {
        StudentScore studentScore = StudentScore.builder()
                .studentId(null)
                .score(10)
                .build();
        given(studentScoreService.updateScore(any(StudentScore.class))).willReturn(studentScore1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/experian/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(studentScore))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("studentId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value("Student ID must be provided"));
    }

    @Test
    public void testScoreExceedsAPI() throws Exception {
        StudentScore studentScore = StudentScore.builder()
                .studentId("student-id")
                .score(101)
                .build();
        given(studentScoreService.updateScore(any(StudentScore.class))).willReturn(studentScore1);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/experian/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(studentScore))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("score"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value("Score cannot exceed 100"));
    }

    @Test
    public void testScoreNegativeAPI() throws Exception {
        given(studentScoreService.updateScore(any(StudentScore.class))).willReturn(studentScore1);

        StudentScore studentScore = StudentScore.builder()
                .studentId("student-id")
                .score(-1)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/experian/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(studentScore))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("score"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value("Score cannot be less than 0"));
    }

    @Test
    public void testScoreNullAPI() throws Exception {
        given(studentScoreService.updateScore(any(StudentScore.class))).willReturn(studentScore1);

        StudentScore studentScore = StudentScore.builder()
                .studentId("student-id")
                .score(null)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/experian/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(studentScore))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("score"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value("Score must be provided"));
    }

    @Test
    public void testScoreInvalidAPI() throws Exception {
        given(studentScoreService.updateScore(any(StudentScore.class))).willReturn(studentScore1);

        Map<String, Object> map = new HashMap<>();
        map.put("studentId", "student-id");
        map.put("score", "asd");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/experian/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(map))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value("score"));
    }
}
