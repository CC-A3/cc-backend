package com.cloudcomputing.utils.mapper;

import com.cloudcomputing.dtos.ClientPostDto;
import com.cloudcomputing.dtos.ClientProfileDto;
import com.cloudcomputing.dtos.LoginGetDto;
import com.cloudcomputing.entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {
    LoginGetDto fromEntity(Client client);
    Client toEntity(ClientPostDto postDto);
    ClientProfileDto profileFromEntity(Client client);
}
