package com.conta.conta.controller;

import com.conta.conta.controller.dto.AuthResponse;
import com.conta.conta.controller.dto.LoginRequest;
import com.conta.conta.controller.dto.RegisterRequest;
import com.conta.conta.security.JwtService;
import com.conta.conta.Repository.ContaRepository;
import com.conta.conta.Entity.Conta;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final ContaRepository contaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(ContaRepository repo, PasswordEncoder pe, AuthenticationManager am, JwtService js) {
        this.contaRepository = repo;
        this.passwordEncoder = pe;
        this.authenticationManager = am;
        this.jwtService = js;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        if (contaRepository.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }
        Conta conta = new Conta();
        try { conta.getClass().getMethod("setTitular", String.class).invoke(conta, req.titular()); } catch (Exception ignored) {}
        try { conta.getClass().getMethod("setCpf", String.class).invoke(conta, req.cpf()); } catch (Exception ignored) {}
        try { conta.getClass().getMethod("setEmail", String.class).invoke(conta, req.email()); } catch (Exception ignored) {}
        try { conta.getClass().getMethod("setTelefone", String.class).invoke(conta, req.telefone()); } catch (Exception ignored) {}
        try { conta.getClass().getMethod("setSenha", String.class).invoke(conta, passwordEncoder.encode(req.senha())); } catch (Exception ignored) {}
        try { conta.getClass().getMethod("setStatus", boolean.class).invoke(conta, true); } catch (Exception ignored) {}
        try { conta.getClass().getMethod("setDataCadastro", java.time.LocalDateTime.class).invoke(conta, java.time.LocalDateTime.now()); } catch (Exception ignored) {}

        contaRepository.save(conta);

        var user = User.withUsername(req.email()).password("N/A").roles("USER").build();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        var authToken = new UsernamePasswordAuthenticationToken(req.email(), req.senha());
        authenticationManager.authenticate(authToken);
        var user = User.withUsername(req.email()).password("N/A").roles("USER").build();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        return Map.of("email", auth.getName());
    }
}
