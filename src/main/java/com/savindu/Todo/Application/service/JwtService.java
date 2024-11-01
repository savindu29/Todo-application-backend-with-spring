package com.savindu.Todo.Application.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.savindu.Todo.Application.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    private final JwtDecoder jwtDecoder;

    @Autowired
    public JwtService(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public String createJwtToken(AppUser appUser) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .subject(appUser.getEmail())
                .claim("userId", appUser.getId())  // Add userId claim here
                .issuedAt(now)
                .expiresAt(now.plusSeconds(24 * 60 * 60))
                .build();
        NimbusJwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey.getBytes()));
        JwtEncoderParameters params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return encoder.encode(params).getTokenValue();
    }

    public Long getUserIdFromToken(String token) {
        if (token != null && !token.isEmpty()) {
            Jwt jwt = jwtDecoder.decode(token);
            System.out.println("Decoded JWT Claims: " + jwt.getClaims());  // Log all claims

            Object userIdClaim = jwt.getClaim("userId");
            return userIdClaim != null ? Long.parseLong(userIdClaim.toString()) : null;
        }
        throw new RuntimeException("Authorization token is missing or invalid");
    }

}
