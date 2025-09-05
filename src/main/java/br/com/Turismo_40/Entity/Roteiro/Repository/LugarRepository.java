package br.com.Turismo_40.Entity.Roteiro.Repository;

import br.com.Turismo_40.Entity.Roteiro.Model.Lugar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LugarRepository extends JpaRepository<Lugar, Long> {
    List<Lugar> findByCidade(String cidade);
}