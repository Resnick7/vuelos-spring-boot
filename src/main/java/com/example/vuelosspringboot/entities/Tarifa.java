package com.example.vuelosspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Tarifa")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Tarifa extends Base{
    @Column(name = "impuestoTarifa")
    private int impuestoTarifa;

    @Column(name = "precioTarifa")
    private int precioTarifa;

    @Enumerated(EnumType.STRING)
    private Clase claseTarifa;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "vuelo_id")
    private Vuelo vuelo;
}
