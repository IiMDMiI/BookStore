package com.example.demo.security.provider;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo.security.authentication.JwtAuthentication;
import com.example.demo.security.exeption.TokenAuthenticationException;
import com.example.demo.security.user.AuthUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import com.example.demo.security.user.Role;

@Component
public class JwtTokenProvider {

    private static final String ROLES_CLAIM = "roles";
    private final Algorithm signingAlgorithm;

    public JwtTokenProvider(@Value("${jwt.signing-secret}") String signingSecret)
    {
        this.signingAlgorithm = Algorithm.HMAC256(signingSecret);
    }

    public JwtAuthentication resolveJwtToken(String token) throws TokenAuthenticationException{
        try {
            JWTVerifier verifier = JWT.require(signingAlgorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            String userId = decodedJWT.getSubject();
            List<Role> roles = decodedJWT.getClaim(ROLES_CLAIM).asList(Role.class);

            return JwtAuthentication.authenticated(new AuthUser(userId, roles));
        } catch (TokenExpiredException expiredException) {
            throw new TokenAuthenticationException("JWT token has expired");
        } catch (JWTVerificationException verificationException) {
            throw new TokenAuthenticationException("JWT is not valid: " + verificationException.getMessage());}
    }

    public String createJwtToken(AuthUser authUser) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 3600000; // 1 hour validity
        Date exp = new Date(expMillis);

        List<String> roles = authUser.roles().stream().map(Role::name).toList();

        return JWT.create()
                .withSubject(authUser.userId())
                .withClaim(ROLES_CLAIM, roles)
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(signingAlgorithm);
    }
}