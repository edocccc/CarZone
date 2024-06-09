package com.polimi.carzone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// annotazione che definisce la classe Appuntamento come entità nel database
@Entity(name = "Appuntamento")
// annotazione che definisce la tabella a cui l'entità fa riferimento
@Table(name = "appuntamento",
    uniqueConstraints = @UniqueConstraint(
            name="id_unique",
            columnNames = "id"
    )
)
// annotazioni per generare i getter e setter, costruttore vuoto e costruttore con tutti i parametri
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appuntamento {
    // l'id è generato automaticamente ed è la chiave primaria della tabella Appuntamento
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
    @Column(updatable = false,unique = true,nullable = false)
    private long id;

    // definizione delle colonne della tabella Appuntamento
    @Column(nullable = false)
    private LocalDateTime dataOra;

    @Column()
    private Integer recensioneVoto;

    @Column()
    private String recensioneTesto;

    @Column(nullable = false)
    private boolean esitoRegistrato;

    // definizione della relazione tra la tabella Appuntamento e la tabella Utente
    // Un appuntamento è associato ad un cliente
    @ManyToOne
    @JoinColumn(
            name = "id_cliente",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "cliente_fk"
            )
    )
    private Utente cliente;

    // definizione della relazione tra la tabella Appuntamento e la tabella Utente
    // Un appuntamento è associato ad un dipendente
    @ManyToOne
    @JoinColumn(
            name = "id_diependente",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "dipendente_fk"
            )
    )
    private Utente dipendente;

    // definizione della relazione tra la tabella Appuntamento e la tabella Veicolo
    // Un appuntamento è associato ad un veicolo
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
