package com.polimi.carzone.model;

import com.polimi.carzone.state.State;
import com.polimi.carzone.state.implementation.Disponibile;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
// annotazione che definisce la classe Veicolo come entità nel database
@Entity(name = "Veicolo")
// annotazione che definisce la tabella a cui l'entità fa riferimento
@Table(name = "veicolo",
    uniqueConstraints = @UniqueConstraint(
            name="targa_unique",
            columnNames = "targa"
    )
)
// annotazione per generare i getter e setter
@Data
public class Veicolo {
    // l'id è generato automaticamente ed è la chiave primaria della tabella Veicolo
    @Id
    @SequenceGenerator(
            name = "veicolo_sequence",
            sequenceName = "veicolo_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "veicolo_sequence",
            strategy = GenerationType.IDENTITY
    )
    @Column(updatable = false,unique = true,nullable = false)
    private long id;

    // definizione delle colonne della tabella Veicolo
    @Column(unique = true,nullable = false)
    private String targa;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modello;

    @Column(nullable = false)
    private int chilometraggio;

    @Column(nullable = false)
    private int annoProduzione;

    @Column(nullable = false)
    private int potenzaCv;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Alimentazione alimentazione;

    @Column(nullable = false)
    private double prezzo;

    @Column
    private String nomeImmagine;

    @Column
    private String tipoImmagine;

    @Column
    private String filePath;

    // lo stato del veicolo è un campo transient, quindi non viene salvato nel database
    // viene utilizzato per definire il comportamento del veicolo in base allo stato (pattern state)
    @Transient
    private State stato;

    // definizione della relazione tra la tabella Veicolo e la tabella Utente
    // Un veicolo è associato ad un solo acquirente, se è stato venduto
    @ManyToOne
    @JoinColumn(
            name = "id_cliente",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "utente_fk"
            )
    )
    private Utente acquirente;

    // definizione della relazione tra la tabella Veicolo e la tabella Appuntamento
    // Un veicolo può avere molti appuntamenti
    @OneToMany(mappedBy = "veicolo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appuntamento> appuntamentiVeicolo;

    // costruttori della classe Veicolo utili per l'implementazione del pattern state
    public Veicolo() {
        this.stato = new Disponibile(this);
    }

    public void cambiaStato(State stato) {
        this.stato = stato;
    }

    @SuppressWarnings("getter del pattern state")
    public State getStato() {
        return this.stato;
    }
}
