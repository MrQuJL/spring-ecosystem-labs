package com.spring.minimal.common.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.spring.minimal.common.enums.response.ResponseEnum;
import com.spring.minimal.common.exception.BusinessException;
import com.spring.minimal.common.result.Result;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一日志切面
 * <p>
 * 拦截 Controller 层，打印请求参数和响应结果
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class WebLogAspect {

    private final ObjectMapper objectMapper;

    /**
     * 定义切入点：所有 Controller 下的方法
     */
    @Pointcut("execution(public * com.spring.minimal.module.*.controller..*.*(..))")
    public void webLog() {}

    /**
     * 环绕通知
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        String methodDescription = getMethodDescription(proceedingJoinPoint);
        Object[] args = proceedingJoinPoint.getArgs();

        // 构建日志内容
        StringBuilder logBuilder = new StringBuilder();
        if (request != null) {
            logBuilder.append("\n========================================== Start ==========================================\n");
            logBuilder.append(String.format("URL            : %s\n", request.getRequestURL().toString()));
            logBuilder.append(String.format("HTTP Method    : %s\n", request.getMethod()));
            logBuilder.append(String.format("Class Method   : %s\n", methodDescription));
            logBuilder.append(String.format("IP             : %s\n", request.getRemoteAddr()));
            logBuilder.append(String.format("Request Args   : %s\n", formatArgs(args)));
        }

        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
            logBuilder.append(String.format("Response Result: %s\n", toJson(result)));
        } catch (Throwable e) {
            // 根据异常类型构建 Result 对象，模拟 GlobalExceptionHandler 的返回
            Result<?> errorResult = getErrorResult(e);
            logBuilder.append(String.format("Response Result: %s\n", toJson(errorResult)));
            throw e;
        } finally {
            long timeCost = System.currentTimeMillis() - startTime;
            logBuilder.append(String.format("Time Cost      : %d ms\n", timeCost));
            logBuilder.append("=========================================== End ===========================================");
            
            // 统一打印
            log.info(logBuilder.toString());
        }

        return result;
    }

    /**
     * 根据异常类型构建 Result 对象
     * 
     * @param e 异常对象
     * @return 构建后的 Result 对象
     */
    private Result<?> getErrorResult(Throwable e) {
        if (e instanceof BusinessException) {
            BusinessException be = (BusinessException) e;
            return Result.fail(be.getCode(), be.getMessage());
        } else if (e instanceof MethodArgumentNotValidException) {
            String message = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError().getDefaultMessage();
            return Result.fail(ResponseEnum.PARAM_ERROR.getCode(), message);
        } else if (e instanceof MissingServletRequestParameterException) {
            String message = ((MissingServletRequestParameterException) e).getParameterName() + "不能为空";
            return Result.fail(ResponseEnum.PARAM_ERROR.getCode(), message);
        } else if (e instanceof ConstraintViolationException) {
            String message = ((ConstraintViolationException) e).getConstraintViolations().iterator().next().getMessage();
            return Result.fail(ResponseEnum.PARAM_ERROR.getCode(), message);
        } else if (e instanceof BindException) {
            String message = ((BindException) e).getBindingResult().getFieldError().getDefaultMessage();
            return Result.fail(ResponseEnum.PARAM_ERROR.getCode(), message);
        } else if (e instanceof RuntimeException) {
            return Result.fail(ResponseEnum.SYSTEM_ERROR.getCode(), "运行时异常：" + e.getMessage());
        } else {
            return Result.fail(ResponseEnum.SYSTEM_ERROR.getCode(), "系统异常：" + e.getMessage());
        }
    }

    /**
     * 获取方法全限定名
     * 
     * @param point 连接点
     * @return 方法全限定名
     */
    private String getMethodDescription(ProceedingJoinPoint point) {
        return point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
    }

    /**
     * 格式化参数：过滤掉 HttpServletRequest/Response 等无法序列化的对象
     * 
     * @param args 参数数组
     * @return 格式化后的参数字符串
     */
    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        List<Object> validArgs = Arrays.stream(args)
                .filter(arg -> !(arg instanceof HttpServletRequest) &&
                        !(arg instanceof HttpServletResponse) &&
                        !(arg instanceof MultipartFile))
                .collect(Collectors.toList());

        if (validArgs.isEmpty()) {
            return "";
        }

        // 尝试转 JSON，失败则调用 toString
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(validArgs);
        } catch (JsonProcessingException e) {
            return validArgs.toString();
        }
    }

    /**
     * 转 JSON
     * 
     * @param object 要转换的对象
     * @return 转换后的 JSON 字符串
     */
    private String toJson(Object object) {
        if (object == null) {
            return "";
        }
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return object.toString();
        }
    }
}
