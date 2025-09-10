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

    @GetMapping("/exemplo")
    @Operation(summary = "Obter exemplo de questionário", 
               description = "Retorna um exemplo de como estruturar o JSON do questionário")
    @ApiResponse(responseCode = "200", description = "Exemplo retornado com sucesso")
    public ResponseEntity<?> obterExemploQuestionario() {
        Map<String, Object> exemplo = new HashMap<>();
        
        // Exemplo de estrutura esperada
        Map<String, Object> exemploRequest = new HashMap<>();
        exemploRequest.put("usuarioId", 1);
        
        Map<String, String>[] respostasExemplo = new Map[8];
        
        respostasExemplo[0] = Map.of(
            "pergunta", "Questão 1: Qual seu tipo preferido de comida?",
            "respostaSelecionada", "Churrasco"
        );
        
        respostasExemplo[1] = Map.of(
            "pergunta", "Questão 2: Qual evento você prefere?",
            "respostaSelecionada", "Shopping"
        );
        
        respostasExemplo[2] = Map.of(
            "pergunta", "Questão 3: Você gosta de saídas culturais?",
            "respostaSelecionada", "Museu"
        );
        
        respostasExemplo[3] = Map.of(
            "pergunta", "Questão 4: Qual você acha mais interessante?",
            "respostaSelecionada", "Cultura"
        );
        
        respostasExemplo[4] = Map.of(
            "pergunta", "Questão 5: Você Tem filhos?",
            "respostaSelecionada", "2"
        );
        
        respostasExemplo[5] = Map.of(
            "pergunta", "Questão 6: Algo mais animado ou calmo?",
            "respostaSelecionada", "Mais para calmo"
        );
        
        respostasExemplo[6] = Map.of(
            "pergunta", "Questão 7: Você pode comer carne?",
            "respostaSelecionada", "Sim, sem restrições"
        );
        
        respostasExemplo[7] = Map.of(
            "pergunta", "Questão 8: Qual ambiente você prefere?",
            "respostaSelecionada", "Cultural"
        );
        
        exemploRequest.put("respostas", respostasExemplo);
        exemplo.put("exemploRequest", exemploRequest);
        exemplo.put("descricao", "Exemplo de JSON para enviar ao endpoint /api/questionario/processar");
        
        return ResponseEntity.ok(exemplo);
    }
}