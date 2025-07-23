package br.com.Turismo_40.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager; // Importe este
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Importe este
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import br.com.Turismo_40.Entity.User.Service.AppUserService; // Assumindo que AppUserService implementa UserDetailsService

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Adicione este Bean para expor o AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AppUserService userService, PasswordEncoder passwordEncoder) throws Exception {
        http
            .userDetailsService(userService) // Configura o UserDetailsService diretamente
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                
                // Rotas protegidas
                .requestMatchers("/api/auth/profile", "/api/auth/logout").authenticated()
                .requestMatchers("/protected").authenticated()
                
                // Qualquer outra rota requer autenticação
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) 
            .headers(headers -> headers.disable()) 
            .formLogin(form -> form.disable())  
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
