package com.polimi.carzone.model;

import com.polimi.carzone.state.State;
import com.polimi.carzone.state.implementation.Disponibile;
import com.polimi.carzone.state.implementation.Venduto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "Veicolo")
@Table(name = "veicolo",
    uniqueConstraints = @UniqueConstraint(
            name="targa_unique",
            columnNames = "targa"
    )
)
@Data
public class Veicolo {
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

    @Column(unique = true,nullable = false)
    private String targa;

    @Column(unique = true,nullable = false)
    private String marca;

    @Column(unique = true,nullable = false)
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

    @Transient
    private State stato;

    @ManyToOne
    @JoinColumn(
            name = "id_cliente",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "utente_fk"
            )
    )
    private Utente acquirente;

    @OneToMany(mappedBy = "veicolo")
    private List<Appuntamento> appuntamentiVeicolo;

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
