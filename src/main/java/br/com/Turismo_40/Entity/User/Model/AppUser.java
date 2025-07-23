package br.com.Turismo_40.Entity.User.Model;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;

    @ElementCollection // Para armazenar uma lista de strings no banco
    private List<String> roles; // Campo para armazenar os papéis

    public AppUser() {
    }

    public AppUser(long id, String username, String password, List<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Optional<List<String>> getRoles() {
        return Optional.ofNullable(roles); // Retorna os papéis como Optional
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}