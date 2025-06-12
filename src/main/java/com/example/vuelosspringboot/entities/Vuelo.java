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

public class Vuelo extends Base{
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_avion")
    private Avion avion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            name = "vuelo_aeropuerto",
            joinColumns = @JoinColumn(name = "vuelo_id"),
            inverseJoinColumns = @JoinColumn(name = "aeropuerto_id")
    )
    private List<Aeropuerto> aeropuertos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_aerolinea")
    private Aerolinea aerolinea = new Aerolinea();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_fecha")
    private Fecha fecha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_piloto")
    private Piloto piloto;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(
            name = "vuelo_tarifa",
            joinColumns = @JoinColumn(name = "vuelo_id"),
            inverseJoinColumns = @JoinColumn(name = "tarifa_id")
    )
    private List<Tarifa> tarifas;
}
