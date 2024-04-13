package com.polimi.carzone.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity(name = "Utente")
@Table(name = "utente",
    uniqueConstraints = @UniqueConstraint(
            name="username_unique",
            columnNames = "username"
    )
)
@Data
@NoArgsConstructor
public class Utente implements UserDetails {
    @Id
    @SequenceGenerator(
            name = "utente_sequence",
            sequenceName = "utente_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "utente_sequence",
            strategy = GenerationType.IDENTITY
    )
    @Column(updatable = false,unique = true,nullable = false)
    private long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false , updatable = false)
    private LocalDate dataNascita;


    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    @OneToMany(mappedBy = "acquirente")
    private List<Veicolo> veicoliAcquistati;

    @OneToMany(mappedBy = "cliente")
    private List<Appuntamento> appuntamentiCliente;

    @OneToMany(mappedBy = "dipendente")
    private List<Appuntamento> appuntamentiDipendente;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+ruolo);
        List<SimpleGrantedAuthority> list=List.of(authority);
        return list;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
