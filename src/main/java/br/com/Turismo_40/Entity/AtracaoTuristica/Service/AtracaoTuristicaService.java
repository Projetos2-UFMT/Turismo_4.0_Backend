package br.com.Turismo_40.Entity.AtracaoTuristica.Service;

import br.com.Turismo_40.Entity.AtracaoTuristica.Model.AtracaoTuristica;
import br.com.Turismo_40.Entity.AtracaoTuristica.Repository.AtracaoTuristicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AtracaoTuristicaService {

    @Autowired
    private AtracaoTuristicaRepository atracaoRepository;

    public AtracaoTuristica criarAtracao(String cidade, String nome, AtracaoTuristica.Categoria categoria,
                                        String descricao, Double custo, String horarioFuncionamento,
                                        AtracaoTuristica.Ambiente ambiente, Double latitude, Double longitude) {
        AtracaoTuristica atracao = new AtracaoTuristica();
        atracao.setCidade(cidade);
        atracao.setNome(nome);
        atracao.setCategoria(categoria);
        atracao.setDescricao(descricao);
        atracao.setCusto(custo);
        atracao.setHorarioFuncionamento(horarioFuncionamento);
        atracao.setAmbiente(ambiente);
        atracao.setLatitude(latitude);
        atracao.setLongitude(longitude);

        return atracaoRepository.save(atracao);
    }

    public List<AtracaoTuristica> listarAtracoesPorCidade(String cidade) {
        return atracaoRepository.findByCidade(cidade);
    }

    public List<AtracaoTuristica> buscarAtracoesPorCategoria(String cidade, AtracaoTuristica.Categoria categoria) {
        return atracaoRepository.findByCidadeAndCategoria(cidade, categoria);
    }

    public List<AtracaoTuristica> buscarAtracoesPorOrcamento(String cidade, Double orcamento) {
        return atracaoRepository.findAtracoesPorOrcamento(cidade, orcamento);
    }

    public List<AtracaoTuristica> buscarAtracoesPorAmbiente(String cidade, AtracaoTuristica.Ambiente ambiente) {
        return atracaoRepository.findAtracoesPorAmbiente(cidade, ambiente);
    }

    public Optional<AtracaoTuristica> buscarAtracaoPorId(Long atracaoId) {
        return atracaoRepository.findById(atracaoId);
    }

    public void deletarAtracao(Long atracaoId) {
        atracaoRepository.deleteById(atracaoId);
    }
}