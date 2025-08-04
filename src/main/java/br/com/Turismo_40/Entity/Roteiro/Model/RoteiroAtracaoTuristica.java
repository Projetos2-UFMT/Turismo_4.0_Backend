package br.com.Turismo_40.Entity.Roteiro.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "roteiro_atracao_turistica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoteiroAtracaoTuristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long roteiroId;

    @Column(nullable = false)
    private Long atracaoId;

    @Column(nullable = false)
    private Integer ordemSequencia;

    @Column(nullable = false)
    private LocalDateTime horaEstimadaVisita;
}