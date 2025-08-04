package br.com.Turismo_40.Entity.Evento.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventoId;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime horaInicio;

    @Column(nullable = false)
    private LocalDateTime horaFim;

    @Column(nullable = false)
    private Double custo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Ambiente ambiente;

    public enum Ambiente {
        INTERNO, EXTERNO
    }
}