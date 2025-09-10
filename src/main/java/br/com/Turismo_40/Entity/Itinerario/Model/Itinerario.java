package br.com.Turismo_40.Entity.Itinerario.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import br.com.Turismo_40.Entity.User.Model.AppUser;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "itinerarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Itinerario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private AppUser usuario;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descricao", length = 1000)
    private String descricao;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "duracao_estimada") // em minutos
    private Integer duracaoEstimada;

    @Column(name = "custo_estimado")
    private Double custoEstimado;

    // Status do itinerário: CRIADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusItinerario status = StatusItinerario.CRIADO;

    @OneToMany(mappedBy = "itinerario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemItinerario> itens;

    // JSON com as preferências do usuário que geraram este itinerário
    @Column(name = "preferencias_json", columnDefinition = "TEXT")
    private String preferenciasJson;

    public enum StatusItinerario {
        CRIADO,
        EM_ANDAMENTO,
        CONCLUIDO,
        CANCELADO
    }
}