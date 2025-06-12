package com.example.vuelosspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@DiscriminatorValue("Tarjeta")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Tarjeta extends Pago{
    @Column(name = "numeroTarjeta")
    private int numeroTarjeta;

    @Enumerated(EnumType.STRING)
    private TipoTarjeta tipoTarjeta;
}
