package br.com.Turismo_40.Entity.Roteiro.Dto;

import br.com.Turismo_40.Entity.Roteiro.Model.Roteiro;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoteiroRequest {
    private String cidade;
    private Roteiro.TempoDisponivel tempoDisponivel;
    private Roteiro.HorarioPreferido horarioPreferido;
    private Double orcamento;
    private Roteiro.ModoTransporte modoTransporte;
    private Roteiro.PreferenciaAmbiente preferenciaAmbiente;
    private Boolean incluirEventosSazonais;
}