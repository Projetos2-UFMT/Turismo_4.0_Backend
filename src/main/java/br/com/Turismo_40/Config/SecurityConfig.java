package br.com.Turismo_40.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.Turismo_40.Entity.User.Service.AppUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AppUserService userService) throws Exception {
        http
            .userDetailsService(userService)
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas - autenticação
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                
                // Rotas autenticadas - perfil e usuário
                .requestMatchers("/api/auth/profile", "/api/auth/logout").authenticated()
                .requestMatchers("/api/perfil/**").authenticated()
                
                // Rotas autenticadas - entidades principais
                .requestMatchers("/api/atracoes/**").authenticated()
                .requestMatchers("/api/eventos/**").authenticated()
                .requestMatchers("/api/roteiros/**").authenticated()
                
                // Rota de teste protegida
                .requestMatchers("/protected").authenticated()
                
                // Todas as demais requisições requerem autenticação
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}