package com.cloudcomputing.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientProfileDto {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
}
