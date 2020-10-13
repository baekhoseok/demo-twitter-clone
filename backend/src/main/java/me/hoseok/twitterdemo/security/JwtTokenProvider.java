package me.hoseok.twitterdemo.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import me.hoseok.twitterdemo.Account.Account;
import me.hoseok.twitterdemo.Account.payload.SimpleAccount;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static me.hoseok.twitterdemo.security.SecurityConstants.EXPIRATION_TIME;
import static me.hoseok.twitterdemo.security.SecurityConstants.SECRET;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final ObjectMapper objectMapper;

    public String generateToken(Authentication authentication) throws JsonProcessingException {
        SimpleAccount simpleAccount = (SimpleAccount)authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expireDate = new Date(now.getTime() + EXPIRATION_TIME);

        String userId = Long.toString(simpleAccount.getId());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", simpleAccount.getUsername());
        claims.put("fullName", simpleAccount.getFullName());
        claims.put("email", simpleAccount.getEmail());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT Signature");
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsuppoerted JWT token");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    public SimpleAccount getSimpleAccountFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();

        Long id = Long.parseLong((String)claims.get("id"));
        String username = (String) claims.get("username");
        String fullName = (String) claims.get("fullName");
        String email = (String) claims.get("email");
        return new SimpleAccount(id, username, fullName, email);
    }
}
