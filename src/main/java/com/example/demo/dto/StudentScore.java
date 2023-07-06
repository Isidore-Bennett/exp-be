package com.example.demo.dto;

import com.example.demo.entity.StudentEntity;
import com.example.demo.entity.StudentScoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.function.Function;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
public class StudentScore implements Serializable {
    @NotBlank(message = "Student ID must be provided")
    private String studentId;
    @NotNull(message = "Score must be provided")
    @Digits(message = "Invalid score value", integer = 3, fraction = 0)
    @Min(value = 0, message = "Score cannot be less than 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer score;

    public static Function<StudentScore, StudentScoreEntity> toScoreEntity = studentScore -> {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(studentScore.getStudentId());
        return StudentScoreEntity
                .builder()
                .score(studentScore.getScore())
                .student(studentEntity)
                .build();
    };
}
