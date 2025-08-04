package br.com.Turismo_40.Entity.Roteiro.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.Turismo_40.Entity.Roteiro.Model.RoteiroEvento;

import java.util.List;

@Repository
public interface RoteiroEventoRepository extends JpaRepository<RoteiroEvento, Long> {
    List<RoteiroEvento> findByRoteiroId(Long roteiroId);
}