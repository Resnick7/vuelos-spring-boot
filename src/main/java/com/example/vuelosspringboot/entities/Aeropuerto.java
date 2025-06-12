package com.example.vuelosspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Aeropuerto")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Aeropuerto extends Base{
    @Column(name = "nombreAeropuerto")
    private String nombreAeropuerto;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_ciudad")
    private Ciudad ciudad;
}
