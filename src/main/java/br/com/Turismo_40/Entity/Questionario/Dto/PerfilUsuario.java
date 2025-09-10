package br.com.Turismo_40.Entity.Questionario.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

// DTO para perfil do usuário baseado nas respostas
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUsuario {
    
    // Preferências alimentares
    private String tipoComida; // "Brasileira", "Churrasco", "Japonesa", etc.
    private boolean temRestricaoAlimentar;
    private boolean vegetariano;
    private boolean vegano;
    
    // Situação familiar
    private boolean temFilhos;
    private int quantidadeFilhos;
    
    // Preferências de atividade
    private String tipoEvento; // "Parque de diversões", "Bar", "Shopping", etc.
    private String interesseCultural; // "Museu", "Arte", "História", etc.
    private String categoriaInteresse; // "Cultura", "Natureza", "Esporte", etc.
    private String nivelAnimacao; // "Animado", "Calmo", etc.
    private String ambientePreferido; // "Natureza", "Urbano", "Histórico", etc.
    
    // Tags derivadas das respostas
    private List<String> tagsPreferencias;
    
    // Filtros aplicados
    private boolean adequadoParaCriancas;
    private boolean evitarAmbienteNoturno;
    private boolean precisaOpcoesVegetarianas;
}