package com.example.demo.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationError implements Serializable {
    private HttpStatus status;
    private List<Error> errors = new ArrayList<>();
}

