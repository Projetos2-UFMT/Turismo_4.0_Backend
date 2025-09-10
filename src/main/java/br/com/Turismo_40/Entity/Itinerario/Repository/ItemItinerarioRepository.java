package br.com.Turismo_40.Entity.Itinerario.Repository;

import br.com.Turismo_40.Entity.Itinerario.Model.ItemItinerario;
import br.com.Turismo_40.Entity.Itinerario.Model.Itinerario;
import br.com.Turismo_40.Entity.Local.Model.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemItinerarioRepository extends JpaRepository<ItemItinerario, Long> {

    // Busca itens por itinerário ordenados por ordem
    List<ItemItinerario> findByItinerarioOrderByOrdem(Itinerario itinerario);

    // Busca itens visitados de um itinerário
    List<ItemItinerario> findByItinerarioAndVisitadoTrue(Itinerario itinerario);

    // Busca itens não visitados de um itinerário
    List<ItemItinerario> findByItinerarioAndVisitadoFalse(Itinerario itinerario);

    // Busca itens por local
    List<ItemItinerario> findByLocal(Local local);

    // Conta itens de um itinerário
    long countByItinerario(Itinerario itinerario);

    // Busca próximo item na ordem
    @Query("SELECT i FROM ItemItinerario i WHERE i.itinerario = :itinerario AND i.ordem > :ordem ORDER BY i.ordem ASC LIMIT 1")
    ItemItinerario findNextItem(@Param("itinerario") Itinerario itinerario, @Param("ordem") Integer ordem);
}