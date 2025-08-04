package br.com.Turismo_40.Entity.Roteiro.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import br.com.Turismo_40.Entity.Notificacao.Model.Notificacao;
import br.com.Turismo_40.Entity.RoteiroAtracaoTuristica.Model.RoteiroAtracaoTuristica;
import br.com.Turismo_40.Entity.RoteiroEvento.Model.RoteiroEvento;

@Entity
@Table(name = "roteiro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Roteiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roteiroId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TempoDisponivel tempoDisponivel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HorarioPreferido horarioPreferido;

    @Column(nullable = false)
    private Double orcamento;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ModoTransporte modoTransporte;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PreferenciaAmbiente preferenciaAmbiente;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean incluirEventosSazonais;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "roteiroId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoteiroAtracaoTuristica> atracoes;

    @OneToMany(mappedBy = "roteiroId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoteiroEvento> eventos;

    @OneToMany(mappedBy = "roteiroId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notificacao> notificacoes;

    public enum TempoDisponivel {
        MEIO_DIA, UM_DIA, DOIS_DIAS, TRES_DIAS, MAIS_DE_TRES_DIAS
    }

    public enum HorarioPreferido {
        MANHA, TARDE, NOITE
    }

    public enum ModoTransporte {
        CAMINHANDO, TRANSPORTE_PUBLICO, CARRO, CARONA, OUTRO
    }

    public enum PreferenciaAmbiente {
        INTERNO, EXTERNO, AMBOS
    }
}