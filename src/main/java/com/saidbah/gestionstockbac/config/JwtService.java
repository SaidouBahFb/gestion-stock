package com.saidbah.gestionstockbac.config;

import com.saidbah.gestionstockbac.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "ngeqPG7YfsPgCQLXPdincxq92V/C+Y6xe9OOyGJeTKqVhlzNyCFKhoIBLgQKlFqlebT3gycnmlZt+CS73UdO1cgi5yGaHZnnIAoDn5uPDVxW10b5pZXCXPc2KmD70IBPp9XJ7Xm0fCSCeR5XnMFEWxbU5NQtMQyYeZu47KP2TuhKWXnyoPdMZT/f5QUCSgTr2vzM50MgmByyfJ/JUd95uFGfo/Qy8yBw+5mAFTUwbJscJnQhSmMH8eyIURv+cWj9xj/7+QC92X5yS4C4apfUvMw5O4YwtXGv5/rEncr9bb2jG1aJyGSX2StFuytHoEv+7t6Vw2u4TCJUHAylz4vaL6vNgrP5TIZNYuZTVZpNGMifUXkMjLRJdaZorZbHlHLteJuNs2FMqmYN2g3EVQBByKmeBeQakGKGjpUA8sjBRMUgvuCueOkuVh0h6UG3Ki0eizd1PtnhG2U43TlMrJLdF91aGvd2C0eBtpq/DHssKLxlJOHwDxpKTPfr8mZvUslD8Rk1uKb6j3EAphXjO9l7IlV9aQPJqdHbGb3zPsMn8EuAJExbuBvw3YLgu3XVE9Jc6+Yl2wr+8uQPDv1TyPHHbttIVPTy6GBh747/mDfvPPrsbzN4tE8fsYRyB31PJFUgnz/JacsDl+DO+VYkYA+Y1x2m9jra51quW2YXnePJivc";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        if (!(userDetails instanceof User user)) {
            throw new IllegalArgumentException("UserDetails must be an instance of User");
        }

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", user.getRoles().stream().map(Enum::name).toList());
        extraClaims.put("firstname", user.getFirstname());
        extraClaims.put("lastname", user.getLastname());
        extraClaims.put("phone", user.getPhone());
        extraClaims.put("address", user.getAddress());

        Instant now = Instant.now();
        Instant expirationTime = now.plus(24, ChronoUnit.HOURS);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractExpiration(token);
        return expirationDate.before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
