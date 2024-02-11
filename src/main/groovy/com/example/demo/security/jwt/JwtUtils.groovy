package com.example.demo.security.jwt

import com.example.demo.dto.UserDTO
import groovy.util.logging.Slf4j
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
@Slf4j
class JwtUtils {

    @Value('${my.jwtSecret}')
    private String jwtSecret

    @Value('${my.jwtExpirationMs}')
    private int jwtExpirationMs

    //Should be some database or tool to store this values in cache but for now it is just ArrayList =)
    private static List<String> invalidTokens = new ArrayList<>()


    String generateJwtToken(Authentication authentication) {
        UserDTO userPrincipal = (UserDTO) authentication.getPrincipal()


        String jwt = Jwts.builder()
                .setSubject((userPrincipal.username))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).time + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact()
        if(invalidTokens.contains(jwt)){
            invalidTokens.remove(jwt)
        }
        return jwt
    }

    private key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))
    }

    def getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(key())
                .parseClaimsJws(token).getBody().getSubject()
    }

    def validateJwtToken(String authToken) {
        if(invalidTokens.contains(authToken)){
            return false
        }
        try {
            Jwts.parser().setSigningKey(key()).parse(authToken)
            return true
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage())
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage())
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage())
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage())
        }
        return false
    }

    def invalidateToken(String token){
        invalidTokens.add(token)
    }
}
