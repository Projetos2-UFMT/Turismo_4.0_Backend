package br.com.Turismo_40.Entity.Notificacao.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.Turismo_40.Entity.Notificacao.Model.Notificacao;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByRoteiroId(Long roteiroId);
}