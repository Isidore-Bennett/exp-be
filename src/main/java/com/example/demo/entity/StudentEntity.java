package com.example.demo.entity;

import com.example.demo.dto.response.StudentResponse;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "student")
public class StudentEntity {
    @Id
    @Column(name = "PK_student_id")
    private String id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private Date dateOfBirth;
    @Column(nullable = false)
    private String cellNumber;
    @Column(nullable = false)
    private String emailAddress;
    private Integer currentScore;
    @JsonManagedReference
    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private List<StudentScoreEntity> studentScores = new ArrayList<>();

    public static Function<StudentEntity, StudentResponse> toStudentResponse = student -> StudentResponse
            .builder()
            .id(student.getId())
            .firstName(student.getFirstName())
            .lastName(student.getLastName())
            .dateOfBirth(student.getDateOfBirth())
            .cellNumber(student.getCellNumber())
            .emailAddress(student.getEmailAddress())
            .currentScore(student.getCurrentScore())
            .averageScore(Objects.nonNull(student.getStudentScores())
                    ? student.getStudentScores()
                    .stream()
                    .mapToInt(StudentScoreEntity::getScore)
                    .average()
                    .orElse(0.0)
                    : null)
            .build();

    public static Function<List<StudentEntity>, List<StudentResponse>> toStudentListDto = students ->
            students.stream().map(studentEntity -> StudentResponse
                    .builder()
                    .id(studentEntity.getId())
                    .firstName(studentEntity.getFirstName())
                    .lastName(studentEntity.getLastName())
                    .dateOfBirth(studentEntity.getDateOfBirth())
                    .cellNumber(studentEntity.getCellNumber())
                    .emailAddress(studentEntity.getEmailAddress())
                    .currentScore(studentEntity.getCurrentScore())
                    .averageScore(Objects.nonNull(studentEntity.getStudentScores())
                            ? (BigDecimal.valueOf(studentEntity.getStudentScores()
                            .stream()
                            .mapToInt(StudentScoreEntity::getScore)
                            .average()
                            .orElse(0.0)).setScale(2, RoundingMode.UP))
                            .doubleValue()
                            : null)
                    .build()).collect(Collectors.toList());
}
