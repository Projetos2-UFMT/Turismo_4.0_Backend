package br.com.Turismo_40.Entity.Roteiro.Service;

import br.com.Turismo_40.Entity.Roteiro.Model.Lugar;
import br.com.Turismo_40.Entity.Roteiro.Model.Roteiro;
import br.com.Turismo_40.Entity.Roteiro.Repository.LugarRepository;
import br.com.Turismo_40.Entity.Roteiro.Repository.RoteiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoteiroService {

    @Autowired
    private RoteiroRepository roteiroRepository;

    @Autowired
    private LugarRepository lugarRepository;

    // Método original para criação de roteiro
    public Roteiro criarRoteiro(Long userId, String cidade, Roteiro.TempoDisponivel tempoDisponivel,
                                Roteiro.HorarioPreferido horarioPreferido, Double orcamento,
                                Roteiro.ModoTransporte modoTransporte, Roteiro.PreferenciaAmbiente preferenciaAmbiente,
                                Boolean incluirEventosSazonais) {
        Roteiro roteiro = new Roteiro();
        roteiro.setUserId(userId);
        roteiro.setCidade(cidade);
        roteiro.setTempoDisponivel(tempoDisponivel);
        roteiro.setHorarioPreferido(horarioPreferido);
        roteiro.setOrcamento(orcamento);
        roteiro.setModoTransporte(modoTransporte);
        roteiro.setPreferenciaAmbiente(preferenciaAmbiente);
        roteiro.setIncluirEventosSazonais(incluirEventosSazonais);
        roteiro.setCriadoEm(LocalDateTime.now());
        
        return roteiroRepository.save(roteiro);
    }
    
    /**
     * Prepara a string (prompt) para a IA.
     */
    public String prepararPromptParaIA(Long userId, String cidade, Roteiro.TempoDisponivel tempoDisponivel,
                                       Roteiro.HorarioPreferido horarioPreferido, Double orcamento,
                                       Roteiro.ModoTransporte modoTransporte, Roteiro.PreferenciaAmbiente preferenciaAmbiente,
                                       Boolean incluirEventosSazonais) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Gerar um roteiro de viagem com base nas seguintes informações:\n");
        promptBuilder.append("Cidade: ").append(cidade).append("\n");
        promptBuilder.append("Tempo disponível: ").append(tempoDisponivel.name().toLowerCase()).append("\n");
        promptBuilder.append("Horário preferido: ").append(horarioPreferido.name().toLowerCase()).append("\n");
        promptBuilder.append("Orçamento: ").append(orcamento).append(" reais\n");
        promptBuilder.append("Modo de transporte: ").append(modoTransporte.name().toLowerCase()).append("\n");
        promptBuilder.append("Preferencia de ambiente: ").append(preferenciaAmbiente.name().toLowerCase()).append("\n");
        promptBuilder.append("Incluir eventos sazonais: ").append(incluirEventosSazonais).append("\n");

        return promptBuilder.toString();
    }

    public List<Roteiro> listarRoteirosPorUsuario(Long userId) {
        return roteiroRepository.findByUserId(userId);
    }

    public Optional<Roteiro> buscarRoteiroPorId(Long roteiroId) {
        return roteiroRepository.findById(roteiroId);
    }

    public void deletarRoteiro(Long roteiroId) {
        roteiroRepository.deleteById(roteiroId);
    }

    /**
     * Busca os lugares disponíveis no banco de dados para a cidade informada.
     */
    public List<Lugar> buscarLugaresPorCidade(String cidade) {
        return lugarRepository.findByCidade(cidade);
    }
}