package br.com.Turismo_40.Entity.PerfilUsuario.Dto;

import br.com.Turismo_40.Entity.PerfilUsuario.Model.PerfilUsuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilUsuarioResponse {
    private Long userId;
    private PerfilUsuario.Estilo estilo;
    private PerfilUsuario.ContextoViagem contextoViagem;
    private String interesses;
    private String restricoes;
    private String message;
}