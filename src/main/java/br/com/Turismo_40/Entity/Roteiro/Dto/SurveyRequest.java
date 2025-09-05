package br.com.Turismo_40.Entity.Roteiro.Dto;

import br.com.Turismo_40.Entity.Roteiro.Model.Roteiro;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class SurveyRequest {
    
    // Dados do roteiro
    private String cidade;
    private Roteiro.TempoDisponivel tempoDisponivel;
    private Roteiro.HorarioPreferido horarioPreferido;
    private Double orcamento;
    private Roteiro.ModoTransporte modoTransporte;
    private Boolean incluirEventosSazonais;

    // A lista de respostas da pesquisa
    private List<QuestionResponse> respostas;

    @Getter
    @Setter
    public static class QuestionResponse {
        private String pergunta;
        private String resposta;
    }
}