package com.qianxu.mall.exception;

import com.qianxu.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/6 19:39
 * @describe
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    @ResponseBody
    public Object handleException(Exception e) {
        logger.error("Default Exception : ", e);
        return ApiRestResponse.error(QianxuMallExceptionEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler
    @ResponseBody
    public Object handleException(QianxuMallException e) {
        logger.error("QianxuMallException : ", e);
        return ApiRestResponse.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ApiRestResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("MethodArgumentNotValidException: ", e);
        return handleBindingResult(e.getBindingResult());
    }

    private ApiRestResponse handleBindingResult(BindingResult result) {
        //把异常处理为对外暴漏的提示
        List<String> list = new LinkedList<>();
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            allErrors.forEach(item->list.add(item.getDefaultMessage()));
        }
        if (list.size() == 0) {
            return ApiRestResponse.error(QianxuMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(QianxuMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());
    }
}
