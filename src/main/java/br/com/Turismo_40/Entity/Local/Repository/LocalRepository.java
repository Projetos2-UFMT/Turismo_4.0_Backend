package br.com.Turismo_40.Entity.Local.Repository;

import br.com.Turismo_40.Entity.Local.Model.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {
    
    // Busca todos os locais ativos
    List<Local> findAllByAtivoTrue();
    
    // Busca locais por tags específicas
    @Query("SELECT l FROM Local l JOIN l.tags t WHERE t IN :tags AND l.ativo = true")
    List<Local> findByTagsIn(@Param("tags") List<String> tags);
    
    // Busca locais adequados para crianças
    List<Local> findByAdequadoCriancasAndAtivoTrue(Boolean adequadoCriancas);
    
    // Busca locais com opções vegetarianas
    List<Local> findByOpcoesVegetarianasAndAtivoTrue(Boolean opcoesVegetarianas);
    
    // Busca locais que não são ambientes noturnos
    List<Local> findByAmbienteNoturnoAndAtivoTrue(Boolean ambienteNoturno);
    
    // Busca locais por faixa de preço
    List<Local> findByPrecoMedioBetweenAndAtivoTrue(Double precoMin, Double precoMax);
    
    // Busca locais por avaliação mínima
    List<Local> findByAvaliacaoMediaGreaterThanEqualAndAtivoTrue(Double avaliacaoMinima);
    
    // Busca locais que contêm uma tag específica
    @Query("SELECT l FROM Local l JOIN l.tags t WHERE t = :tag AND l.ativo = true")
    List<Local> findByTag(@Param("tag") String tag);
    
    // Busca locais por nome (busca parcial)
    List<Local> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);
    
    // Busca locais próximos baseado em coordenadas (simplificado)
    @Query("SELECT l FROM Local l WHERE l.ativo = true AND " +
           "(6371 * acos(cos(radians(:lat)) * cos(radians(l.latitude)) * " +
           "cos(radians(l.longitude) - radians(:lng)) + sin(radians(:lat)) * " +
           "sin(radians(l.latitude)))) < :distancia")
    List<Local> findLocaisProximos(@Param("lat") Double latitude, 
                                   @Param("lng") Double longitude, 
                                   @Param("distancia") Double distanciaKm);
}