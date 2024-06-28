package com.m2g2.mobiauto_backend_interview.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O campo 'primeiroNome' deve ser preenchido.")
    private String primeiroNome;

    @NotBlank(message = "O campo 'sobrenome' deve ser preenchido.")
    private String sobrenome;

    @Email
    @NotBlank(message = "O campo 'email' deve ser preenchido.")
    @Column(unique = true)
    private String email;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "O campo 'telefone' deve seguir o padrão (99) 99999-9999.")
    private String telefone;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return telefone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.telefone = phoneNumber;
    }
}
