package br.com.Turismo_40.Entity.Roteiro.Controller;

import br.com.Turismo_40.Entity.Roteiro.Dto.RoteiroRequest;
import br.com.Turismo_40.Entity.Roteiro.Dto.RoteiroResponse;
import br.com.Turismo_40.Entity.Roteiro.Model.Roteiro;
import br.com.Turismo_40.Entity.Roteiro.Service.RoteiroService;
import br.com.Turismo_40.Entity.User.Model.AppUser;
import br.com.Turismo_40.Entity.User.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roteiros")
public class RoteiroController {

    @Autowired
    private RoteiroService roteiroService;

    @Autowired
    private AppUserService userService;

    @PostMapping("/criar")
    public ResponseEntity<?> criarRoteiro(@RequestBody RoteiroRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Optional<AppUser> userOpt = userService.findByUsername(username);

            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuário não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            // Nova lógica: prepara o prompt para a IA
            String promptParaIA = roteiroService.prepararPromptParaIA(
                userOpt.get().getId(),
                request.getCidade(),
                request.getTempoDisponivel(),
                request.getHorarioPreferido(),
                request.getOrcamento(),
                request.getModoTransporte(),
                request.getPreferenciaAmbiente(),
                request.getIncluirEventosSazonais()
            );

            // Resposta para demonstrar que o prompt foi gerado com sucesso
            Map<String, String> response = new HashMap<>();
            response.put("message", "Dados do usuário e do perfil foram coletados e o prompt foi preparado para a IA.");
            response.put("prompt_para_ia", promptParaIA);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao preparar roteiro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/meus-roteiros")
    public ResponseEntity<?> listarMeusRoteiros() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Optional<AppUser> userOpt = userService.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuário não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            List<Roteiro> roteiros = roteiroService.listarRoteirosPorUsuario(userOpt.get().getId());
            List<RoteiroResponse> responses = roteiros.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar roteiros: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{roteiroId}")
    public ResponseEntity<?> deletarRoteiro(@PathVariable Long roteiroId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Optional<AppUser> userOpt = userService.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuário não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            Optional<Roteiro> roteiroOpt = roteiroService.buscarRoteiroPorId(roteiroId);
            
            if (roteiroOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Roteiro não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            Roteiro roteiro = roteiroOpt.get();
            
            // Verificar se o roteiro pertence ao usuário autenticado
            if (!roteiro.getUserId().equals(userOpt.get().getId())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Acesso negado");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            roteiroService.deletarRoteiro(roteiroId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Roteiro deletado com sucesso!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao deletar roteiro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private RoteiroResponse convertToResponse(Roteiro roteiro) {
        RoteiroResponse response = new RoteiroResponse();
        response.setRoteiroId(roteiro.getRoteiroId());
        response.setUserId(roteiro.getUserId());
        response.setCidade(roteiro.getCidade());
        response.setTempoDisponivel(roteiro.getTempoDisponivel());
        response.setHorarioPreferido(roteiro.getHorarioPreferido());
        response.setOrcamento(roteiro.getOrcamento());
        response.setModoTransporte(roteiro.getModoTransporte());
        response.setPreferenciaAmbiente(roteiro.getPreferenciaAmbiente());
        response.setIncluirEventosSazonais(roteiro.getIncluirEventosSazonais());
        response.setCriadoEm(roteiro.getCriadoEm());
        return response;
    }
}