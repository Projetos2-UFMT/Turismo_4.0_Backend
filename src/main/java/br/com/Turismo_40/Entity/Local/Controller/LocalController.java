package br.com.Turismo_40.Entity.Local.Controller;

import br.com.Turismo_40.Entity.Local.Model.Local;
import br.com.Turismo_40.Entity.Local.Repository.LocalRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/locais")
@Tag(name = "Locais", description = "Endpoints para gerenciamento de locais turísticos")
public class LocalController {

    @Autowired
    private LocalRepository localRepository;

    @GetMapping
    @Operation(summary = "Listar todos os locais", 
               description = "Retorna uma lista paginada de todos os locais ativos")
    @ApiResponse(responseCode = "200", description = "Lista de locais retornada com sucesso")
    public ResponseEntity<?> listarLocais(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Local> locais = localRepository.findAllByAtivoTrue();
            
            // Implementar paginação manual se necessário
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), locais.size());
            List<Local> paginatedLocais = locais.subList(start, end);
            
            Map<String, Object> response = new HashMap<>();
            response.put("locais", paginatedLocais);
            response.put("totalElements", locais.size());
            response.put("totalPages", (int) Math.ceil((double) locais.size() / size));
            response.put("currentPage", page);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao listar locais: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter local por ID", 
               description = "Retorna os detalhes de um local específico")
    @ApiResponse(responseCode = "200", description = "Local encontrado")
    @ApiResponse(responseCode = "404", description = "Local não encontrado")
    public ResponseEntity<?> obterLocal(@PathVariable Long id) {
        try {
            Optional<Local> localOpt = localRepository.findById(id);
            if (localOpt.isPresent() && localOpt.get().getAtivo()) {
                return ResponseEntity.ok(localOpt.get());
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Local não encontrado");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar local: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    @PatchMapping("/{id}/imagem")
    public ResponseEntity<?> atualizarImagem(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String urlImagem = body.get("urlImagem");
        if (urlImagem == null || urlImagem.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "urlImagem é obrigatória"));
        }

        Optional<Local> localOpt = localRepository.findById(id);
        if (localOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Local não encontrado"));
        }

        Local local = localOpt.get();
        local.setUrlImagem(urlImagem);
        localRepository.save(local);

        return ResponseEntity.ok(Map.of("message", "Imagem atualizada com sucesso", "id", id, "urlImagem", urlImagem));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar locais por nome", 
               description = "Busca locais que contenham o termo especificado no nome")
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    public ResponseEntity<?> buscarLocaisPorNome(@RequestParam String nome) {
        try {
            List<Local> locais = localRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome);
            
            Map<String, Object> response = new HashMap<>();
            response.put("locais", locais);
            response.put("totalEncontrados", locais.size());
            response.put("termoBusca", nome);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar locais: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Buscar locais por tag", 
               description = "Retorna todos os locais que possuem uma tag específica")
    @ApiResponse(responseCode = "200", description = "Locais encontrados com sucesso")
    public ResponseEntity<?> buscarLocaisPorTag(@PathVariable String tag) {
        try {
            List<Local> locais = localRepository.findByTag(tag);
            
            Map<String, Object> response = new HashMap<>();
            response.put("locais", locais);
            response.put("totalEncontrados", locais.size());
            response.put("tag", tag);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar locais por tag: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/tags")
    @Operation(summary = "Listar todas as tags disponíveis", 
               description = "Retorna uma lista com todas as tags utilizadas pelos locais")
    @ApiResponse(responseCode = "200", description = "Tags listadas com sucesso")
    public ResponseEntity<?> listarTags() {
        try {
            List<String> tags = List.of(
                "cultura", "natureza", "esporte", "noturno", "historia",
                "bar", "museu", "animal", "arte", "shopping", "familia",
                "parque de diversoes", "agua", "churrasco", "brasileira",
                "japonesa", "fast food", "vegana", "italiana", "fitness"
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("tags", tags);
            response.put("totalTags", tags.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao listar tags: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/familia")
    @Operation(summary = "Buscar locais adequados para famílias", 
               description = "Retorna locais marcados como adequados para crianças")
    @ApiResponse(responseCode = "200", description = "Locais familiares encontrados")
    public ResponseEntity<?> buscarLocaisFamilia() {
        try {
            List<Local> locais = localRepository.findByAdequadoCriancasAndAtivoTrue(true);
            
            Map<String, Object> response = new HashMap<>();
            response.put("locais", locais);
            response.put("totalEncontrados", locais.size());
            response.put("filtro", "Adequado para crianças");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar locais para família: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/vegetariano")
    @Operation(summary = "Buscar locais com opções vegetarianas", 
               description = "Retorna locais que oferecem opções vegetarianas/veganas")
    @ApiResponse(responseCode = "200", description = "Locais vegetarianos encontrados")
    public ResponseEntity<?> buscarLocaisVegetarianos() {
        try {
            List<Local> locais = localRepository.findByOpcoesVegetarianasAndAtivoTrue(true);
            
            Map<String, Object> response = new HashMap<>();
            response.put("locais", locais);
            response.put("totalEncontrados", locais.size());
            response.put("filtro", "Com opções vegetarianas");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar locais vegetarianos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/preco")
    @Operation(summary = "Buscar locais por faixa de preço", 
               description = "Retorna locais dentro de uma faixa de preço especificada")
    @ApiResponse(responseCode = "200", description = "Locais encontrados na faixa de preço")
    public ResponseEntity<?> buscarLocaisPorPreco(
            @RequestParam(defaultValue = "0") Double precoMin,
            @RequestParam(defaultValue = "1000") Double precoMax) {
        try {
            List<Local> locais = localRepository.findByPrecoMedioBetweenAndAtivoTrue(precoMin, precoMax);
            
            Map<String, Object> response = new HashMap<>();
            response.put("locais", locais);
            response.put("totalEncontrados", locais.size());
            response.put("precoMin", precoMin);
            response.put("precoMax", precoMax);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar locais por preço: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/avaliacao")
    @Operation(summary = "Buscar locais por avaliação mínima", 
               description = "Retorna locais com avaliação igual ou superior à especificada")
    @ApiResponse(responseCode = "200", description = "Locais bem avaliados encontrados")
    public ResponseEntity<?> buscarLocaisPorAvaliacao(@RequestParam(defaultValue = "4.0") Double avaliacaoMinima) {
        try {
            List<Local> locais = localRepository.findByAvaliacaoMediaGreaterThanEqualAndAtivoTrue(avaliacaoMinima);
            
            Map<String, Object> response = new HashMap<>();
            response.put("locais", locais);
            response.put("totalEncontrados", locais.size());
            response.put("avaliacaoMinima", avaliacaoMinima);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar locais por avaliação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/proximos")
    @Operation(summary = "Buscar locais próximos", 
               description = "Retorna locais próximos às coordenadas especificadas (em km)")
    @ApiResponse(responseCode = "200", description = "Locais próximos encontrados")
    public ResponseEntity<?> buscarLocaisProximos(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10.0") Double distanciaKm) {
        try {
            List<Local> locais = localRepository.findLocaisProximos(latitude, longitude, distanciaKm);
            
            Map<String, Object> response = new HashMap<>();
            response.put("locais", locais);
            response.put("totalEncontrados", locais.size());
            response.put("latitude", latitude);
            response.put("longitude", longitude);
            response.put("distanciaKm", distanciaKm);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar locais próximos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}