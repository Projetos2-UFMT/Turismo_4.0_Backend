package br.com.Turismo_40.Config;

import br.com.Turismo_40.Entity.User.Model.AppUser;
import br.com.Turismo_40.Entity.User.Repository.AppUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = Logger.getLogger(JwtUserDetailsService.class.getName());

    @Autowired
    private AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Carregando usuário com username: " + username);

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    LOGGER.severe("Usuário não encontrado: " + username);
                    return new UsernameNotFoundException("Usuário não encontrado: " + username);
                });

        // Obtém os papéis do usuário, usando uma lista vazia se não houver papéis
        List<SimpleGrantedAuthority> authorities = user.getRoles()
                .orElse(List.of()) // Retorna lista vazia se roles for null
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Adiciona prefixo ROLE_
                .collect(Collectors.toList());

        LOGGER.info("Usuário encontrado: " + user.getUsername() + ", roles: " + authorities);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}