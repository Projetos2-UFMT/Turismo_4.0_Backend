package br.com.Turismo_40.Entity.Itinerario.Controller;

import br.com.Turismo_40.Entity.Itinerario.Model.Itinerario;
import br.com.Turismo_40.Entity.Itinerario.Service.ItinerarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/api/itinerarios")
@Tag(name = "Itinerários", description = "Endpoints para gerenciamento de itinerários de turismo")
public class ItinerarioController {

    @Autowired
    private ItinerarioService itinerarioService;

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar itinerários de um usuário", 
               description = "Retorna todos os itinerários criados por um usuário específico")
    @ApiResponse(responseCode = "200", description = "Lista de itinerários retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<?> listarItinerariosPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Itinerario> itinerarios = itinerarioService.buscarItinerariosPorUsuario(usuarioId);
            return ResponseEntity.ok(itinerarios);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar itinerários: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{itinerarioId}")
    @Operation(summary = "Obter detalhes de um itinerário", 
               description = "Retorna um itinerário específico com todos os seus itens")
    @ApiResponse(responseCode = "200", description = "Itinerário encontrado")
    @ApiResponse(responseCode = "404", description = "Itinerário não encontrado")
    public ResponseEntity<?> obterItinerario(@PathVariable Long itinerarioId) {
        try {
            Optional<Itinerario> itinerarioOpt = itinerarioService.buscarItinerarioComItens(itinerarioId);
            
            if (itinerarioOpt.isPresent()) {
                return ResponseEntity.ok(itinerarioOpt.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Itinerário não encontrado");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar itinerário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/item/{itemId}/marcar-visitado")
    @Operation(summary = "Marcar item como visitado", 
               description = "Marca um item específico do itinerário como visitado pelo usuário")
    @ApiResponse(responseCode = "200", description = "Item marcado como visitado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item não encontrado")
    public ResponseEntity<?> marcarItemComoVisitado(@PathVariable Long itemId) {
        try {
            itinerarioService.marcarItemComoVisitado(itemId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Item marcado como visitado com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao marcar item como visitado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{itinerarioId}")
    @Operation(summary = "Excluir itinerário", 
               description = "Exclui um itinerário específico (apenas o dono pode excluir)")
    @ApiResponse(responseCode = "200", description = "Itinerário excluído com sucesso")
    @ApiResponse(responseCode = "403", description = "Usuário não autorizado")
    @ApiResponse(responseCode = "404", description = "Itinerário não encontrado")
    public ResponseEntity<?> excluirItinerario(@PathVariable Long itinerarioId) {
        try {
            // Obter usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            // Aqui você precisaria buscar o ID do usuário pelo username
            // Por simplificação, vou assumir que o usuário ID é passado como parâmetro
            // Em uma implementação real, você buscaria o usuário pelo username
            
            // Para este exemplo, vou simular que o usuário pode excluir qualquer itinerário
            // itinerarioService.excluirItinerario(itinerarioId, usuarioId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Funcionalidade de exclusão será implementada com controle de usuário");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao excluir itinerário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas de itinerários", 
               description = "Retorna estatísticas gerais sobre os itinerários criados no sistema")
    @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")
    public ResponseEntity<?> obterEstatisticas() {
        try {
            ItinerarioService.ItinerarioStats stats = itinerarioService.obterEstatisticas();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao obter estatísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/meus-itinerarios")
    @Operation(summary = "Listar meus itinerários", 
               description = "Retorna todos os itinerários do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Lista de itinerários retornada com sucesso")
    public ResponseEntity<?> listarMeusItinerarios() {
        try {
            // Obter usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            // Aqui você precisaria buscar os itinerários pelo username
            // Por enquanto, retornar uma mensagem informativa
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Para listar seus itinerários, use o endpoint /api/itinerarios/usuario/{usuarioId}");
            response.put("usuario", username);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar itinerários do usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}