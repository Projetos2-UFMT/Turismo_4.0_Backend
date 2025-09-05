package br.com.Turismo_40.Entity.Roteiro.Controller;

import br.com.Turismo_40.Entity.PerfilUsuario.Dto.PerfilUsuarioRequest;
import br.com.Turismo_40.Entity.PerfilUsuario.Model.PerfilUsuario;
import br.com.Turismo_40.Entity.PerfilUsuario.Service.PerfilUsuarioService;
import br.com.Turismo_40.Entity.Roteiro.Dto.RoteiroRequest;
import br.com.Turismo_40.Entity.Roteiro.Dto.RoteiroResponse;
import br.com.Turismo_40.Entity.Roteiro.Dto.SurveyRequest;
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
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private PerfilUsuarioService perfilService;
    
    // URL da sua API de recomendação em Python
    private static final String RECOMMENDATION_API_URL = "http://localhost:5000/api/generate-itinerary";

    // Mapeamento de respostas da pesquisa para enums do PerfilUsuario
    private PerfilUsuario.Estilo mapComidaToEstilo(String resposta) {
        if ("Brasileira".equalsIgnoreCase(resposta) || "Churrasco".equalsIgnoreCase(resposta) || "Japonesa".equalsIgnoreCase(resposta) || "Italiana".equalsIgnoreCase(resposta) || "Vegana".equalsIgnoreCase(resposta)) {
            return PerfilUsuario.Estilo.GASTRONOMICO;
        }
        return PerfilUsuario.Estilo.OUTRO;
    }

    private PerfilUsuario.Estilo mapInteresseToEstilo(String resposta) {
        switch (resposta.toLowerCase()) {
            case "cultura":
            case "história":
            case "shows culturais":
                return PerfilUsuario.Estilo.CULTURAL;
            case "natureza":
                return PerfilUsuario.Estilo.NATUREZA;
            case "esporte":
                return PerfilUsuario.Estilo.AVENTURA;
            case "gastronomia":
                return PerfilUsuario.Estilo.GASTRONOMICO;
            default:
                return PerfilUsuario.Estilo.OUTRO;
        }
    }

    private PerfilUsuario.ContextoViagem mapFilhosToContexto(String resposta) {
        if ("Não".equalsIgnoreCase(resposta)) {
            return PerfilUsuario.ContextoViagem.SOLO;
        } else {
            return PerfilUsuario.ContextoViagem.FAMILIA_COM_CRIANCAS;
        }
    }

    private Roteiro.PreferenciaAmbiente mapPreferenciaAmbiente(String resposta) {
        if ("Natureza".equalsIgnoreCase(resposta)) {
            return Roteiro.PreferenciaAmbiente.EXTERNO;
        } else if ("Urbano".equalsIgnoreCase(resposta) || "Histórico".equalsIgnoreCase(resposta) || "Cultural".equalsIgnoreCase(resposta) || "Diversificado".equalsIgnoreCase(resposta)) {
            return Roteiro.PreferenciaAmbiente.AMBOS;
        }
        return Roteiro.PreferenciaAmbiente.AMBOS;
    }

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

    @PostMapping("/gerar-roteiro-por-pesquisa")
    public ResponseEntity<?> gerarRoteiroPorPesquisa(@RequestBody SurveyRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Optional<AppUser> userOpt = userService.findByUsername(username);

            if (userOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuário não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            PerfilUsuarioRequest perfilRequest = new PerfilUsuarioRequest();

            for (SurveyRequest.QuestionResponse qr : request.getRespostas()) {
                String pergunta = qr.getPergunta();
                String resposta = qr.getResposta();

                if (pergunta != null && resposta != null) {
                    if (pergunta.contains("tipo preferido de comida")) {
                        perfilRequest.setEstilo(mapComidaToEstilo(resposta));
                    } else if (pergunta.contains("saídas culturais")) {
                        perfilRequest.setInteresses(resposta);
                    } else if (pergunta.contains("mais interessante")) {
                        perfilRequest.setEstilo(mapInteresseToEstilo(resposta));
                    } else if (pergunta.contains("Tem filhos")) {
                        perfilRequest.setContextoViagem(mapFilhosToContexto(resposta));
                    } else if (pergunta.contains("pode comer carne")) {
                        perfilRequest.setRestricoes(resposta);
                    }
                }
            }
            
            perfilService.buscarPerfilPorUserId(userOpt.get().getId()).ifPresentOrElse(
                    perfil -> perfilService.atualizarPerfil(userOpt.get().getId(), perfilRequest.getEstilo(), perfilRequest.getContextoViagem(), perfilRequest.getInteresses(), perfilRequest.getRestricoes()),
                    () -> perfilService.criarPerfil(userOpt.get().getId(), perfilRequest.getEstilo(), perfilRequest.getContextoViagem(), perfilRequest.getInteresses(), perfilRequest.getRestricoes())
            );

            // Objeto para enviar para a API de recomendação
            Map<String, Object> recommendationPayload = new HashMap<>();
            recommendationPayload.put("perfil_usuario", perfilService.buscarPerfilPorUserId(userOpt.get().getId()).get());
            recommendationPayload.put("roteiro_request", request);

            // Chama a API de recomendação externa
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> recommendationResponse = restTemplate.postForEntity(
                    RECOMMENDATION_API_URL,
                    recommendationPayload,
                    Map.class
            );

            if (recommendationResponse.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        Map.of("error", "Erro ao chamar o motor de recomendação externo.")
                );
            }

            Map<String, Object> responseBody = recommendationResponse.getBody();

            // Salva o roteiro retornado no banco de dados e retorna a resposta
            // Você precisará de um método no seu RoteiroService para salvar o itinerário
            // Roteiro novoRoteiro = roteiroService.salvarItinerarioDoPython(responseBody);
            
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "Itinerário gerado com sucesso.");
            successResponse.put("itinerario_sugerido", responseBody.get("itinerario"));

            return ResponseEntity.status(HttpStatus.OK).body(successResponse);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao processar a pesquisa: " + e.getMessage());
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