package com.smartflow.crrc.exception;

import com.smartflow.crrc.controller.BaseController;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseController {
    /**
     * 处理请求对象属性不满足校验规则的异常信息
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Map<String, Object> methodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException exception) {
        Map<String, Object> json = new HashMap<>();
        //Get the error messages for invalid fields
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));

        StringBuffer errorMsg = new StringBuffer();
        List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        errors.stream().forEach(x -> errorMsg.append(x.getDefaultMessage()).append("; "));

//        json = baseController.setJson(0,errorMessage, 1);
        json = this.setJson(0, errorMsg.toString(), 1);
        return json;
    }

    /**
     * 处理请求单个参数不满足校验规则的异常信息
     *
     * @param request
     * @param exception
     * @return
     */
    public Map<String, Object> constraintViolationExceptionHandler(HttpServletRequest request, ConstraintViolationException exception) {
        Map<String, Object> json = new HashMap<>();
        log.info(exception.getMessage());
        return this.setJson(0, exception.getMessage(), 1);
    }

    /**
     * 处理未定义的其他异常信息
     *
     * @param request
     * @param exception
     * @return
     */
    public Map<String, Object> exceptionHandler(HttpServletRequest request, Exception exception) {
        log.error(exception.getMessage());
        return this.setJson(0, exception.getMessage(), 1);
    }

}
