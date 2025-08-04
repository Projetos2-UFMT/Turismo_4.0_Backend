package br.com.Turismo_40.Entity.AtracaoTuristica.Controller;

import br.com.Turismo_40.Entity.AtracaoTuristica.Dto.AtracaoTuristicaRequest;
import br.com.Turismo_40.Entity.AtracaoTuristica.Dto.AtracaoTuristicaResponse;
import br.com.Turismo_40.Entity.AtracaoTuristica.Model.AtracaoTuristica;
import br.com.Turismo_40.Entity.AtracaoTuristica.Service.AtracaoTuristicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/atracoes")
public class AtracaoTuristicaController {

    @Autowired
    private AtracaoTuristicaService atracaoService;

    @PostMapping("/criar")
    public ResponseEntity<?> criarAtracao(@RequestBody AtracaoTuristicaRequest request) {
        try {
            AtracaoTuristica atracao = atracaoService.criarAtracao(
                request.getCidade(),
                request.getNome(),
                request.getCategoria(),
                request.getDescricao(),
                request.getCusto(),
                request.getHorarioFuncionamento(),
                request.getAmbiente(),
                request.getLatitude(),
                request.getLongitude()
            );

            AtracaoTuristicaResponse response = convertToResponse(atracao);
            response.setMessage("Atração turística criada com sucesso!");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao criar atração: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<?> listarAtracoesPorCidade(@PathVariable String cidade) {
        try {
            List<AtracaoTuristica> atracoes = atracaoService.listarAtracoesPorCidade(cidade);
            List<AtracaoTuristicaResponse> responses = atracoes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar atrações: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/cidade/{cidade}/categoria/{categoria}")
    public ResponseEntity<?> listarAtracoesPorCategoria(@PathVariable String cidade, 
                                                       @PathVariable AtracaoTuristica.Categoria categoria) {
        try {
            List<AtracaoTuristica> atracoes = atracaoService.buscarAtracoesPorCategoria(cidade, categoria);
            List<AtracaoTuristicaResponse> responses = atracoes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar atrações por categoria: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{atracaoId}")
    public ResponseEntity<?> buscarAtracaoPorId(@PathVariable Long atracaoId) {
        try {
            Optional<AtracaoTuristica> atracaoOpt = atracaoService.buscarAtracaoPorId(atracaoId);
            
            if (atracaoOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Atração não encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            AtracaoTuristicaResponse response = convertToResponse(atracaoOpt.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar atração: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{atracaoId}")
    public ResponseEntity<?> deletarAtracao(@PathVariable Long atracaoId) {
        try {
            atracaoService.deletarAtracao(atracaoId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Atração deletada com sucesso!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao deletar atração: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private AtracaoTuristicaResponse convertToResponse(AtracaoTuristica atracao) {
        AtracaoTuristicaResponse response = new AtracaoTuristicaResponse();
        response.setAtracaoId(atracao.getAtracaoId());
        response.setCidade(atracao.getCidade());
        response.setNome(atracao.getNome());
        response.setCategoria(atracao.getCategoria());
        response.setDescricao(atracao.getDescricao());
        response.setCusto(atracao.getCusto());
        response.setHorarioFuncionamento(atracao.getHorarioFuncionamento());
        response.setAmbiente(atracao.getAmbiente());
        response.setLatitude(atracao.getLatitude());
        response.setLongitude(atracao.getLongitude());
        return response;
    }
}
