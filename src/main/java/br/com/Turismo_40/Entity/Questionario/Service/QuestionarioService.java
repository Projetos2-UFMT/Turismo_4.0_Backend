package br.com.Turismo_40.Entity.Questionario.Service;

import br.com.Turismo_40.Entity.Itinerario.Model.Itinerario;
import br.com.Turismo_40.Entity.Itinerario.Service.ItinerarioService;
import br.com.Turismo_40.Entity.Local.Model.Local;
import br.com.Turismo_40.Entity.Local.Repository.LocalRepository;
import br.com.Turismo_40.Entity.Questionario.Dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionarioService {

    @Autowired
    private ItinerarioService itinerarioService;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private RestTemplate restTemplate;

    // URL do serviço de algoritmo de recomendação (ajustar conforme necessário)
    private final String ALGORITMO_RECOMENDACAO_URL = "http://127.0.0.1:8081/api/recomendar";

    /**
     * Processa as respostas do questionário e retorna recomendações
     */
    public RecomendacaoResponse processarQuestionario(QuestionarioRequest request) {

        // 1. Converter respostas em perfil do usuário
        PerfilUsuario perfil = criarPerfilUsuario(request.getRespostas());

        // Log para verificar as tags geradas
        System.out.println("Tags de preferência do usuário: " + perfil.getTagsPreferencias());

        // 2. Usar o método findByTagsIn para buscar locais que já têm as tags do perfil
        List<Local> locaisComTags = localRepository.findByTagsIn(perfil.getTagsPreferencias());

        // Log do resultado da busca inicial
        System.out.println("Número de locais encontrados com as tags: " + locaisComTags.size());

        // 3. Aplicar filtros mais finos (em memória)
        List<Local> locaisFiltrados = locaisComTags.stream().filter(local -> {
            // Se tem filhos pequenos, evitar ambientes noturnos
            if (perfil.isEvitarAmbienteNoturno() && local.getAmbienteNoturno() != null && local.getAmbienteNoturno()) {
                return false;
            }

            // Se é vegetariano/vegano e é restaurante, verificar opções
            if (perfil.isPrecisaOpcoesVegetarianas() &&
                contemTags(local.getTags(), Arrays.asList("churrasco", "brasileira", "japonesa", "italiana", "fast food")) &&
                (local.getOpcoesVegetarianas() == null || !local.getOpcoesVegetarianas())) {
                return false;
            }

            return true;
        }).collect(Collectors.toList());

        // Log do resultado da filtragem final
        System.out.println("Número de locais após o filtro final: " + locaisFiltrados.size());

        // 4. Enviar para algoritmo de recomendação
        List<LocalRecomendado> locaisRecomendados = enviarParaAlgoritmo(locaisFiltrados, perfil);

        

        // 5. Criar itinerário no banco de dados
        Itinerario itinerarioCriado = null;
        if (request.getUsuarioId() != null && !locaisRecomendados.isEmpty()) {
            try {
                itinerarioCriado = itinerarioService.criarItinerario(
                    request.getUsuarioId(),
                    locaisRecomendados,
                    perfil,
                    "Roteiro Personalizado - " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                );

            } catch (Exception e) {
                System.err.println("Erro ao criar itinerário: " + e.getMessage());
            }
        }

        // 6. Criar resposta
        RecomendacaoResponse response = new RecomendacaoResponse();
        response.setMessage("Recomendações geradas com sucesso!");
        response.setLocaisRecomendados(locaisRecomendados);
        response.setTotalLocais(locaisRecomendados.size());

        if (itinerarioCriado != null) {
            response.setItinerarioGerado("Itinerário criado com ID: " + itinerarioCriado.getId() +
                                         ". Duração estimada: " + itinerarioCriado.getDuracaoEstimada() + " minutos. " +
                                         "Custo estimado: R$ " + String.format("%.2f", itinerarioCriado.getCustoEstimado()));
        }

        return response;
    }

    // ... (restante dos métodos auxiliares, como criarPerfilUsuario, etc.)

    /**
     * Envia os locais filtrados para o algoritmo de recomendação via HTTP.
     */
    private List<LocalRecomendado> enviarParaAlgoritmo(List<Local> locais, PerfilUsuario perfil) {
        try {
            // Preparar os dados para o corpo da requisição
            Map<String, Object> dadosAlgoritmo = new HashMap<>();
            dadosAlgoritmo.put("locais", locais);
            dadosAlgoritmo.put("perfil", perfil);

            // Definir os headers da requisição
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Montar a entidade da requisição (corpo + headers)
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(dadosAlgoritmo, headers);

            // Definir o tipo de retorno esperado, pois é uma lista de objetos
            ParameterizedTypeReference<List<LocalRecomendado>> responseType =
                new ParameterizedTypeReference<List<LocalRecomendado>>() {};

            // Enviar a requisição POST para o microserviço Python
            ResponseEntity<List<LocalRecomendado>> responseEntity = restTemplate.exchange(
                ALGORITMO_RECOMENDACAO_URL,
                HttpMethod.POST,
                requestEntity,
                responseType
            );

            // Retornar a lista de locais recomendados do corpo da resposta
            return responseEntity.getBody();

        } catch (Exception e) {
            System.err.println("Erro ao chamar o serviço de recomendação: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private PerfilUsuario criarPerfilUsuario(List<RespostaQuestionario> respostas) {
        PerfilUsuario perfil = new PerfilUsuario();
        List<String> tags = new ArrayList<>();

        for (RespostaQuestionario resposta : respostas) {
            String pergunta = resposta.getPergunta().toLowerCase();
            String resposta_selecionada = resposta.getRespostaSelecionada();

            // Questão 1: Tipo de comida
            if (pergunta.contains("comida")) {
                perfil.setTipoComida(resposta_selecionada);
                adicionarTagComida(tags, resposta_selecionada);
            }
            // Questão 2: Tipo de evento
            else if (pergunta.contains("evento")) {
                perfil.setTipoEvento(resposta_selecionada);
                adicionarTagEvento(tags, resposta_selecionada);
            }
            // Questão 3: Interesse cultural
            else if (pergunta.contains("cultural")) {
                perfil.setInteresseCultural(resposta_selecionada);
                adicionarTagCultural(tags, resposta_selecionada);
            }
            // Questão 4: Categoria de interesse
            else if (pergunta.contains("interessante")) {
                perfil.setCategoriaInteresse(resposta_selecionada);
                adicionarTagCategoria(tags, resposta_selecionada);
            }
            // Questão 5: Filhos
            else if (pergunta.contains("filhos")) {
                processarFilhos(perfil, resposta_selecionada);
                if (perfil.isTemFilhos()) {
                    tags.add("familia");
                }
            }
            // Questão 6: Nível de animação
            else if (pergunta.contains("animado") || pergunta.contains("calmo")) {
                perfil.setNivelAnimacao(resposta_selecionada);
                adicionarTagAnimacao(tags, resposta_selecionada);
            }
            // Questão 7: Restrições alimentares
            else if (pergunta.contains("carne")) {
                processarRestricoesAlimentares(perfil, resposta_selecionada);
            }
            // Questão 8: Ambiente preferido
            else if (pergunta.contains("ambiente")) {
                perfil.setAmbientePreferido(resposta_selecionada);
                adicionarTagAmbiente(tags, resposta_selecionada);
            }
        }

        // Definir filtros baseados no perfil
        perfil.setAdequadoParaCriancas(perfil.isTemFilhos());
        perfil.setEvitarAmbienteNoturno(perfil.isTemFilhos() && perfil.getQuantidadeFilhos() > 0);
        perfil.setPrecisaOpcoesVegetarianas(perfil.isVegetariano() || perfil.isVegano());

        perfil.setTagsPreferencias(tags.stream().distinct().collect(Collectors.toList()));
        return perfil;
    }

    private boolean contemTags(List<String> tagsLocal, List<String> tagsVerificar) {
        if (tagsLocal == null || tagsLocal.isEmpty()) return false;
        return tagsLocal.stream().anyMatch(tagsVerificar::contains);
    }
    
    // ... (restante dos métodos auxiliares)
    private void adicionarTagComida(List<String> tags, String resposta) {
        switch (resposta.toLowerCase()) {
            case "brasileira": tags.add("brasileira"); break;
            case "churrasco": tags.add("churrasco"); break;
            case "japonesa": tags.add("japonesa"); break;
            case "italiana": tags.add("italiana"); break;
            case "vegana": tags.add("vegana"); break;
        }
    }

    private void adicionarTagEvento(List<String> tags, String resposta) {
        switch (resposta.toLowerCase()) {
            case "parque de diversões": tags.add("parque de diversoes"); break;
            case "bar": tags.add("bar"); tags.add("noturno"); break;
            case "shopping": tags.add("shopping"); break;
            case "eventos culturais": tags.add("cultura"); tags.add("arte"); break;
            case "vida noturna": tags.add("noturno"); tags.add("bar"); break;
        }
    }

    private void adicionarTagCultural(List<String> tags, String resposta) {
        switch (resposta.toLowerCase()) {
            case "museu": tags.add("museu"); tags.add("cultura"); break;
            case "arte": tags.add("arte"); tags.add("cultura"); break;
            case "história": tags.add("historia"); tags.add("cultura"); break;
            case "shows culturais": tags.add("cultura"); tags.add("arte"); break;
        }
    }

    private void adicionarTagCategoria(List<String> tags, String resposta) {
        switch (resposta.toLowerCase()) {
            case "cultura": tags.add("cultura"); tags.add("museu"); tags.add("arte"); break;
            case "natureza": tags.add("natureza"); tags.add("agua"); break;
            case "esporte": tags.add("esporte"); tags.add("fitness"); break;
            case "noturno": tags.add("noturno"); tags.add("bar"); break;
            case "gastronomia": tags.add("brasileira"); tags.add("churrasco"); break;
        }
    }

    private void adicionarTagAnimacao(List<String> tags, String resposta) {
        switch (resposta.toLowerCase()) {
            case "animado": tags.add("parque de diversoes"); tags.add("esporte"); break;
            case "calmo": tags.add("natureza"); tags.add("museu"); break;
            case "mais para animado": tags.add("shopping"); tags.add("cultura"); break;
            case "mais para calmo": tags.add("natureza"); tags.add("historia"); break;
        }
    }

    private void adicionarTagAmbiente(List<String> tags, String resposta) {
        switch (resposta.toLowerCase()) {
            case "natureza": tags.add("natureza"); tags.add("agua"); break;
            case "urbano": tags.add("shopping"); tags.add("bar"); break;
            case "histórico": tags.add("historia"); tags.add("cultura"); break;
            case "cultural": tags.add("cultura"); tags.add("museu"); tags.add("arte"); break;
        }
    }

    private void processarFilhos(PerfilUsuario perfil, String resposta) {
        if (resposta.equalsIgnoreCase("Não")) {
            perfil.setTemFilhos(false);
            perfil.setQuantidadeFilhos(0);
        } else {
            perfil.setTemFilhos(true);
            try {
                if (resposta.contains("+")) {
                    perfil.setQuantidadeFilhos(4);
                } else {
                    perfil.setQuantidadeFilhos(Integer.parseInt(resposta));
                }
            } catch (NumberFormatException e) {
                perfil.setQuantidadeFilhos(1);
            }
        }
    }

    private void processarRestricoesAlimentares(PerfilUsuario perfil, String resposta) {
        switch (resposta.toLowerCase()) {
            case "sou vegetariano":
                perfil.setVegetariano(true);
                perfil.setTemRestricaoAlimentar(true);
                break;
            case "sou vegano":
                perfil.setVegano(true);
                perfil.setTemRestricaoAlimentar(true);
                break;
            case "tenho restrições específicas":
                perfil.setTemRestricaoAlimentar(true);
                break;
            default:
                perfil.setTemRestricaoAlimentar(false);
        }
    }
}