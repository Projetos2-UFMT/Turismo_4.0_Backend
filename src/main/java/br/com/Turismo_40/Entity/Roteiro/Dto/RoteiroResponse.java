package br.com.Turismo_40.Entity.Roteiro.Dto;

import br.com.Turismo_40.Entity.AtracaoTuristica.Model.AtracaoTuristica;
import br.com.Turismo_40.Entity.Evento.Model.Evento;
import br.com.Turismo_40.Entity.Roteiro.Model.Roteiro;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RoteiroResponse {
    private Long roteiroId;
    private Long userId;
    private String cidade;
    private Roteiro.TempoDisponivel tempoDisponivel;
    private Roteiro.HorarioPreferido horarioPreferido;
    private Double orcamento;
    private Roteiro.ModoTransporte modoTransporte;
    private Roteiro.PreferenciaAmbiente preferenciaAmbiente;
    private Boolean incluirEventosSazonais;
    private LocalDateTime criadoEm;
    private List<AtracaoTuristica> atracoes;
    private List<Evento> eventos;
    private String message;
}