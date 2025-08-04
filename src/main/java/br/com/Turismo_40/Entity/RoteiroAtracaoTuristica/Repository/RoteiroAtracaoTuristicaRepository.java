package br.com.Turismo_40.Entity.RoteiroAtracaoTuristica.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.Turismo_40.Entity.RoteiroAtracaoTuristica.Model.RoteiroAtracaoTuristica;

import java.util.List;

@Repository
public interface RoteiroAtracaoTuristicaRepository extends JpaRepository<RoteiroAtracaoTuristica, Long> {
    List<RoteiroAtracaoTuristica> findByRoteiroIdOrderByOrdemSequencia(Long roteiroId);
}