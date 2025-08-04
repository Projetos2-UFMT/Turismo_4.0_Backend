package br.com.Turismo_40.Entity.Notificacao.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificacaoId;

    @Column(nullable = false)
    private Long roteiroId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Column(nullable = false)
    private LocalDateTime horarioAgendado;

    public enum Tipo {
        ALERTA_TRAFEGO, LEMBRETE_AGENDA, SUGESTAO_ALTERNATIVA
    }
}