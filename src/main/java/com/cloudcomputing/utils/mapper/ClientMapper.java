package com.cloudcomputing.utils.mapper;

import com.cloudcomputing.dtos.LoginDto;
import com.cloudcomputing.entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {
    LoginDto fromEntity(Client client);
}
