package br.com.Turismo_40.Entity.Questionario.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

// DTO para resposta da recomendação
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacaoResponse {
    private String message;
    private List<LocalRecomendado> locaisRecomendados;
    private String itinerarioGerado;
    private int totalLocais;
}