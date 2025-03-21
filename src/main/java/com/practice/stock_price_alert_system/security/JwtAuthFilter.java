package com.practice.stock_price_alert_system.security;

import com.practice.stock_price_alert_system.exception.GlobalException;
import com.practice.stock_price_alert_system.service.SecurityService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final SecurityService securityService;

    @Autowired
    public JwtAuthFilter(JwtUtil jwtUtil, SecurityService securityService){
        this.jwtUtil = jwtUtil;
        this.securityService = securityService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        String jwToken = authorizationHeader.substring(7);
        String username;
        try{
            username = jwtUtil.extractUsernameFromJwt(jwToken);
        }catch(ExpiredJwtException e){
            throw new GlobalException("Expired Jwt");
        }catch(MalformedJwtException e){
            throw new GlobalException("Invalid Jwt format or Modified Jwt");
        } catch (SignatureException e) {
            throw new GlobalException("JWT signature does not match.");
        } catch(JwtException e){
            throw new GlobalException("Invalid Jwt : "+e.getMessage());
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserLoadedFromDbBySecurity userDetails = securityService.loadUserByUsername(username);
            try{
                if(jwtUtil.validatejwToken(jwToken,userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request)); //This line is useless for me now and hence and am not focusing over it now

                    SecurityContextHolder.getContext().setAuthentication(authToken); //This line is most IMPORTANT

                }
            }catch(Exception e){
                throw new GlobalException("JWT validation failed: " + e.getMessage());
            }
        }

        filterChain.doFilter(request,response);

    }
}
