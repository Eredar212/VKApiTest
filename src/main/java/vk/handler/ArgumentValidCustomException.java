package vk.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ArgumentValidCustomException extends DefaultHandlerExceptionResolver {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.status(HttpStatus.OK).body(mapper.writeValueAsString(Map.of("error",
                Arrays.stream(ex.getDetailMessageArguments()).map(e -> e.toString())
                        .collect(Collectors.joining(" ")))));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.status(HttpStatus.OK).body(mapper.writeValueAsString(Map.of("error", ex.getMessage())));
    }
}
