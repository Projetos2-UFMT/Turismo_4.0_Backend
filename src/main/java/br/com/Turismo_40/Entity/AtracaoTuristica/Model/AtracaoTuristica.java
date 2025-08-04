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

    @Column(nullable = false, precision = 10, scale = 8)
    private Double latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private Double longitude;

    public enum Categoria {
        AVENTURA, CULTURAL, RELAXANTE, GASTRONOMICO, NATUREZA
    }

    public enum Ambiente {
        INTERNO, EXTERNO
    }
}