package com.example.demo.entity;

import com.example.demo.dto.StudentScore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name = "student_score")
public class StudentScoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_student_score_id")
    private Long id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "FK_student_id", referencedColumnName = "PK_student_id")
    private StudentEntity student;
    @Column(nullable = false)
    private Integer score;

    public static Function<StudentScoreEntity, StudentScore> toScoreDto = studentScoreEntity ->
            StudentScore
                    .builder()
                    .score(studentScoreEntity.getScore())
                    .studentId(studentScoreEntity.getStudent().getId())
                    .build();
}
