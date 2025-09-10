package br.com.Turismo_40.Entity.Itinerario.Service;

import br.com.Turismo_40.Entity.Itinerario.Model.Itinerario;
import br.com.Turismo_40.Entity.Itinerario.Model.ItemItinerario;
import br.com.Turismo_40.Entity.Itinerario.Repository.ItinerarioRepository;
import br.com.Turismo_40.Entity.Itinerario.Repository.ItemItinerarioRepository;
import br.com.Turismo_40.Entity.Local.Model.Local;
import br.com.Turismo_40.Entity.Local.Repository.LocalRepository;
import br.com.Turismo_40.Entity.User.Model.AppUser;
import br.com.Turismo_40.Entity.User.Repository.AppUserRepository;
import br.com.Turismo_40.Entity.Questionario.Dto.LocalRecomendado;
import br.com.Turismo_40.Entity.Questionario.Dto.PerfilUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItinerarioService {

    @Autowired
    private ItinerarioRepository itinerarioRepository;

    @Autowired
    private ItemItinerarioRepository itemItinerarioRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private AppUserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Cria um itinerário baseado nas recomendações geradas
     */
    public Itinerario criarItinerario(Long usuarioId, List<LocalRecomendado> locaisRecomendados, 
                                     PerfilUsuario perfil, String titulo) {
        
        // Buscar o usuário
        Optional<AppUser> userOpt = userRepository.findById(usuarioId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        AppUser usuario = userOpt.get();

        // Criar o itinerário
        Itinerario itinerario = new Itinerario();
        itinerario.setUsuario(usuario);
        itinerario.setTitulo(titulo != null ? titulo : "Roteiro Personalizado");
        itinerario.setDescricao(gerarDescricaoItinerario(perfil));
        
        // Calcular durações e custos
        int duracaoTotal = locaisRecomendados.stream()
                .mapToInt(local -> local.getTempoMedioVisita() != null ? local.getTempoMedioVisita() : 60)
                .sum();
        itinerario.setDuracaoEstimada(duracaoTotal);

        double custoTotal = locaisRecomendados.stream()
                .mapToDouble(local -> local.getPrecoMedio() != null ? local.getPrecoMedio() : 0.0)
                .sum();
        itinerario.setCustoEstimado(custoTotal);

        // Salvar preferências como JSON
        try {
            itinerario.setPreferenciasJson(objectMapper.writeValueAsString(perfil));
        } catch (Exception e) {
            itinerario.setPreferenciasJson("{}");
        }

        // Salvar o itinerário
        itinerario = itinerarioRepository.save(itinerario);

        // Criar os itens do itinerário
        criarItensItinerario(itinerario, locaisRecomendados);

        return itinerario;
    }

    /**
     * Cria os itens individuais do itinerário
     */
    private void criarItensItinerario(Itinerario itinerario, List<LocalRecomendado> locaisRecomendados) {
        LocalTime horarioInicial = LocalTime.of(9, 0); // Começar às 9h
        LocalTime horarioAtual = horarioInicial;

        for (int i = 0; i < locaisRecomendados.size(); i++) {
            LocalRecomendado localRec = locaisRecomendados.get(i);
            
            // Buscar o local no banco
            Optional<Local> localOpt = localRepository.findById(localRec.getId());
            if (localOpt.isEmpty()) continue;

            Local local = localOpt.get();

            // Criar item do itinerário
            ItemItinerario item = new ItemItinerario();
            item.setItinerario(itinerario);
            item.setLocal(local);
            item.setOrdem(i + 1);
            item.setHorarioSugerido(horarioAtual);
            item.setTempoPermanencia(localRec.getTempoMedioVisita() != null ? 
                                   localRec.getTempoMedioVisita() : 60);
            item.setPontuacaoRecomendacao(localRec.getPontuacaoRecomendacao());
            item.setObservacoes(gerarObservacaoItem(local, i == 0, i == locaisRecomendados.size() - 1));

            // Salvar item
            itemItinerarioRepository.save(item);

            // Atualizar horário para próximo local (tempo de permanência + 30 min de deslocamento)
            horarioAtual = horarioAtual.plusMinutes(item.getTempoPermanencia() + 30);
        }
    }

    /**
     * Busca itinerários de um usuário
     */
    public List<Itinerario> buscarItinerariosPorUsuario(Long usuarioId) {
        return itinerarioRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Busca um itinerário específico com seus itens
     */
    public Optional<Itinerario> buscarItinerarioComItens(Long itinerarioId) {
        Optional<Itinerario> itinerarioOpt = itinerarioRepository.findById(itinerarioId);
        if (itinerarioOpt.isPresent()) {
            Itinerario itinerario = itinerarioOpt.get();
            // Força o carregamento dos itens
            itinerario.setItens(itemItinerarioRepository.findByItinerarioOrderByOrdem(itinerario));
        }
        return itinerarioOpt;
    }

    /**
     * Marca um item do itinerário como visitado
     */
    public void marcarItemComoVisitado(Long itemId) {
        Optional<ItemItinerario> itemOpt = itemItinerarioRepository.findById(itemId);
        if (itemOpt.isPresent()) {
            ItemItinerario item = itemOpt.get();
            item.setVisitado(true);
            itemItinerarioRepository.save(item);

            // Verificar se todos os itens foram visitados para atualizar status do itinerário
            verificarEAtualizarStatusItinerario(item.getItinerario());
        }
    }

    /**
     * Atualiza status do itinerário baseado nos itens visitados
     */
    private void verificarEAtualizarStatusItinerario(Itinerario itinerario) {
        List<ItemItinerario> todosItens = itemItinerarioRepository.findByItinerarioOrderByOrdem(itinerario);
        List<ItemItinerario> itensVisitados = itemItinerarioRepository.findByItinerarioAndVisitadoTrue(itinerario);

        if (itensVisitados.size() == todosItens.size()) {
            itinerario.setStatus(Itinerario.StatusItinerario.CONCLUIDO);
        } else if (itensVisitados.size() > 0) {
            itinerario.setStatus(Itinerario.StatusItinerario.EM_ANDAMENTO);
        }

        itinerarioRepository.save(itinerario);
    }

    /**
     * Exclui um itinerário
     */
    public void excluirItinerario(Long itinerarioId, Long usuarioId) {
        Optional<Itinerario> itinerarioOpt = itinerarioRepository.findById(itinerarioId);
        if (itinerarioOpt.isPresent()) {
            Itinerario itinerario = itinerarioOpt.get();
            
// Verifica se o itinerário tem um usuário associado E se o ID do usuário é diferente do ID fornecido.
if (itinerario.getUsuario() == null || itinerario.getUsuario().getId() != usuarioId) {
    throw new RuntimeException("Usuário não autorizado a excluir este itinerário");
}

            itinerarioRepository.delete(itinerario);
        } else {
            throw new RuntimeException("Itinerário não encontrado");
        }
    }

    /**
     * Gera descrição do itinerário baseada no perfil
     */
    private String gerarDescricaoItinerario(PerfilUsuario perfil) {
        StringBuilder descricao = new StringBuilder();
        descricao.append("Roteiro personalizado baseado em suas preferências: ");
        
        if (perfil.getTipoComida() != null) {
            descricao.append("culinária ").append(perfil.getTipoComida().toLowerCase()).append(", ");
        }
        
        if (perfil.getCategoriaInteresse() != null) {
            descricao.append("interesse em ").append(perfil.getCategoriaInteresse().toLowerCase()).append(", ");
        }
        
        if (perfil.isTemFilhos()) {
            descricao.append("adequado para famílias com crianças, ");
        }
        
        if (perfil.getNivelAnimacao() != null) {
            descricao.append("ambiente ").append(perfil.getNivelAnimacao().toLowerCase()).append(".");
        }
        
        return descricao.toString();
    }

    /**
     * Gera observação específica para cada item
     */
    private String gerarObservacaoItem(Local local, boolean isPrimeiro, boolean isUltimo) {
        StringBuilder obs = new StringBuilder();
        
        if (isPrimeiro) {
            obs.append("Primeiro local do roteiro. ");
        }
        
        if (isUltimo) {
            obs.append("Último local do roteiro. ");
        }
        
        if (local.getRequerReserva() != null && local.getRequerReserva()) {
            obs.append("Recomenda-se fazer reserva antecipada. ");
        }
        
        if (local.getHorarioFuncionamento() != null) {
            obs.append("Horário: ").append(local.getHorarioFuncionamento()).append(". ");
        }
        
        return obs.toString().trim();
    }

    /**
     * Busca estatísticas de itinerários
     */
    public ItinerarioStats obterEstatisticas() {
        ItinerarioStats stats = new ItinerarioStats();
        stats.setTotalItinerarios(itinerarioRepository.count());
        stats.setItinerariosCriados(itinerarioRepository.countByStatus(Itinerario.StatusItinerario.CRIADO));
        stats.setItinerariosEmAndamento(itinerarioRepository.countByStatus(Itinerario.StatusItinerario.EM_ANDAMENTO));
        stats.setItinerariosConcluidos(itinerarioRepository.countByStatus(Itinerario.StatusItinerario.CONCLUIDO));
        return stats;
    }

    // Classe interna para estatísticas
    public static class ItinerarioStats {
        private long totalItinerarios;
        private long itinerariosCriados;
        private long itinerariosEmAndamento;
        private long itinerariosConcluidos;

        // Getters e Setters
        public long getTotalItinerarios() { return totalItinerarios; }
        public void setTotalItinerarios(long totalItinerarios) { this.totalItinerarios = totalItinerarios; }
        
        public long getItinerariosCriados() { return itinerariosCriados; }
        public void setItinerariosCriados(long itinerariosCriados) { this.itinerariosCriados = itinerariosCriados; }
        
        public long getItinerariosEmAndamento() { return itinerariosEmAndamento; }
        public void setItinerariosEmAndamento(long itinerariosEmAndamento) { this.itinerariosEmAndamento = itinerariosEmAndamento; }
        
        public long getItinerariosConcluidos() { return itinerariosConcluidos; }
        public void setItinerariosConcluidos(long itinerariosConcluidos) { this.itinerariosConcluidos = itinerariosConcluidos; }
    }
}