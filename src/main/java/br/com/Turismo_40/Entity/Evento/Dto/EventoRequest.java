package br.com.Turismo_40.Entity.Evento.Dto;

import br.com.Turismo_40.Entity.Evento.Model.Evento;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventoRequest {
    private String cidade;
    private String nome;
    private String descricao;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private Double custo;
    private Evento.Ambiente ambiente;
}