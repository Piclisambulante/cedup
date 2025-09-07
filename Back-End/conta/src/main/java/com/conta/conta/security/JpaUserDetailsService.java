package com.conta.conta.security;

import com.conta.conta.Repository.ContaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final ContaRepository contaRepository;

    public JpaUserDetailsService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var conta = contaRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return User.withUsername(conta.getEmail())
                .password(conta.getSenha())
                .roles("USER")
                .build();
    }
}
