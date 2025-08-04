package br.com.Turismo_40.Entity.Evento.Controller;

import br.com.Turismo_40.Entity.Evento.Dto.EventoRequest;
import br.com.Turismo_40.Entity.Evento.Dto.EventoResponse;
import br.com.Turismo_40.Entity.Evento.Model.Evento;
import br.com.Turismo_40.Entity.Evento.Service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @PostMapping("/criar")
    public ResponseEntity<?> criarEvento(@RequestBody EventoRequest request) {
        try {
            Evento evento = eventoService.criarEvento(
                request.getCidade(),
                request.getNome(),
                request.getDescricao(),
                request.getHoraInicio(),
                request.getHoraFim(),
                request.getCusto(),
                request.getAmbiente()
            );

            EventoResponse response = convertToResponse(evento);
            response.setMessage("Evento criado com sucesso!");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao criar evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<?> listarEventosPorCidade(@PathVariable String cidade) {
        try {
            List<Evento> eventos = eventoService.listarEventosPorCidade(cidade);
            List<EventoResponse> responses = eventos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar eventos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{eventoId}")
    public ResponseEntity<?> buscarEventoPorId(@PathVariable Long eventoId) {
        try {
            Optional<Evento> eventoOpt = eventoService.buscarEventoPorId(eventoId);
            
            if (eventoOpt.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Evento não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            EventoResponse response = convertToResponse(eventoOpt.get());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{eventoId}")
    public ResponseEntity<?> deletarEvento(@PathVariable Long eventoId) {
        try {
            eventoService.deletarEvento(eventoId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Evento deletado com sucesso!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao deletar evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private EventoResponse convertToResponse(Evento evento) {
        EventoResponse response = new EventoResponse();
        response.setEventoId(evento.getEventoId());
        response.setCidade(evento.getCidade());
        response.setNome(evento.getNome());
        response.setDescricao(evento.getDescricao());
        response.setHoraInicio(evento.getHoraInicio());
        response.setHoraFim(evento.getHoraFim());
        response.setCusto(evento.getCusto());
        response.setAmbiente(evento.getAmbiente());
        return response;
    }
}