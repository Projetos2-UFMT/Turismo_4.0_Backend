package br.com.Turismo_40.Entity.Questionario.Controller;

import br.com.Turismo_40.Entity.Questionario.Dto.QuestionarioRequest;
import br.com.Turismo_40.Entity.Questionario.Dto.RecomendacaoResponse;
import br.com.Turismo_40.Entity.Questionario.Service.QuestionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/questionario")
@Tag(name = "Questionário", description = "Endpoints para processamento do questionário de recomendações")
public class QuestionarioController {

    @Autowired
    private QuestionarioService questionarioService;

    @PostMapping("/processar")
    @Operation(summary = "Processar questionário de recomendações", 
               description = "Recebe as respostas do questionário, aplica filtros inteligentes e retorna recomendações personalizadas")
    @ApiResponse(responseCode = "200", description = "Recomendações geradas com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados do questionário inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<?> processarQuestionario(@RequestBody QuestionarioRequest request) {
        try {
            // Validar se o request contém dados
            if (request.getRespostas() == null || request.getRespostas().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Nenhuma resposta fornecida no questionário");
                return ResponseEntity.badRequest().body(error);
            }

            // Processar questionário e gerar recomendações
            RecomendacaoResponse recomendacoes = questionarioService.processarQuestionario(request);
            
            return ResponseEntity.ok(recomendacoes);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao processar questionário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}