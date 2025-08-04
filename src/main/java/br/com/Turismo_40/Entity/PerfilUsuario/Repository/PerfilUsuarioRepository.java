package br.com.Turismo_40.Entity.PerfilUsuario.Repository;

import br.com.Turismo_40.Entity.PerfilUsuario.Model.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Long> {
    Optional<PerfilUsuario> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}