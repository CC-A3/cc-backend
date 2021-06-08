package com.cloudcomputing.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordDto {
    private Long id;
    private String previousPassword;
    private String newPassword;
}
