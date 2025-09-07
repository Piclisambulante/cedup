package com.conta.conta.Repository; // ajuste se seu pacote for diferente

import com.conta.conta.Entity.Conta; // ajuste o pacote da sua entidade
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> { // troque Long pelo tipo do seu @Id, se for outro
    Optional<Conta> findByEmail(String email);   // ← necessário para o login
    boolean existsByEmail(String email);         // ← necessário para o cadastro
}
