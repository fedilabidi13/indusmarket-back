package tn.esprit.usermanagement.servicesImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY="3778214125442A472D4A614E645267556B58703273357638792F423F4528482B";

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken,Claims::getSubject);

    }
    public <T> T extractClaim(String jwtToken, Function<Claims,T> claimsResolver)
    {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }
    public String generateJwtToken(UserDetails userDetails)
    {
        return generateJwtToken(new HashMap<>(),userDetails);
    }
    public String generateJwtToken(Map<String, Object> extraClaims, UserDetails userDetails)
    {
        return   Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }
    public Claims extractAllClaims(String jwtToken)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean isJwtTokenValid(String jwtToken, UserDetails userDetails)
    {
        final String username = extractUsername(jwtToken);
        return (username.equals(userDetails.getUsername())) && !isJwtTokenExpired(jwtToken);
    }
    public Boolean isJwtTokenExpired(String jwtToken)
    {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {

        return extractClaim(jwtToken, Claims::getExpiration);}
}
