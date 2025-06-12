package com.example.vuelosspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Reserva")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Reserva extends Base{
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_pago")
    private Pago pago;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_vuelo")
    private Vuelo vueloReservado;

    @ManyToOne
    @JoinColumn(name = "fk_usuario")
    private Usuario usuario;
}
