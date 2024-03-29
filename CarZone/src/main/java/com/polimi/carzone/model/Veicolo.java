package com.polimi.carzone.model;

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
@NoArgsConstructor
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
    private char prezzo;

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
}
