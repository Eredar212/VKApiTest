package vk.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.Map;

@ControllerAdvice
public class NotReadableCustomException extends DefaultHandlerExceptionResolver {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleNotReadableException(HttpMessageNotReadableException ex) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.status(HttpStatus.OK).body(mapper.writeValueAsString(Map.of("error", "Неверный формат запроса")));
    }

}
