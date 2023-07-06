package com.example.demo.dto.request;

import com.example.demo.entity.StudentEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class CreateStudentRequest implements Serializable {
    @NotBlank(message = "First name needs to be provided")
    private String firstName;
    @NotBlank(message = "Last name needs to be provided")
    private String lastName;
    @NotNull(message = "Date of birth needs to be provided")
    private Date dateOfBirth;
    @NotBlank(message = "Cell number needs to be provided")
    @Pattern(regexp = "^(\\+\\d{11}|\\d{10})$", message = "Invalid phone number")
    private String cellNumber;
    @NotBlank(message = "Email needs to be provided")
    @Email(message = "Invalid email")
    private String emailAddress;
    @Digits(message = "Invalid score value", integer = 3, fraction = 0)
    @Min(value = 0, message = "Score cannot be less than 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Integer currentScore;

    public static Function<CreateStudentRequest, StudentEntity> createToStudentEntity = student ->
            StudentEntity
                    .builder()
                    .firstName(student.getFirstName())
                    .lastName(student.getLastName())
                    .cellNumber(student.getCellNumber())
                    .emailAddress(student.getEmailAddress())
                    .dateOfBirth(student.getDateOfBirth())
                    .currentScore(student.getCurrentScore())
                    .build();
}
