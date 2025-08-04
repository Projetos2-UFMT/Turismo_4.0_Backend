package br.com.Turismo_40.Entity.AtracaoTuristica.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "atracao_turistica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtracaoTuristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long atracaoId;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false)
    private Double custo;

    @Column(length = 100)
    private String horarioFuncionamento;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Ambiente ambiente;

    // A anotação foi simplificada para remover precision e scale
    @Column(nullable = false)
    private Double latitude;

    // A anotação foi simplificada para remover precision e scale
    @Column(nullable = false)
    private Double longitude;

    public enum Categoria {
        AVENTURA, CULTURAL, RELAXANTE, GASTRONOMICO, NATUREZA
    }

    public enum Ambiente {
        INTERNO, EXTERNO
    }
}