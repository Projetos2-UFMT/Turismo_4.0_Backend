package br.com.Turismo_40.Entity.Itinerario.Repository;

import br.com.Turismo_40.Entity.Itinerario.Model.Itinerario;
import br.com.Turismo_40.Entity.User.Model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

    // Busca itinerários por usuário
    List<Itinerario> findByUsuarioOrderByDataCriacaoDesc(AppUser usuario);

    // Busca itinerários por usuário e status
    List<Itinerario> findByUsuarioAndStatusOrderByDataCriacaoDesc(AppUser usuario, Itinerario.StatusItinerario status);

    // Busca itinerários criados em um período
    List<Itinerario> findByDataCriacaoBetweenOrderByDataCriacaoDesc(LocalDateTime inicio, LocalDateTime fim);

    // Busca itinerários por usuário ID
    @Query("SELECT i FROM Itinerario i WHERE i.usuario.id = :usuarioId ORDER BY i.dataCriacao DESC")
    List<Itinerario> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Busca o último itinerário criado por um usuário
    @Query("SELECT i FROM Itinerario i WHERE i.usuario.id = :usuarioId ORDER BY i.dataCriacao DESC LIMIT 1")
    Itinerario findLatestByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Conta itinerários por status
    long countByStatus(Itinerario.StatusItinerario status);
}