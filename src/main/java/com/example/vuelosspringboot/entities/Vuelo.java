package com.example.vuelosspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Vuelo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vuelo extends Base {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_aerolinea")
    private Aerolinea aerolinea;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "vuelo_aeropuerto",
            joinColumns = @JoinColumn(name = "vuelo_id"),
            inverseJoinColumns = @JoinColumn(name = "aeropuerto_id")
    )
    private List<Aeropuerto> aeropuertos;

    // Campos opcionales - pueden ser null
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_avion")
    private Avion avion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_fecha")
    private Fecha fecha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_piloto")
    private Piloto piloto;

    @OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tarifa> tarifas;
}