package services;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateless;
import java.security.Key;
import java.util.Optional;

@Stateless
public class TokenService {
    private final Key key = generateKey("shippingport", "HmacSHA512");

    public String generate(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public Optional<String> verify(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return Optional.of(claimsJws.getBody().getSubject());
        } catch (JwtException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    private Key generateKey(String keyword, String algorithm) {
        return new SecretKeySpec(keyword.getBytes(), 0, keyword.getBytes().length, algorithm);
    }
}
