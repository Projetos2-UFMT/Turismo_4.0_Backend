package br.com.Turismo_40.Entity.Questionario.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

// DTO para local recomendado
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalRecomendado {
    private Long id;
    private String nome;
    private String descricao;
    private String endereco;
    private String telefone;
    private Double precoMedio;
    private Double avaliacaoMedia;
    private String urlImagem;
    private List<String> tags;
    private Integer tempoMedioVisita;
    private Double pontuacaoRecomendacao;
}

