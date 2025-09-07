package com.conta.conta.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String titular,
        @NotBlank String cpf,
        @Email @NotBlank String email,
        @NotBlank String telefone,
        @NotBlank String senha
) {}
