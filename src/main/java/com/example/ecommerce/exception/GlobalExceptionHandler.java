package com.example.ecommerce.exception;

import com.example.ecommerce.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Ловим именно RuntimeException (который у тебя в сервисе)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(), // Сюда прилетит твое сообщение "Корзина пуста"
                System.currentTimeMillis()
        );

        // Отдаем статус 400 (Bad Request), так как это ошибка данных пользователя
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}