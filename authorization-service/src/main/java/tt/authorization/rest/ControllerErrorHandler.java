package tt.authorization.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tt.authorization.dto.MessageDTO;
import tt.authorization.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ControllerErrorHandler {
    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<MessageDTO> handle(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseBody
    public ResponseEntity<MessageDTO> handleExceptions(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseBody
    public ResponseEntity<MessageDTO> handleNotFoundExceptions(NotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleExceptions(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
}
