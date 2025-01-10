package com.wirebarley.core.interceptor;


import com.wirebarley.bank.common.dto.ApiDTO;
import com.wirebarley.bank.common.type.ResponseCode;
import com.wirebarley.core.cmm.BankMessageSource;
import com.wirebarley.core.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class WirebarleyAOP {

    private final BankMessageSource bankMessageSource;

    @Pointcut("within(com.wirebarley.bank.*.controller..*Controller)")
    public void onRequest() {
    }

    @Around("onRequest()")
    public ApiDTO onAroundHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        ApiDTO response = null;

        try {
            if (RequestContextHolder.getRequestAttributes() == null) {
                return null;
            }
            HttpServletRequest request
                    = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            log.info("HTTP/HTTPS Request : {} | Method : {}", request.getRequestURI(), request.getMethod());
            Object result = joinPoint.proceed();

            response = (ApiDTO) Objects.requireNonNull(result);

            if (!StringUtils.hasText(response.getRtMsg())) {
                response.setRtMsg(bankMessageSource.getRtCodeMessage(response.getRtCode()));
            }

        } catch (BizException be) {
            response = ApiDTO.builder()
                    .rtCode((int) be.getErrCode())
                    .rtMsg(bankMessageSource.getRtCodeMessage((int) be.getErrCode()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("AOP Exception : [{}]", e.getMessage());
            response = ApiDTO.builder()
                    .rtCode(ResponseCode.SERVER_ERROR.getCode())
                    .rtMsg(bankMessageSource.getRtCodeMessage(ResponseCode.SERVER_ERROR.getCode()))
                    .build();
        }

        return response;
    }

}
