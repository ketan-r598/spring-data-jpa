package io.java_core.taskmanagementapi.controller;

import io.java_core.taskmanagementapi.exception.TaskNotCreatedException;
import io.java_core.taskmanagementapi.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(TaskNotCreatedException.class)
    public ResponseEntity<ProblemDetail> handleTaskNotCreated(TaskNotCreatedException ex) {
        return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException exception) {
//        System.out.println(exception.getFieldError("title").getRejectedValue());
//        System.out.println(exception.getFieldError("title").getDefaultMessage());
//        System.out.println(exception.getFieldError("title").getField());
//        System.out.println(exception.getFieldError("title").getObjectName());
//        System.out.println(exception.getFieldError("title").getField());
//
//
//        System.out.println(exception.getBody());
//        System.out.println(exception.updateAndGetBody());
//        System.out.println(exception.getDetailMessageArguments());
//
//
//        System.out.println(exception.getFieldError("description"));
//        return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getBody().getDetail()));

        String detail = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problemDetail.setTitle("Validation Failed");

        return ResponseEntity.badRequest().body(problemDetail);
    }

//    @ExceptionHandler(InvalidSortFieldException.class)
//    public ResponseEntity<ProblemDetail> handleInvalidSortParameter(InvalidSortFieldException ex) {
//        return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage()));
//    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }


}
