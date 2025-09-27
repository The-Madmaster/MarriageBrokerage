package com.mahi.marriagebrokerage.security;

import com.mahi.marriagebrokerage.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
    // Builder for MVC patterns (DispatcherServlet based endpoints)
    MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector).servletPath("/");

    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> {})
        .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authz -> authz
            // Public / authentication endpoints (Spring MVC)
            .requestMatchers(mvc.pattern("/api/auth/**")).permitAll()
            .requestMatchers(mvc.pattern("/api/public/**")).permitAll()
            // H2 console served by its own servlet -> use AntPathRequestMatcher
            .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
            // Role secured endpoints
            .requestMatchers(mvc.pattern("/api/admin/**")).hasRole("ADMIN")
            .requestMatchers(mvc.pattern("/api/broker/**")).hasAnyRole("ADMIN", "BROKER")
            .requestMatchers(mvc.pattern("/api/client/**")).hasAnyRole("ADMIN", "BROKER", "CLIENT")
            .anyRequest().authenticated()
        );

    // Allow H2 console frames (same-origin) & disable frame options for console usage
    http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }
}