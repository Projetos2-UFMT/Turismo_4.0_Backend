package br.com.Turismo_40.Entity.PerfilUsuario.Service;

import br.com.Turismo_40.Entity.PerfilUsuario.Model.PerfilUsuario;
import br.com.Turismo_40.Entity.PerfilUsuario.Repository.PerfilUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PerfilUsuarioService {

    @Autowired
    private PerfilUsuarioRepository perfilRepository;

    public PerfilUsuario criarPerfil(Long userId, PerfilUsuario.Estilo estilo, 
                                    PerfilUsuario.ContextoViagem contextoViagem, 
                                    String interesses, String restricoes) {
        if (perfilRepository.existsByUserId(userId)) {
            throw new RuntimeException("Perfil já existe para este usuário");
        }

        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setUserId(userId);
        perfil.setEstilo(estilo);
        perfil.setContextoViagem(contextoViagem);
        perfil.setInteresses(interesses);
        perfil.setRestricoes(restricoes);

        return perfilRepository.save(perfil);
    }

    public PerfilUsuario atualizarPerfil(Long userId, PerfilUsuario.Estilo estilo, 
                                        PerfilUsuario.ContextoViagem contextoViagem, 
                                        String interesses, String restricoes) {
        Optional<PerfilUsuario> perfilOpt = perfilRepository.findByUserId(userId);
        if (perfilOpt.isEmpty()) {
            throw new RuntimeException("Perfil não encontrado para este usuário");
        }

        PerfilUsuario perfil = perfilOpt.get();
        perfil.setEstilo(estilo);
        perfil.setContextoViagem(contextoViagem);
        perfil.setInteresses(interesses);
        perfil.setRestricoes(restricoes);

        return perfilRepository.save(perfil);
    }

    public Optional<PerfilUsuario> buscarPerfilPorUserId(Long userId) {
        return perfilRepository.findByUserId(userId);
    }
}