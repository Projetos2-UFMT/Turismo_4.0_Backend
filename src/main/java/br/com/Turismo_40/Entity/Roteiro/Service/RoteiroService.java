package br.com.Turismo_40.Entity.Roteiro.Service;

import br.com.Turismo_40.Entity.AtracaoTuristica.Model.AtracaoTuristica;
import br.com.Turismo_40.Entity.AtracaoTuristica.Service.AtracaoTuristicaService;
import br.com.Turismo_40.Entity.Evento.Model.Evento;
import br.com.Turismo_40.Entity.Evento.Service.EventoService;
import br.com.Turismo_40.Entity.PerfilUsuario.Model.PerfilUsuario;
import br.com.Turismo_40.Entity.PerfilUsuario.Service.PerfilUsuarioService;
import br.com.Turismo_40.Entity.Roteiro.Model.Roteiro;
import br.com.Turismo_40.Entity.Roteiro.Model.RoteiroAtracaoTuristica;
import br.com.Turismo_40.Entity.Roteiro.Model.RoteiroEvento;
import br.com.Turismo_40.Entity.Roteiro.Repository.RoteiroRepository;
import br.com.Turismo_40.Entity.Roteiro.Repository.RoteiroAtracaoTuristicaRepository;
import br.com.Turismo_40.Entity.Roteiro.Repository.RoteiroEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoteiroService {

    @Autowired
    private RoteiroRepository roteiroRepository;

    @Autowired
    private RoteiroAtracaoTuristicaRepository roteiroAtracaoRepository;

    @Autowired
    private RoteiroEventoRepository roteiroEventoRepository;

    @Autowired
    private AtracaoTuristicaService atracaoService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private PerfilUsuarioService perfilService;

    public Roteiro criarRoteiro(Long userId, String cidade, Roteiro.TempoDisponivel tempoDisponivel,
                                Roteiro.HorarioPreferido horarioPreferido, Double orcamento,
                                Roteiro.ModoTransporte modoTransporte, Roteiro.PreferenciaAmbiente preferenciaAmbiente,
                                Boolean incluirEventosSazonais) {
        // Este método agora apenas salva o roteiro básico, sem a lógica de recomendação.
        Roteiro roteiro = new Roteiro();
        roteiro.setUserId(userId);
        roteiro.setCidade(cidade);
        roteiro.setTempoDisponivel(tempoDisponivel);
        roteiro.setHorarioPreferido(horarioPreferido);
        roteiro.setOrcamento(orcamento);
        roteiro.setModoTransporte(modoTransporte);
        roteiro.setPreferenciaAmbiente(preferenciaAmbiente);
        roteiro.setIncluirEventosSazonais(incluirEventosSazonais);
        roteiro.setCriadoEm(LocalDateTime.now());
        
        return roteiroRepository.save(roteiro);
    }
    
    /**
     * Prepara uma string (prompt) com todos os dados do usuário e do roteiro para ser usada por uma IA externa.
     * Esta string descreve o roteiro desejado para que a IA possa gerar as sugestões.
     */
    public String prepararPromptParaIA(Long userId, String cidade, Roteiro.TempoDisponivel tempoDisponivel,
                                       Roteiro.HorarioPreferido horarioPreferido, Double orcamento,
                                       Roteiro.ModoTransporte modoTransporte, Roteiro.PreferenciaAmbiente preferenciaAmbiente,
                                       Boolean incluirEventosSazonais) {
        
        Optional<PerfilUsuario> perfilOpt = perfilService.buscarPerfilPorUserId(userId);
        
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Gerar um roteiro de viagem com base nas seguintes informações:\n");
        promptBuilder.append("Cidade: ").append(cidade).append("\n");
        promptBuilder.append("Tempo disponível: ").append(tempoDisponivel.name().toLowerCase()).append("\n");
        promptBuilder.append("Horário preferido: ").append(horarioPreferido.name().toLowerCase()).append("\n");
        promptBuilder.append("Orçamento: ").append(orcamento).append(" reais\n");
        promptBuilder.append("Modo de transporte: ").append(modoTransporte.name().toLowerCase()).append("\n");
        promptBuilder.append("Preferencia de ambiente: ").append(preferenciaAmbiente.name().toLowerCase()).append("\n");
        promptBuilder.append("Incluir eventos sazonais: ").append(incluirEventosSazonais).append("\n");
        
        if (perfilOpt.isPresent()) {
            PerfilUsuario perfil = perfilOpt.get();
            promptBuilder.append("Estilo de viagem do usuário: ").append(perfil.getEstilo().name().toLowerCase()).append("\n");
            promptBuilder.append("Contexto da viagem: ").append(perfil.getContextoViagem().name().toLowerCase()).append("\n");
            promptBuilder.append("Interesses do usuário: ").append(perfil.getInteresses()).append("\n");
            promptBuilder.append("Restrições do usuário: ").append(perfil.getRestricoes()).append("\n");
        }
        
        return promptBuilder.toString();
    }

    public List<Roteiro> listarRoteirosPorUsuario(Long userId) {
        return roteiroRepository.findByUserId(userId);
    }

    public Optional<Roteiro> buscarRoteiroPorId(Long roteiroId) {
        return roteiroRepository.findById(roteiroId);
    }

    public List<AtracaoTuristica> buscarAtracoesDoRoteiro(Long roteiroId) {
        List<RoteiroAtracaoTuristica> roteiroAtracoes = roteiroAtracaoRepository
            .findByRoteiroIdOrderByOrdemSequencia(roteiroId);
        
        List<AtracaoTuristica> atracoes = new ArrayList<>();
        for (RoteiroAtracaoTuristica ra : roteiroAtracoes) {
            Optional<AtracaoTuristica> atracao = atracaoService.buscarAtracaoPorId(ra.getAtracaoId());
            atracao.ifPresent(atracoes::add);
        }
        
        return atracoes;
    }

    public List<Evento> buscarEventosDoRoteiro(Long roteiroId) {
        List<RoteiroEvento> roteiroEventos = roteiroEventoRepository.findByRoteiroId(roteiroId);
        
        List<Evento> eventos = new ArrayList<>();
        for (RoteiroEvento re : roteiroEventos) {
            Optional<Evento> evento = eventoService.buscarEventoPorId(re.getEventoId());
            evento.ifPresent(eventos::add);
        }
        
        return eventos;
    }

    public void deletarRoteiro(Long roteiroId) {
        roteiroRepository.deleteById(roteiroId);
    }
}