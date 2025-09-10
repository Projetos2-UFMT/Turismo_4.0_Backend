package br.com.Turismo_40.Entity.Itinerario.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import br.com.Turismo_40.Entity.Local.Model.Local;

import java.time.LocalTime;

@Entity
@Table(name = "itens_itinerario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemItinerario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerario_id", nullable = false)
    private Itinerario itinerario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    @Column(name = "horario_sugerido")
    private LocalTime horarioSugerido;

    @Column(name = "tempo_permanencia") // em minutos
    private Integer tempoPermanencia;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    // Pontuação de recomendação para este item específico
    @Column(name = "pontuacao_recomendacao")
    private Double pontuacaoRecomendacao;

    @Column(name = "visitado")
    private Boolean visitado = false;
}