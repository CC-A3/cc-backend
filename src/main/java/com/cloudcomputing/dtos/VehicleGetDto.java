package com.cloudcomputing.dtos;

import com.cloudcomputing.entities.CarType;
import com.cloudcomputing.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleGetDto {

    private Long id;

    private String title;

    private BigDecimal price;

    private Integer kilometers;

    private String colour;

    private String body;

    private String engine;

    private String transmission;

    private String fuelConsumption;

    private CarType type;

    private Status status;

    private Boolean isWatched;

    private Long ownerId;

    private String imgUrl;
}
