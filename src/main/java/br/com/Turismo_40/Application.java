package br.com.Turismo_40;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    // Método principal que serve como ponto de entrada da aplicação
    public static void main(String[] args) {
        // Inicia a aplicação Spring Boot, carregando configurações e beans
        SpringApplication.run(Application.class, args);
    }
}

// Explicação:
// Esta classe é a entrada da aplicação Spring Boot. A anotação @SpringBootApplication
// indica que é uma aplicação Spring, habilitando a configuração automática e a varredura de componentes.
// O método main chama SpringApplication.run, que inicia o contexto da aplicação e permite que o servidor web
// (como Tomcat) seja executado, tornando a aplicação acessível via HTTP.