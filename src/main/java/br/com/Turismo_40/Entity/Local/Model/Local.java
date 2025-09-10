package br.com.Turismo_40.Entity.Local.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "locais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false)
    private String endereco;

    private String telefone;

    private String site;

    @Column(name = "horario_funcionamento")
    private String horarioFuncionamento;

    @Column(name = "preco_medio")
    private Double precoMedio;

    @Column(name = "avaliacao_media")
    private Double avaliacaoMedia;

    @Column(name = "url_imagem")
    private String urlImagem;

    // Coordenadas geográficas
    private Double latitude;
    private Double longitude;

    // Tags para categorização e filtragem
    @ElementCollection
    @CollectionTable(name = "local_tags", joinColumns = @JoinColumn(name = "local_id"))
    @Column(name = "tag")
    private List<String> tags;

    // Indica se o local é adequado para famílias com crianças
    @Column(name = "adequado_criancas")
    private Boolean adequadoCriancas = false;

    // Indica se o local é adequado para vegetarianos/veganos
    @Column(name = "opcoes_vegetarianas")
    private Boolean opcoesVegetarianas = false;

    // Indica se é um local mais para ambiente noturno
    @Column(name = "ambiente_noturno")
    private Boolean ambienteNoturno = false;

    // Tempo médio de permanência em minutos
    @Column(name = "tempo_medio_visita")
    private Integer tempoMedioVisita;

    // Indica se o local requer reserva
    @Column(name = "requer_reserva")
    private Boolean requerReserva = false;

    // Status do local (ativo/inativo)
    @Column(name = "ativo")
    private Boolean ativo = true;
}