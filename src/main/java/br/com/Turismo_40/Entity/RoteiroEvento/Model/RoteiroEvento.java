package br.com.Turismo_40.Entity.RoteiroEvento.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "roteiro_evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoteiroEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long roteiroId;

    @Column(nullable = false)
    private Long eventoId;

    @Column(nullable = false)
    private LocalDateTime horaEstimadaVisita;
}