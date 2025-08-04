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

        Roteiro roteiroSalvo = roteiroRepository.save(roteiro);
        
        // Gerar sugestões baseadas no perfil do usuário
        gerarSugestoesRoteiro(roteiroSalvo);
        
        return roteiroSalvo;
    }

    private void gerarSugestoesRoteiro(Roteiro roteiro) {
        // Buscar perfil do usuário
        Optional<PerfilUsuario> perfilOpt = perfilService.buscarPerfilPorUserId(roteiro.getUserId());
        
        // Buscar atrações baseadas no orçamento e preferências
        List<AtracaoTuristica> atracoes = atracaoService.buscarAtracoesPorOrcamento(
            roteiro.getCidade(), 
            roteiro.getOrcamento()
        );

        // Se o perfil existe, filtrar atrações baseadas no estilo de viagem preferido
        if (perfilOpt.isPresent()) {
            PerfilUsuario perfil = perfilOpt.get();
            AtracaoTuristica.Categoria categoriaPreferida = mapearEstiloParaCategoria(perfil.getEstilo());
            
            // Priorizar atrações que correspondem ao estilo do usuário
            List<AtracaoTuristica> atracoesPreferidas = atracoes.stream()
                .filter(a -> a.getCategoria() == categoriaPreferida)
                .toList();
            
            // Se existem atrações do estilo preferido, usar essas primeiro
            if (!atracoesPreferidas.isEmpty()) {
                // Usar 70% de atrações preferidas e 30% de outras categorias para variedade
                int maxAtracoes = getMaxAtracoesPorTempo(roteiro.getTempoDisponivel());
                int atracoesPreferidas70 = (int) (maxAtracoes * 0.7);
                int outrasAtracoes30 = maxAtracoes - atracoesPreferidas70;
                
                List<AtracaoTuristica> atracoesSelecionadas = new ArrayList<>();
                atracoesSelecionadas.addAll(atracoesPreferidas.stream()
                    .limit(atracoesPreferidas70)
                    .toList());
                
                // Adicionar outras categorias para diversificar
                List<AtracaoTuristica> outrasAtracoes = atracoes.stream()
                    .filter(a -> a.getCategoria() != categoriaPreferida)
                    .limit(outrasAtracoes30)
                    .toList();
                atracoesSelecionadas.addAll(outrasAtracoes);
                
                atracoes = atracoesSelecionadas;
            }
        }

        // Filtrar por ambiente se especificado
        if (roteiro.getPreferenciaAmbiente() != Roteiro.PreferenciaAmbiente.AMBOS) {
            AtracaoTuristica.Ambiente ambientePref = roteiro.getPreferenciaAmbiente() == 
                Roteiro.PreferenciaAmbiente.INTERNO ? 
                AtracaoTuristica.Ambiente.INTERNO : 
                AtracaoTuristica.Ambiente.EXTERNO;
            
            atracoes = atracoes.stream()
                .filter(a -> a.getAmbiente() == ambientePref)
                .toList();
        }

        // Adicionar atrações ao roteiro (limitando por tempo disponível)
        int maxAtracoes = getMaxAtracoesPorTempo(roteiro.getTempoDisponivel());
        LocalDateTime horaAtual = calcularHoraInicialBaseadaNoHorarioPreferido(roteiro.getHorarioPreferido());
        
        for (int i = 0; i < Math.min(atracoes.size(), maxAtracoes); i++) {
            RoteiroAtracaoTuristica roteiroAtracao = new RoteiroAtracaoTuristica();
            roteiroAtracao.setRoteiroId(roteiro.getRoteiroId());
            roteiroAtracao.setAtracaoId(atracoes.get(i).getAtracaoId());
            roteiroAtracao.setOrdemSequencia(i + 1);
            roteiroAtracao.setHoraEstimadaVisita(horaAtual.plusHours(i * 2));
            
            roteiroAtracaoRepository.save(roteiroAtracao);
        }

        // Incluir eventos se solicitado
        if (roteiro.getIncluirEventosSazonais()) {
            List<Evento> eventos = eventoService.buscarEventosPorOrcamento(
                roteiro.getCidade(), 
                roteiro.getOrcamento()
            );
            
            for (Evento evento : eventos) {
                RoteiroEvento roteiroEvento = new RoteiroEvento();
                roteiroEvento.setRoteiroId(roteiro.getRoteiroId());
                roteiroEvento.setEventoId(evento.getEventoId());
                roteiroEvento.setHoraEstimadaVisita(evento.getHoraInicio());
                
                roteiroEventoRepository.save(roteiroEvento);
            }
        }
    }
    
    /**
     * Mapeia o estilo de viagem do perfil do usuário para uma categoria de atração correspondente
     */
    private AtracaoTuristica.Categoria mapearEstiloParaCategoria(PerfilUsuario.Estilo estilo) {
        return switch (estilo) {
            case AVENTURA -> AtracaoTuristica.Categoria.AVENTURA;
            case CULTURAL -> AtracaoTuristica.Categoria.CULTURAL;
            case RELAXANTE -> AtracaoTuristica.Categoria.RELAXANTE;
            case GASTRONOMICO -> AtracaoTuristica.Categoria.GASTRONOMICO;
            case NATUREZA -> AtracaoTuristica.Categoria.NATUREZA;
            case OUTRO -> AtracaoTuristica.Categoria.CULTURAL; // Default para "outro"
        };
    }
    
    /**
     * Calcula a hora inicial do roteiro baseada na preferência de horário do usuário
     */
    private LocalDateTime calcularHoraInicialBaseadaNoHorarioPreferido(Roteiro.HorarioPreferido horarioPreferido) {
        LocalDateTime hoje = LocalDateTime.now().toLocalDate().atStartOfDay();
        
        return switch (horarioPreferido) {
            case MANHA -> hoje.withHour(9);   // 09:00
            case TARDE -> hoje.withHour(14);  // 14:00
            case NOITE -> hoje.withHour(19);  // 19:00
        };
    }

    private int getMaxAtracoesPorTempo(Roteiro.TempoDisponivel tempo) {
        return switch (tempo) {
            case MEIO_DIA -> 2;
            case UM_DIA -> 4;
            case DOIS_DIAS -> 8;
            case TRES_DIAS -> 12;
            case MAIS_DE_TRES_DIAS -> 20;
        };
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