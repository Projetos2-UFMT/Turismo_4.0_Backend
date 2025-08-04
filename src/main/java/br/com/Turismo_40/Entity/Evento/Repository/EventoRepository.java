package br.com.Turismo_40.Entity.Evento.Repository;

import br.com.Turismo_40.Entity.Evento.Model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByCidade(String cidade);
    
    @Query("SELECT e FROM Evento e WHERE e.cidade = :cidade AND e.horaInicio >= :dataInicio AND e.horaFim <= :dataFim")
    List<Evento> findEventosDisponiveis(@Param("cidade") String cidade, 
                                       @Param("dataInicio") LocalDateTime dataInicio, 
                                       @Param("dataFim") LocalDateTime dataFim);
    
    @Query("SELECT e FROM Evento e WHERE e.cidade = :cidade AND e.custo <= :orcamento")
    List<Evento> findEventosPorOrcamento(@Param("cidade") String cidade, @Param("orcamento") Double orcamento);
}