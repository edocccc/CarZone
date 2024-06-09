package com.polimi.carzone.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

//annotazione che definisce la classe Utente come entità nel database
@Entity(name = "Utente")
//annotazione che definisce la tabella a cui l'entità fa riferimento
@Table(name = "utente",
    uniqueConstraints = @UniqueConstraint(
            name="username_unique",
            columnNames = "username"
    )
)
//annotazioni per generare i getter e setter, costruttore vuoto e costruttore con tutti i parametri
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente implements UserDetails {
    //l'id è generato automaticamente ed è la chiave primaria della tabella Utente
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

    //definizione delle colonne della tabella Utente
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

    @Column(nullable = false)
    private LocalDate dataNascita;


    @Column(nullable = false)
    //definizione della colonna ruolo della tabella Utente
    //il tipo Ruolo non è supportato nel db, quindi viene mappato come stringa
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    // definizione della relazione tra la tabella Utente e la tabella Veicolo
    // Un utente può avere molti veicoli
    @OneToMany(mappedBy = "acquirente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Veicolo> veicoliAcquistati;

    // definizione della relazione tra la tabella Utente e la tabella Appuntamento
    // Un cliente può avere molti appuntamenti
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appuntamento> appuntamentiCliente;

    // definizione della relazione tra la tabella Utente e la tabella Appuntamento
    // Un dipendente può avere molti appuntamenti
    @OneToMany(mappedBy = "dipendente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appuntamento> appuntamentiDipendente;

    //la classe Utente implementa UserDetails, quindi deve implementare i metodi dell'interfaccia
    //questi metodi sono utilizzati da Spring Security per gestire l'autenticazione e l'autorizzazione
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
