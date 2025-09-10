package br.com.Turismo_40.Entity.Questionario.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

// DTO para cada resposta individual
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespostaQuestionario {
    private String pergunta;
    private String respostaSelecionada;
    private List<String> opcoes; // Para referência, se necessário
}