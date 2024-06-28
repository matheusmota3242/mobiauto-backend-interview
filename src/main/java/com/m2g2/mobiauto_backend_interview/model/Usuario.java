package com.m2g2.mobiauto_backend_interview.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.m2g2.mobiauto_backend_interview.enums.DescricaoPapel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Entity
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O campo 'primeiroNome' deve ser informado")
    private String primeiroNome;

    @NotBlank(message = "O campo 'sobrenome' deve ser informado")
    private String sobrenome;

    @Email
    @NotBlank(message = "O campo 'email' deve ser informado")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "O campo 'senha' deve ser informado")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "papel_usuario",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "papel_id"))
    @NotNull(message = "O campo 'papeis' deve ser informado")
    private Collection<Papel> papeis = new ArrayList<>();

    @ManyToOne
    private Revenda revenda;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Collection<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(Collection<Papel> papeis) {
        this.papeis = papeis;
    }

    public Revenda getRevenda() {
        return revenda;
    }

    public void setRevenda(Revenda revenda) {
        this.revenda = revenda;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return papeis.stream().map(papel ->
             new SimpleGrantedAuthority("ROLE_".concat(papel.getDescricao()))
        ).toList();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public boolean contemAlgumPapel(String... descricoes) {
        List<String> listaDescricoes = List.of(descricoes);
        return this.papeis.stream().anyMatch(papel ->
                listaDescricoes.contains(papel.getDescricao()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", primeiroNome='" + primeiroNome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", papeis=" + papeis +
                ", revenda=" + revenda +
                '}';
    }
}
