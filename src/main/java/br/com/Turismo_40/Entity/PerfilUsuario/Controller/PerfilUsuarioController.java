package br.com.Turismo_40.Entity.PerfilUsuario.Controller;

import br.com.Turismo_40.Entity.PerfilUsuario.Dto.PerfilUsuarioRequest;
import br.com.Turismo_40.Entity.PerfilUsuario.Dto.PerfilUsuarioResponse;
import br.com.Turismo_40.Entity.PerfilUsuario.Model.PerfilUsuario;
import br.com.Turismo_40.Entity.PerfilUsuario.Service.PerfilUsuarioService;
import br.com.Turismo_40.Entity.User.Model.AppUser;
import br.com.Turismo_40.Entity.User.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/perfil")
public class PerfilUsuarioController {

    @Autowired
    private PerfilUsuarioService perfilService;

    @Autowired
    private AppUserService userService;

    @PostMapping("/criar")
    public ResponseEntity<?> criarPerfil(@RequestBody PerfilUsuarioRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Optional<AppUser> userOpt = userService.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuário não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            PerfilUsuario perfil = perfilService.criarPerfil(
                userOpt.get().getId(),
                request.getEstilo(),
                request.getContextoViagem(),
                request.getInteresses(),
                request.getRestricoes()
            );

            PerfilUsuarioResponse response = new PerfilUsuarioResponse();
            response.setUserId(perfil.getUserId());
            response.setEstilo(perfil.getEstilo());
            response.setContextoViagem(perfil.getContextoViagem());
            response.setInteresses(perfil.getInteresses());
            response.setRestricoes(perfil.getRestricoes());
            response.setMessage("Perfil criado com sucesso!");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarPerfil(@RequestBody PerfilUsuarioRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Optional<AppUser> userOpt = userService.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuário não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            PerfilUsuario perfil = perfilService.atualizarPerfil(
                userOpt.get().getId(),
                request.getEstilo(),
                request.getContextoViagem(),
                request.getInteresses(),
                request.getRestricoes()
            );

            PerfilUsuarioResponse response = new PerfilUsuarioResponse();
            response.setUserId(perfil.getUserId());
            response.setEstilo(perfil.getEstilo());
            response.setContextoViagem(perfil.getContextoViagem());
            response.setInteresses(perfil.getInteresses());
            response.setRestricoes(perfil.getRestricoes());
            response.setMessage("Perfil atualizado com sucesso!");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/meu-perfil")
    public ResponseEntity<?> buscarMeuPerfil() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Optional<AppUser> userOpt = userService.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuário não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            Optional<PerfilUsuario> perfilOpt = perfilService.buscarPerfilPorUserId(userOpt.get().getId());
            
            if (perfilOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Perfil não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            PerfilUsuario perfil = perfilOpt.get();
            PerfilUsuarioResponse response = new PerfilUsuarioResponse();
            response.setUserId(perfil.getUserId());
            response.setEstilo(perfil.getEstilo());
            response.setContextoViagem(perfil.getContextoViagem());
            response.setInteresses(perfil.getInteresses());
            response.setRestricoes(perfil.getRestricoes());
            response.setMessage("Perfil encontrado com sucesso!");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar perfil: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}