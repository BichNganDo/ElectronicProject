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
    private static final long _1_HOUR_IN_MILI = 3600000;
    public static JWTModel INSTANCE = new JWTModel();

    private JWTModel() {
    }

    public String genJWT(String username, String phone, int role) {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY),
                SignatureAlgorithm.HS256.getJcaName());

        Date now = new Date(System.currentTimeMillis());
        Date expire = new Date(System.currentTimeMillis() + _1_HOUR_IN_MILI);
        System.out.println(now);
        String jwtToken = Jwts.builder()
                .claim("username", username)
                .claim("phone", phone)
                .claim("role", role)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(hmacKey)
                .compact();

        return jwtToken;
    }

    public Jws<Claims> parseJwt(String jwtString) {
        try {
            Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY),
                    SignatureAlgorithm.HS256.getJcaName());

            Jws<Claims> jwt = Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(jwtString);

            return jwt;
        } catch (Exception e) {
            return null;
        }
    }
}
