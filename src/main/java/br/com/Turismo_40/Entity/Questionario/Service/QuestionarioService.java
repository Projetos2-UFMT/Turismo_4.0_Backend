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
    private final String ALGORITMO_RECOMENDACAO_URL = "http://host.docker.internal:8081/api/recomendar";

    // Opções fixas do questionário (deve bater com o frontend)
    private static final List<List<String>> OPCOES_QUESTIONARIO = Arrays.asList(
        Arrays.asList("Brasileira", "Churrasco", "Japonesa", "Italiana", "Vegana"),
        Arrays.asList("Parque de diversões", "Bar", "Shopping", "Eventos culturais", "Vida noturna"),
        Arrays.asList("Museu", "Arte", "História", "Shows culturais", "Não tenho interesse"),
        Arrays.asList("Cultura", "Natureza", "Esporte", "Noturno", "Gastronomia"),
        Arrays.asList("1", "2", "3", "4 ou +", "Não"),
        Arrays.asList("Animado", "Calmo", "Depende do dia", "Mais para animado", "Mais para calmo"),
        Arrays.asList("Sim, sem restrições", "Sou vegetariano", "Sou vegano", "Apenas frango/peixe", "Tenho restrições específicas"),
        Arrays.asList("Natureza", "Urbano", "Histórico", "Cultural", "Diversificado")
    );

    /**
     * Processa as respostas do questionário e retorna recomendações
     */
    public RecomendacaoResponse processarQuestionario(QuestionarioRequest request) {

        // 1. Converter respostas em perfil do usuário
        PerfilUsuario perfil = criarPerfilUsuario(request);

        // Log para verificar as tags geradas
        System.out.println("Tags de preferência do usuário: " + perfil.getTagsPreferencias());

        // 2. Buscar locais pelas tags do perfil
        List<Local> locaisComTags = localRepository.findByTagsIn(perfil.getTagsPreferencias());
        System.out.println("Número de locais encontrados com as tags: " + locaisComTags.size());

        // 3. Filtros adicionais
        List<Local> locaisFiltrados = locaisComTags.stream().filter(local -> {
            if (perfil.isEvitarAmbienteNoturno() && Boolean.TRUE.equals(local.getAmbienteNoturno())) {
                return false;
            }
            if (perfil.isPrecisaOpcoesVegetarianas() &&
                contemTags(local.getTags(), Arrays.asList("churrasco", "brasileira", "japonesa", "italiana", "fast food")) &&
                (local.getOpcoesVegetarianas() == null || !local.getOpcoesVegetarianas())) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());

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

    /**
     * Converte os índices das respostas em um perfil de usuário e tags.
     */
    private PerfilUsuario criarPerfilUsuario(QuestionarioRequest request) {
        PerfilUsuario perfil = new PerfilUsuario();
        List<String> tags = new ArrayList<>();
        List<Integer> respostas = request.getRespostas();

        // Garantir que o array de respostas tem o tamanho esperado
        if (respostas == null || respostas.size() < 8) {
            throw new IllegalArgumentException("Respostas do questionário incompletas.");
        }

        // Questão 1: Tipo de comida
        String comida = OPCOES_QUESTIONARIO.get(0).get(respostas.get(0));
        perfil.setTipoComida(comida);
        adicionarTagComida(tags, comida);

        // Questão 2: Tipo de evento
        String evento = OPCOES_QUESTIONARIO.get(1).get(respostas.get(1));
        perfil.setTipoEvento(evento);
        adicionarTagEvento(tags, evento);

        // Questão 3: Interesse cultural
        String cultural = OPCOES_QUESTIONARIO.get(2).get(respostas.get(2));
        perfil.setInteresseCultural(cultural);
        adicionarTagCultural(tags, cultural);

        // Questão 4: Categoria de interesse
        String categoria = OPCOES_QUESTIONARIO.get(3).get(respostas.get(3));
        perfil.setCategoriaInteresse(categoria);
        adicionarTagCategoria(tags, categoria);

        // Questão 5: Filhos
        String filhos = OPCOES_QUESTIONARIO.get(4).get(respostas.get(4));
        processarFilhos(perfil, filhos);
        if (perfil.isTemFilhos()) tags.add("familia");

        // Questão 6: Nível de animação
        String animacao = OPCOES_QUESTIONARIO.get(5).get(respostas.get(5));
        perfil.setNivelAnimacao(animacao);
        adicionarTagAnimacao(tags, animacao);

        // Questão 7: Restrições alimentares
        String restricao = OPCOES_QUESTIONARIO.get(6).get(respostas.get(6));
        processarRestricoesAlimentares(perfil, restricao);

        // Questão 8: Ambiente preferido
        String ambiente = OPCOES_QUESTIONARIO.get(7).get(respostas.get(7));
        perfil.setAmbientePreferido(ambiente);
        adicionarTagAmbiente(tags, ambiente);

        // Dados adicionais do request
        if (request.getLocalizacao() != null) {
            perfil.setLatitude(request.getLocalizacao().getLatitude());
            perfil.setLongitude(request.getLocalizacao().getLongitude());
        }
        perfil.setHorarioInicio(request.getHorarioInicio());
        perfil.setHorarioFinal(request.getHorarioFinal());

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

    // Métodos auxiliares de tags e perfil (iguais ao seu código anterior)
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

    /**
     * Envia os locais filtrados para o algoritmo de recomendação via HTTP.
     */
    private List<LocalRecomendado> enviarParaAlgoritmo(List<Local> locais, PerfilUsuario perfil) {
        try {
            Map<String, Object> dadosAlgoritmo = new HashMap<>();
            dadosAlgoritmo.put("locais", locais);
            dadosAlgoritmo.put("perfil", perfil);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(dadosAlgoritmo, headers);

            ParameterizedTypeReference<List<LocalRecomendado>> responseType =
                new ParameterizedTypeReference<List<LocalRecomendado>>() {};

            ResponseEntity<List<LocalRecomendado>> responseEntity = restTemplate.exchange(
                ALGORITMO_RECOMENDACAO_URL,
                HttpMethod.POST,
                requestEntity,
                responseType
            );

            return responseEntity.getBody();

        } catch (Exception e) {
            System.err.println("Erro ao chamar o serviço de recomendação: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}