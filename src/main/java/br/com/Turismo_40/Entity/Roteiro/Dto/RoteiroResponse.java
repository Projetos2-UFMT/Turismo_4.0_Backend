package br.com.Turismo_40.Entity.Roteiro.Dto;

import br.com.Turismo_40.Entity.Roteiro.Model.Roteiro;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private String message;
}