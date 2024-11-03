package com.savindu.Todo.Application.util;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.savindu.Todo.Application.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtUtil {
    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    private final JwtDecoder jwtDecoder;

    @Autowired
    public JwtUtil(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }



    public String createJwtToken(AppUser appUser) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .subject(appUser.getEmail())
                .claim("userId", appUser.getId())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(24 * 60 * 60))
                .build();

        NimbusJwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey.getBytes()));
        JwtEncoderParameters params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return encoder.encode(params).getTokenValue();
    }

    public  Long getUserIdFromToken(String token) {
        if (token != null && !token.isEmpty()) {

            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Jwt jwt = jwtDecoder.decode(token);


            Object userIdClaim = jwt.getClaim("userId");
            return userIdClaim != null ? Long.parseLong(userIdClaim.toString()) : null;
        }
        throw new RuntimeException("Authorization token is missing or invalid");
    }


    public  boolean validateToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            Instant now = Instant.now();
            Instant expiry = jwt.getExpiresAt();


            return expiry != null && now.isBefore(expiry);
        } catch (Exception e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }
    public  Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String token = jwt.getTokenValue();

            if (!validateToken(token)) {
                throw new RuntimeException("Invalid JWT token");
            }

            Long userId = getUserIdFromToken(token);

            return userId;
        } else {
            throw new RuntimeException("User is not authenticated or token is invalid");
        }
    }


}
