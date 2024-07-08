package ru.cft.template.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.cft.template.core.exception.InsufficientFundsException;
import ru.cft.template.core.exception.InvoiceNotFoundException;
import ru.cft.template.core.exception.ServiceException;
import ru.cft.template.core.exception.UserEmailException;
import ru.cft.template.core.exception.UserIdNotMatch;
import ru.cft.template.core.exception.UserNotFoundException;
import ru.cft.template.core.exception.UserPhoneException;

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

    @ExceptionHandler(UserEmailException.class)
    public ResponseEntity<Object> handle(UserEmailException exception) {
        return new ResponseEntity<>("Почта не найдена: " + exception.getLocalizedMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserPhoneException.class)
    public ResponseEntity<Object> handle(UserPhoneException exception) {
        return new ResponseEntity<>("Номер телефона не найден: " + exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handle(UserNotFoundException exception) {
        return new ResponseEntity<>("Пользователь не найден: " + exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<Object> handle(InvoiceNotFoundException exception) {
        return new ResponseEntity<>("Счет на оплату не найден: " + exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Object> handle(InsufficientFundsException exception) {
        return new ResponseEntity<>("Не хватает средств: " + exception.getLocalizedMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserIdNotMatch.class)
    public ResponseEntity<Object> handle(UserIdNotMatch exception) {
        return new ResponseEntity<>("Номер телефона и пользователь в сесси не совпадают: " + exception.getLocalizedMessage(), HttpStatus.FORBIDDEN);
    }
}
