package br.com.Turismo_40.Entity.Roteiro.Repository;

import br.com.Turismo_40.Entity.Roteiro.Model.Roteiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoteiroRepository extends JpaRepository<Roteiro, Long> {
    List<Roteiro> findByUserId(Long userId);
    List<Roteiro> findByUserIdAndCidade(Long userId, String cidade);
}