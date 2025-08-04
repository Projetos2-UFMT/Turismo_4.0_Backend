package br.com.Turismo_40.Entity.PerfilUsuario.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "perfil_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Estilo estilo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContextoViagem contextoViagem;

    @Column(length = 500)
    private String interesses;

    @Column(length = 500)
    private String restricoes;

    // Enums para os tipos de perfil
    public enum Estilo {
        AVENTURA, CULTURAL, RELAXANTE, GASTRONOMICO, NATUREZA, OUTRO
    }

    public enum ContextoViagem {
        SOLO, PARCEIRO, FAMILIA_COM_CRIANCAS, FAMILIA_SEM_CRIANCAS, AMIGOS
    }
}