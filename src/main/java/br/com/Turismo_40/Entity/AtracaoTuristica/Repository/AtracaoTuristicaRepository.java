package br.com.Turismo_40.Entity.AtracaoTuristica.Repository;

import br.com.Turismo_40.Entity.AtracaoTuristica.Model.AtracaoTuristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtracaoTuristicaRepository extends JpaRepository<AtracaoTuristica, Long> {
    List<AtracaoTuristica> findByCidade(String cidade);
    List<AtracaoTuristica> findByCidadeAndCategoria(String cidade, AtracaoTuristica.Categoria categoria);
    
    @Query("SELECT a FROM AtracaoTuristica a WHERE a.cidade = :cidade AND a.custo <= :orcamento")
    List<AtracaoTuristica> findAtracoesPorOrcamento(@Param("cidade") String cidade, @Param("orcamento") Double orcamento);
    
    @Query("SELECT a FROM AtracaoTuristica a WHERE a.cidade = :cidade AND a.ambiente = :ambiente")
    List<AtracaoTuristica> findAtracoesPorAmbiente(@Param("cidade") String cidade, @Param("ambiente") AtracaoTuristica.Ambiente ambiente);
}