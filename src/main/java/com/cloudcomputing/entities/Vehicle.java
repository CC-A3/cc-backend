package com.cloudcomputing.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Type(type = "long")
    @Column(name = "id")
    private Long id;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "kilometers", nullable = false)
    private Integer kilometers;

    @Column(name = "colour", nullable = false)
    private String colour;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "engine", nullable = false)
    private String engine;

    @Column(name = "transmission", nullable = false)
    private String transmission;

    @Column(name = "fuel_consumption", nullable = false)
    private String fuelConsumption;

    @Column(name = "type", nullable = false)
    private CarType type;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "owner_Id", nullable = false)
    private Long ownerId;

    @ManyToMany(mappedBy = "vehicles")
    private List<Client> clients;

}
