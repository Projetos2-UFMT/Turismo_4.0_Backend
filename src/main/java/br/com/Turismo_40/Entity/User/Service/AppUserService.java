package br.com.Turismo_40.Entity.User.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.Turismo_40.Entity.User.Model.AppUser;
import br.com.Turismo_40.Entity.User.Repository.AppUserRepository;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = userRepository.findByUsername(username);
        
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        
        AppUser userEntity = user.get();
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles("USER")
                .build();
    }
    
    public AppUser createUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Usuário já existe: " + username);
        }
        
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        
        return userRepository.save(user);
    }
    
    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public boolean authenticateUser(String username, String rawPassword) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        
        if (user.isEmpty()) {
            return false;
        }
        
        return passwordEncoder.matches(rawPassword, user.get().getPassword());
    }
}