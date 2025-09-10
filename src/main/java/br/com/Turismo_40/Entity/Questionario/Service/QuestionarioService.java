package br.com.Turismo_40.Entity.Questionario.Service;

import br.com.Turismo_40.Entity.Itinerario.Model.Itinerario;
import br.com.Turismo_40.Entity.Itinerario.Service.ItinerarioService;
import br.com.Turismo_40.Entity.Local.Model.Local;
import br.com.Turismo_40.Entity.Local.Repository.LocalRepository;
import br.com.Turismo_40.Entity.Questionario.Dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private final String ALGORITMO_RECOMENDACAO_URL = "http://algoritmo-service:8080/api/recomendar";

    /**
     * Processa as respostas do questionário e retorna recomendações
     */
    public RecomendacaoResponse processarQuestionario(QuestionarioRequest request) {
        
        // 1. Converter respostas em perfil do usuário
        PerfilUsuario perfil = criarPerfilUsuario(request.getRespostas());
        
        // 2. Aplicar filtros inteligentes
        List<Local> locaisFiltrados = aplicarFiltros(perfil);
        
        // 3. Filtrar por tags de preferência
        List<Local> locaisComTags = filtrarPorTags(locaisFiltrados, perfil.getTagsPreferencias());
        
        // 4. Enviar para algoritmo de recomendação
        List<LocalRecomendado> locaisRecomendados = enviarParaAlgoritmo(locaisComTags, perfil);
        
        // 5. Criar itinerário no banco de dados
        Itinerario itinerarioCriado = null;
        if (request.getUsuarioId() != null && !locaisRecomendados.isEmpty()) {
            try {
                itinerarioCriado = itinerarioService.criarItinerario(
                    request.getUsuarioId(), 
                    locaisRecomendados, 
                    perfil, 
                    "Roteiro Personalizado - " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                );
            } catch (Exception e) {
                // Log do erro, mas continue com a resposta
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
     * Converte as respostas em um perfil estruturado do usuário
     */
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

    /**
     * Aplica filtros inteligentes baseados no perfil do usuário
     */
    private List<Local> aplicarFiltros(PerfilUsuario perfil) {
        List<Local> todosLocais = localRepository.findAllByAtivoTrue();
        
        return todosLocais.stream().filter(local -> {
            
            // Se tem filhos pequenos, evitar ambientes noturnos
            if (perfil.isEvitarAmbienteNoturno() && local.getAmbienteNoturno() != null && local.getAmbienteNoturno()) {
                return false;
            }
            
            // Se tem filhos, priorizar locais adequados para crianças
            if (perfil.isTemFilhos() && local.getAdequadoCriancas() != null && !local.getAdequadoCriancas()) {
                // Permite alguns tipos específicos mesmo sem ser marcado como adequado para crianças
                if (!contemTags(local.getTags(), Arrays.asList("shopping", "natureza", "parque de diversoes"))) {
                    return false;
                }
            }
            
            // Se é vegetariano/vegano e é restaurante, verificar opções
            if (perfil.isPrecisaOpcoesVegetarianas() && 
                contemTags(local.getTags(), Arrays.asList("churrasco", "brasileira", "japonesa", "italiana", "fast food")) &&
                (local.getOpcoesVegetarianas() == null || !local.getOpcoesVegetarianas())) {
                return false;
            }
            
            return true;
        }).collect(Collectors.toList());
    }

    /**
     * Filtra locais pelas tags de preferência do usuário
     */
    private List<Local> filtrarPorTags(List<Local> locais, List<String> tagsPreferencia) {
        if (tagsPreferencia.isEmpty()) {
            return locais;
        }

        return locais.stream()
                .filter(local -> local.getTags() != null && 
                        local.getTags().stream().anyMatch(tagsPreferencia::contains))
                .collect(Collectors.toList());
    }

    /**
     * Envia os locais filtrados para o algoritmo de recomendação
     */
    private List<LocalRecomendado> enviarParaAlgoritmo(List<Local> locais, PerfilUsuario perfil) {
        try {
            // Preparar dados para envio
            Map<String, Object> dadosAlgoritmo = new HashMap<>();
            dadosAlgoritmo.put("locais", locais);
            dadosAlgoritmo.put("perfil", perfil);
            
            // Enviar requisição POST para o algoritmo
            // ResponseEntity<List<LocalRecomendado>> response = restTemplate.postForEntity(
            //     ALGORITMO_RECOMENDACAO_URL, 
            //     dadosAlgoritmo, 
            //     List.class
            // );
            
            // Por enquanto, simular a resposta do algoritmo
            return simularAlgoritmoRecomendacao(locais, perfil);
            
        } catch (Exception e) {
            // Em caso de erro, usar algoritmo local simples
            return simularAlgoritmoRecomendacao(locais, perfil);
        }
    }

    /**
     * Simulação do algoritmo de recomendação (enquanto o serviço externo não está disponível)
     */
    private List<LocalRecomendado> simularAlgoritmoRecomendacao(List<Local> locais, PerfilUsuario perfil) {
        return locais.stream()
                .map(this::converterParaLocalRecomendado)
                .sorted((a, b) -> Double.compare(b.getPontuacaoRecomendacao(), a.getPontuacaoRecomendacao()))
                .limit(10) // Limitar a 10 recomendações
                .collect(Collectors.toList());
    }

    private LocalRecomendado converterParaLocalRecomendado(Local local) {
        LocalRecomendado recomendado = new LocalRecomendado();
        recomendado.setId(local.getId());
        recomendado.setNome(local.getNome());
        recomendado.setDescricao(local.getDescricao());
        recomendado.setEndereco(local.getEndereco());
        recomendado.setTelefone(local.getTelefone());
        recomendado.setPrecoMedio(local.getPrecoMedio());
        recomendado.setAvaliacaoMedia(local.getAvaliacaoMedia());
        recomendado.setUrlImagem(local.getUrlImagem());
        recomendado.setTags(local.getTags());
        recomendado.setTempoMedioVisita(local.getTempoMedioVisita());
        recomendado.setPontuacaoRecomendacao(calcularPontuacao(local));
        return recomendado;
    }

    private Double calcularPontuacao(Local local) {
        // Algoritmo simples de pontuação
        double pontuacao = 0.0;
        
        if (local.getAvaliacaoMedia() != null) {
            pontuacao += local.getAvaliacaoMedia() * 2;
        }
        
        if (local.getTags() != null) {
            pontuacao += local.getTags().size() * 0.5;
        }
        
        return pontuacao + Math.random() * 2; // Adicionar um pouco de aleatoriedade
    }

    // Métodos auxiliares para adicionar tags baseadas nas respostas
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

    private boolean contemTags(List<String> tagsLocal, List<String> tagsVerificar) {
        if (tagsLocal == null || tagsLocal.isEmpty()) return false;
        return tagsLocal.stream().anyMatch(tagsVerificar::contains);
    }
}