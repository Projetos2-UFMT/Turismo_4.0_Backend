package br.com.Turismo_40.Entity.Roteiro.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lugar")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cidade;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String endereco;

    @Column(name = "tags", columnDefinition = "text[]")
    private String[] tags;
}