package cr.ac.una.proyectobolsaempleo.security;

import cr.ac.una.proyectobolsaempleo.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    public String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("usuarioId", usuario.getId());
        claims.put("correo", usuario.getCorreo());
        claims.put("rol", usuario.getRol());
        claims.put("estado", usuario.getEstado());

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getCorreo())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSignInKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUsuarioId(String token) {
        Object usuarioId = extractAllClaims(token).get("usuarioId");

        if (usuarioId instanceof Integer id) {
            return id.longValue();
        }

        if (usuarioId instanceof Long id) {
            return id;
        }

        return Long.valueOf(usuarioId.toString());
    }

    public String extractRol(String token) {
        Object rol = extractAllClaims(token).get("rol");
        return rol != null ? rol.toString() : null;
    }

    public String extractEstado(String token) {
        Object estado = extractAllClaims(token).get("estado");
        return estado != null ? estado.toString() : null;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);

        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
