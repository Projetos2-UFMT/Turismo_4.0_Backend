package br.com.Turismo_40.Entity.Questionario.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

// DTO para receber as respostas do questionário
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionarioRequest {
    private Integer horarioInicio;
    private Integer horarioFinal;
    private Localizacao localizacao;
    private List<Integer> respostas;
    private Long usuarioId;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Localizacao {
        private Double latitude;
        private Double longitude;
    }
}