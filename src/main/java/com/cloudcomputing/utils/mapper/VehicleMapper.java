package com.cloudcomputing.utils.mapper;

import com.cloudcomputing.dtos.VehicleGetDto;
import com.cloudcomputing.dtos.VehiclePostDto;
import com.cloudcomputing.entities.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VehicleMapper {
    Vehicle toEntity(VehiclePostDto vehiclePostDto);

    VehicleGetDto fromEntity(Vehicle vehicle);
}
