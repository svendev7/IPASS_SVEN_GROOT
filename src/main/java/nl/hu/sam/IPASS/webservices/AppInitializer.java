package nl.hu.sam.IPASS.webservices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import nl.hu.sam.IPASS.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.Optional;

public class AppInitializer implements Filter {
    private static final Key SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    @Override
    public void init(FilterConfig filterConfig) {
        // Initialization logic, if needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Extract the token from the request header
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Remove the "Bearer " prefix from the token
        token = token.substring(7);

        // Validate the token
        if (!validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Extract the user information from the token
        Optional<User> userOptional = extractUserFromToken(token);
        if (userOptional.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User user = userOptional.get();

        // Pass the user information to the protected resource or endpoint
        request.setAttribute("user", user);

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup logic, if needed
    }

    private boolean validateToken(String token) {
        try {
            // Validate the token's signature and expiration
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            // Perform additional checks if necessary

            return true; // Token is valid
        } catch (Exception e) {
            return false; // Token is invalid
        }
    }

    private Optional<User> extractUserFromToken(String token) {
        try {
            // Extract user information from the token's payload
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Search for the user in your list of allUsers using the token or any other identifier
            List<User> allUsers = User.getAllUsers();
            String username = claims.getSubject(); // Assuming the username is stored as the subject in the token
            Optional<User> userOptional = allUsers.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst();

            return userOptional;
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}