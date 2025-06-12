package com.example.vuelosspringboot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Aerolinea")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Aerolinea extends Base{
    @Column(name = "nombreAerolinea")
    private String nombreAerolinea;
}
