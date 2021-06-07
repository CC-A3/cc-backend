package com.cloudcomputing.services;

import com.cloudcomputing.dtos.LoginDto;
import com.cloudcomputing.entities.Client;
import com.cloudcomputing.exceptions.ClientNotFoundException;
import com.cloudcomputing.repositories.ClientRepository;
import com.cloudcomputing.utils.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final AuthenticationManager authenticationManager;
    private final ClientMapper clientMapper;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public LoginDto login(String email, String password){
        Client foundClient = clientRepository.findInvalidClientByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("No such client"));

        LoginDto userInfoDto = clientMapper.fromEntity(foundClient);
        Authentication authenticate = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        log.info(String.valueOf(authenticate));
        return userInfoDto;
    }




}
