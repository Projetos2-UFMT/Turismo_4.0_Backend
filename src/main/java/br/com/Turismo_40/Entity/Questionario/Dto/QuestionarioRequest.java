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
    private List<RespostaQuestionario> respostas;
    private Long usuarioId; // ID do usuário que respondeu
}