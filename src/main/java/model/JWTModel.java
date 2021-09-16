package model;

import client.MysqlClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;

public class JWTModel {

    private static final String SECRET_KEY = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

    public String genJWT(String username, String phone) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY),
                SignatureAlgorithm.HS256.getJcaName());

        Date now = new Date(System.currentTimeMillis());
        Date expire = new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000);
        System.out.println(now);
        String jwtToken = Jwts.builder()
                .claim("name", username)
                .claim("phone", phone)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(hmacKey)
                .compact();

        return jwtToken;
    }

    public static Jws<Claims> parseJwt(String jwtString) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY),
                SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString);

        return jwt;
    }
}
