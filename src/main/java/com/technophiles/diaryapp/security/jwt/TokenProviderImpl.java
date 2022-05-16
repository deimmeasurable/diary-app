package com.technophiles.diaryapp.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.technophiles.diaryapp.security.jwt.SecurityConstants.AUTHORITIES_KEY;
import static com.technophiles.diaryapp.security.jwt.SecurityConstants.SIGNING_KEY;


@Component
@Slf4j
public class TokenProviderImpl implements TokenProvider{


    private final static Long TOKEN_VALIDITY_PERIOD = (long) (24 * 10 * 3600);

//    private String SIGNING_KEY = System.getenv("SECRET_KEY");
//    private String AUTHORITIES_KEY = System.getenv("AUTHORITIES_KEY");

    @Override
    public String getUsernameFromJWTToken(String token) {
        return getClaimFromJWTToken(token, Claims::getSubject);
    }

    @Override
    public Date getExpirationDateFromJWTToken(String token) {
        return getClaimFromJWTToken(token, Claims::getExpiration);
    }

    @Override
    public <T> T getClaimFromJWTToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromJWTToken(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Header<?> getHeaderFromJWTToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getHeader();
    }

    @Override
    public Claims getAllClaimsFromJWTToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Boolean isJWTTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromJWTToken(token);
        return expirationDate.before(new Date());
    }

    @Override
    public String generateJWTToken(Authentication authentication) {
        log.info("Signing key -> {}", SIGNING_KEY);
        log.info("Authorities key -> {}", AUTHORITIES_KEY);
        log.info("Authentication name --> {}",authentication.getName());
        String jwts=  Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authentication.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_PERIOD))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
        log.info("Jwts -->{}", jwts);
        return jwts;
    }

    @Override
    public Boolean validateJWTToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromJWTToken(token);
        return (username.equals(userDetails.getUsername()) && !isJWTTokenExpired(token));
    }

    @Override
    public UsernamePasswordAuthenticationToken getAuthenticationToken(String token, Authentication existingAuth, UserDetails userDetails) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(SIGNING_KEY);
        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
        log.info("Authorities her --> {}", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
