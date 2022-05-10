package com.epam.rd.stock.exchange.util;

import com.epam.rd.stock.exchange.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {

    private static final String CSRF_TOKEN_ATTRIBUTE = "_csrf";
    private static final String CSRF_HEADER = "X-CSRF-TOKEN";
    private static final String CSRF_ATTRIBUTE_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    private static final String CLAIM_USER_ID = "user_id";
    private static final String EXCEPTION_MESSAGE = "Token is invalid or expired ";

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;

    @Value("${jwt.secret}")
    private String secret;

    public String createAndAddTokenIntoSession(String id, HttpServletRequest request) {
        String token = createToken(id);
        DefaultCsrfToken csrfToken = new DefaultCsrfToken(CSRF_HEADER, CSRF_TOKEN_ATTRIBUTE, token);
        addTokenInSession(csrfToken, request);
        return token;
    }

    private String createToken(String userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .claim(CLAIM_USER_ID, userId)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private void addTokenInSession(CsrfToken token, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(CSRF_ATTRIBUTE_NAME, token);
    }

    public String getUserIdFromToken(String token, HttpServletRequest request) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            String userId = e.getClaims().get(CLAIM_USER_ID, String.class);
            log.info("Refresh token");
            String newToken = createAndAddTokenIntoSession(userId, request);
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(newToken).getBody();
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new InvalidTokenException(EXCEPTION_MESSAGE + e.getMessage());
        }
        return claims.get(CLAIM_USER_ID, String.class);
    }
}
