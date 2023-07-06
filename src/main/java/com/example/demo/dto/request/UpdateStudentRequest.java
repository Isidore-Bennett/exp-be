package com.example.demo.dto.request;

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
public class UpdateStudentRequest implements Serializable {
    @NotNull(message = "ID needs to be provided for an update operation")
    private String id;
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

    public static Function<UpdateStudentRequest, StudentEntity> updateToStudentEntity = student ->
            StudentEntity
                    .builder()
                    .id(student.getId())
                    .firstName(student.getFirstName())
                    .lastName(student.getLastName())
                    .cellNumber(student.getCellNumber())
                    .emailAddress(student.getEmailAddress())
                    .dateOfBirth(student.getDateOfBirth())
                    .build();
}
