package br.com.Turismo_40.Entity.AtracaoTuristica.Dto;

import br.com.Turismo_40.Entity.AtracaoTuristica.Model.AtracaoTuristica;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtracaoTuristicaResponse {
    private Long atracaoId;
    private String cidade;
    private String nome;
    private AtracaoTuristica.Categoria categoria;
    private String descricao;
    private Double custo;
    private String horarioFuncionamento;
    private AtracaoTuristica.Ambiente ambiente;
    private Double latitude;
    private Double longitude;
    private String message;
}