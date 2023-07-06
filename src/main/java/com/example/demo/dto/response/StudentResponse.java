package com.example.demo.dto.response;

import com.example.demo.entity.StudentEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class StudentResponse implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String cellNumber;
    private String emailAddress;
    private Integer currentScore;
    private Double averageScore;
}
