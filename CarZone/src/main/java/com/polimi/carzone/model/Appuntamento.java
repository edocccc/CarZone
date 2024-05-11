package com.polimi.carzone.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Appuntamento")
@Table(name = "appuntamento",
    uniqueConstraints = @UniqueConstraint(
            name="id_unique",
            columnNames = "id"
    )
)
@Data
@NoArgsConstructor
public class Appuntamento {
    @Id
    @SequenceGenerator(
            name = "appuntamento_sequence",
            sequenceName = "appuntamento_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "appuntamento_sequence",
            strategy = GenerationType.IDENTITY
    )
    private long id;

    @Column(updatable = false, nullable = false)
    private LocalDateTime dataOra;

    @Column()
    private Integer recensioneVoto;

    @Column()
    private String recensioneTesto;

    @Column(nullable = false)
    private boolean esitoRegistrato;

    @ManyToOne
    @JoinColumn(
            name = "id_cliente",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "cliente_fk"
            )
    )
    private Utente cliente;

    @ManyToOne
    @JoinColumn(
            name = "id_diependente",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "dipendente_fk"
            )
    )
    private Utente dipendente;

    @ManyToOne
    @JoinColumn(
            name = "id_veicolo",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "veicolo_fk"
            )
    )
    private Veicolo veicolo;
}
