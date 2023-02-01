package tt.hashtranslator.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tt.hashtranslator.dto.MessageDTO;
import tt.hashtranslator.exception.AsyncException;
import tt.hashtranslator.exception.NotFoundException;
import tt.hashtranslator.exception.URIException;

import javax.validation.ValidationException;

@Slf4j
@ControllerAdvice
public class ControllerErrorHandler {
    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<MessageDTO> handle(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AsyncException.class)
    @ResponseBody
    public ResponseEntity<MessageDTO> handle(AsyncException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ValidationException.class, NotFoundException.class, URIException.class})
    @ResponseBody
    public ResponseEntity<MessageDTO> handleExceptions(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new MessageDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
