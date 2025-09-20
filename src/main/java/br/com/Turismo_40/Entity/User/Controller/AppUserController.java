package br.com.Turismo_40.Entity.User.Controller;

import java.util.HashMap;
import java.util.Map;

import java.util.Optional;
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
import org.springframework.web.bind.annotation.PatchMapping;
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
// Define o prefixo base para todas as rotas do controlador
@RequestMapping("/api/auth")
public class AppUserController {

    // Injeção do serviço de usuário para operações de criação e busca
    @Autowired
    private AppUserService userService;
    
    // Injeção do gerenciador de autenticação para validar credenciais
    @Autowired
    private AuthenticationManager authenticationManager;

    // Injeção da utilidade JWT para geração de tokens
    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint para registro de novos usuários
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AppUserRegistrationRequest request) {
        try {
            // Cria um novo usuário com base no nome de usuário e senha fornecidos
            AppUser user = userService.createUser(request.getUsername(), request.getPassword());
            
            // Prepara a resposta com os dados do usuário registrado
            AppUserResponse response = new AppUserResponse();
            response.setMessage("Usuário registrado com sucesso!");
            response.setUsername(user.getUsername());
            response.setId(user.getId());
            
            // Retorna resposta com status 201 (Created)
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            // Trata erros, como usuário já existente, retornando status 400 (Bad Request)
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

@PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody AppUserLoginRequest request) {
    try {
        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        // Define a autenticação no contexto de segurança
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Obtém os detalhes do usuário autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // BUSCA A ENTIDADE COMPLETA DO USUÁRIO PARA OBTER O ID
        Optional<AppUser> userOpt = userService.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuário não encontrado após autenticação.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        
        AppUser user = userOpt.get();
        
        // Gera um token JWT para o usuário autenticado
        String token = jwtUtil.generateToken(userDetails);
        
        // Prepara a resposta com os dados do login, incluindo o ID do usuário
        AppUserResponse response = new AppUserResponse();
        response.setMessage("Login realizado com sucesso!");
        response.setUsername(user.getUsername());
        response.setId(user.getId()); // AGORA INCLUI O ID
        response.setAuthenticated(true);
        response.setToken(token);
        
        // Retorna resposta com status 200 (OK)
        return ResponseEntity.ok(response);
        
    } catch (BadCredentialsException e) {
        // Trata erro de credenciais inválidas, retornando status 401 (Unauthorized)
        Map<String, String> error = new HashMap<>();
        error.put("error", "Credenciais inválidas.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    } catch (Exception e) {
        // Trata outros erros de autenticação, retornando status 401 (Unauthorized)
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erro de autenticação: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}

// NOVO ENDPOINT: Obter dados do usuário autenticado via token
@GetMapping("/me")
public ResponseEntity<?> getCurrentUser() {
    try {
        // Obtém o contexto de autenticação atual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Verifica se o usuário está autenticado
        if (authentication == null || !authentication.isAuthenticated() || 
            authentication.getName().equals("anonymousUser")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuário não autenticado.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        // Obtém o nome de usuário do token
        String username = authentication.getName();
        
        // Busca o usuário completo no banco
        Optional<AppUser> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuário não encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        AppUser user = userOpt.get();
        
        // Prepara a resposta com os dados do usuário
        AppUserResponse response = new AppUserResponse();
        response.setMessage("Dados do usuário obtidos com sucesso!");
        response.setUsername(user.getUsername());
        response.setId(user.getId());
        response.setAuthenticated(true);
        // Não inclui o token aqui pois já foi fornecido
        
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erro ao obter dados do usuário: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

    @PatchMapping("/editarSenha")
    public ResponseEntity<?> editarSenha(@RequestBody AppUserRegistrationRequest request) {
        try {
            // Obtém o nome de usuário do contexto de segurança
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            // Atualiza a senha do usuário autenticado
            AppUser updatedUser = userService.updatePassword(currentUsername, request.getPassword());

            // Prepara a resposta com os dados do usuário atualizado
            AppUserResponse response = new AppUserResponse();
            response.setMessage("Senha atualizada com sucesso!");
            response.setUsername(updatedUser.getUsername());
            response.setId(updatedUser.getId());

            // Retorna resposta com status 200 (OK)
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Trata erros, retornando status 400 (Bad Request)
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

}

// Explicação:
// Este controlador REST gerencia as operações de autenticação e perfil de usuários.
// Ele permite registrar novos usuários, autenticar usuários existentes, realizar logout e obter o perfil do usuário autenticado.
// As respostas são formatadas em objetos AppUserResponse, que incluem mensagens e dados relevantes.
// O controlador utiliza o AppUserService para interagir com os dados dos usuários e o JwtUtil para gerar tokens JWT durante o login.
// estejam autorizados a acessar recursos protegidos.
