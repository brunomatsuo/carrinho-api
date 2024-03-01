package br.com.carrinhosapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public DecodedJWT validateToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");

        if (token != null) {
            try {
                Algorithm algorithm = Algorithm.HMAC256(secret);
                DecodedJWT jwt = JWT.require(algorithm)
                        .withIssuer("auth-api")
                        .build()
                        .verify(token.replace("Bearer ", ""));

                return jwt;
            }
            catch (JWTVerificationException exception) {
                return null;
            }
        }
        return null;
    }

    public String getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token.replace("Bearer ", ""));
            return jwt.getKeyId();
        }
        catch (JWTVerificationException exception) {
            return null;
        }
    }

}
