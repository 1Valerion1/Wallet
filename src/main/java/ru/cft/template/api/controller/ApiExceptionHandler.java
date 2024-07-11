package ru.cft.template.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.cft.template.core.exception.InsufficientFundsException;
import ru.cft.template.core.exception.NotFoundException;
import ru.cft.template.core.exception.ServiceException;
import ru.cft.template.core.exception.ConflictException;
import ru.cft.template.core.exception.UserIdNotMatch;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handle(ServiceException exception) {
        return new ResponseEntity<>(exception.getLocalizedMessage(), exception.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handle(RuntimeException exception) {
        return new ResponseEntity<>(exception.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handle(ConflictException exception) {
        return new ResponseEntity<>("Данный ресурс уже существует: " + exception.getLocalizedMessage(), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handle(NotFoundException exception) {
        return new ResponseEntity<>("Не найдено: " + exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Object> handle(InsufficientFundsException exception) {
        return new ResponseEntity<>("Не хватает средств: " + exception.getLocalizedMessage(), HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(UserIdNotMatch.class)
    public ResponseEntity<Object> handle(UserIdNotMatch exception) {
        return new ResponseEntity<>("Номер телефона и пользователь в сесси не совпадают: " + exception.getLocalizedMessage(),
                HttpStatus.FORBIDDEN);
    }
}
