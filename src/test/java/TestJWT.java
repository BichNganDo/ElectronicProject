
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.UUID;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

public class TestJWT {
    
    public static void main(String[] args) throws InterruptedException {
        String SECRET_KEY = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY),
                SignatureAlgorithm.HS256.getJcaName());
        
        Date now = new Date(System.currentTimeMillis());
        Date expire = new Date(System.currentTimeMillis() + 1 * 10 * 1000);
        System.out.println(now);
        String jwtToken = Jwts.builder()
                .claim("name", "Ngan")
                .claim("email", "ngando6598@gmail.com")
                .setSubject("jane")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(hmacKey)
                .compact();
//        System.out.println(jwtToken);

        Thread.sleep(5 * 1000l);
        Jws<Claims> parseJwt = parseJwt(jwtToken);
        System.out.println(parseJwt);
    }
    
    public static Jws<Claims> parseJwt(String jwtString) {
        String SECRET_KEY = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY),
                SignatureAlgorithm.HS256.getJcaName());
        
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString);
        
        return jwt;
    }
}
