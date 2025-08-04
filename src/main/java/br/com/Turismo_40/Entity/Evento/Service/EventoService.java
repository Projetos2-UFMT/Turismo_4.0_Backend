package br.com.Turismo_40.Entity.Evento.Service;

import br.com.Turismo_40.Entity.Evento.Model.Evento;
import br.com.Turismo_40.Entity.Evento.Repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public Evento criarEvento(String cidade, String nome, String descricao, 
                             LocalDateTime horaInicio, LocalDateTime horaFim, 
                             Double custo, Evento.Ambiente ambiente) {
        Evento evento = new Evento();
        evento.setCidade(cidade);
        evento.setNome(nome);
        evento.setDescricao(descricao);
        evento.setHoraInicio(horaInicio);
        evento.setHoraFim(horaFim);
        evento.setCusto(custo);
        evento.setAmbiente(ambiente);

        return eventoRepository.save(evento);
    }

    public List<Evento> listarEventosPorCidade(String cidade) {
        return eventoRepository.findByCidade(cidade);
    }

    public List<Evento> buscarEventosDisponiveis(String cidade, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return eventoRepository.findEventosDisponiveis(cidade, dataInicio, dataFim);
    }

    public List<Evento> buscarEventosPorOrcamento(String cidade, Double orcamento) {
        return eventoRepository.findEventosPorOrcamento(cidade, orcamento);
    }

    public Optional<Evento> buscarEventoPorId(Long eventoId) {
        return eventoRepository.findById(eventoId);
    }

    public void deletarEvento(Long eventoId) {
        eventoRepository.deleteById(eventoId);
    }
}