package org.example.expert.aop;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;


@Aspect
@Slf4j
public class AdminAspect {

    private final String BEARER_PREFIX = "Bearer ";
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Pointcut("@annotation(org.example.expert.annotation.CheckAdmin)")
    private void checkAdminAnnotation(){}

    @Around("checkAdminAnnotation()")
    public Object adviceAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {

        LocalDateTime startTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedStartTime = startTime.format(formatter);

        // HttpServletRequest 가져오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 요청 URL 가져오기
        String requestUrl = request.getRequestURI();

        String authorizationHeader = request.getHeader("Authorization");
        String userId = null;

        // Authorization 헤더가 존재하고 Bearer 토큰일 경우
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.replace(BEARER_PREFIX, "");
            userId = extractClaims(token).getSubject();
        }

        try{
            Object result = joinPoint.proceed();
            return result;
        }finally {
            //메서드 실행 후
            //측정 완료
            log.info("::: Request URL: {}", requestUrl);
            log.info("::: User ID: {}", userId);
            log.info("::: StartTime : {}", formattedStartTime);
        }

    }

}