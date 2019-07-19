package com.izuul.springsecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izuul.springsecurity.exception.MyException;
import com.izuul.springsecurity.util.FailureResponse;
import com.izuul.springsecurity.util.JwtTokenUtil;
import com.izuul.springsecurity.service.SysUserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ObjectMapper objectMapper;

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader("Authorization");
        String username = null;
        String authToken = null;
        if (header != null && header.startsWith("Bearer ")) {
            authToken = header.replace("Bearer ","");
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException e) {
                logger.error("从令牌获取用户名期间发生错误", e);
            } catch (MyException e) {
                logger.warn("令牌已过期且无效", e);
            } catch(SignatureException e){
                logger.error("身份验证失败。用户名或密码无效.");
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() != null) {

            UserDetails userDetails = sysUserService.loadUserByUsername(username);

//            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthentication(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
        }
        chain.doFilter(req, res);
    }

}
