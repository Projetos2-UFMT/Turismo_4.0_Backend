package br.com.Turismo_40.Entity.User.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.Turismo_40.Config.JwtUtil;
import br.com.Turismo_40.Entity.User.Dto.AppUserLoginRequest;
import br.com.Turismo_40.Entity.User.Dto.AppUserRegistrationRequest;
import br.com.Turismo_40.Entity.User.Dto.AppUserResponse;
import br.com.Turismo_40.Entity.User.Model.AppUser;
import br.com.Turismo_40.Entity.User.Service.AppUserService;

@RestController
@RequestMapping("/api/auth")
public class AppUserController {

    @Autowired
    private AppUserService userService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AppUserRegistrationRequest request) {
        try {
            AppUser user = userService.createUser(request.getUsername(), request.getPassword());
            
            AppUserResponse response = new AppUserResponse();
            response.setMessage("Usuário registrado com sucesso!"); // Mensagem mais amigável
            response.setUsername(user.getUsername());
            response.setId(user.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AppUserLoginRequest request) {
        try {
            // Tenta autenticar o usuário
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            // Define a autenticação no contexto de segurança (opcional para APIs stateless, mas não prejudica)
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Obtém os detalhes do usuário autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Gera o token JWT
            String token = jwtUtil.generateToken(userDetails);
            
            // Prepara a resposta com o token
            AppUserResponse response = new AppUserResponse();
            response.setMessage("Login realizado com sucesso!");
            response.setUsername(request.getUsername());
            response.setAuthenticated(true);
            response.setToken(token); // <-- Define o token na resposta
            
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciais inválidas.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) { // Captura outras exceções genéricas de autenticação
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro de autenticação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Para APIs stateless (com JWT), o logout no servidor geralmente significa apenas limpar o contexto.
        // O cliente é responsável por descartar o token JWT.
        SecurityContextHolder.clearContext();
        
        AppUserResponse response = new AppUserResponse();
        response.setMessage("Logout realizado com sucesso.");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Verifica se o usuário está autenticado e não é anônimo
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuário não autenticado.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        String username = authentication.getName();
        AppUser user = userService.findByUsername(username).orElse(null);
        
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuário não encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        AppUserResponse response = new AppUserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setMessage("Perfil do usuário recuperado com sucesso."); // Mensagem de sucesso
        
        return ResponseEntity.ok(response);
    }
}
