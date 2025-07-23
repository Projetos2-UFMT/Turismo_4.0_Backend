package br.com.Turismo_40.Entidades.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.Turismo_40.Entidades.Model.UserModel;
import br.com.Turismo_40.Entidades.Repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> user = userRepository.findByUsername(username);
        
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        
        UserModel userModel = user.get();
        
        return User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .roles("USER") // Você pode expandir isso para roles dinâmicas
                .build();
    }
    
    public UserModel createUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Usuário já existe: " + username);
        }
        
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        
        return userRepository.save(user);
    }
    
    public Optional<UserModel> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public boolean authenticateUser(String username, String rawPassword) {
        Optional<UserModel> user = userRepository.findByUsername(username);
        
        if (user.isEmpty()) {
            return false;
        }
        
        return passwordEncoder.matches(rawPassword, user.get().getPassword());
    }
}