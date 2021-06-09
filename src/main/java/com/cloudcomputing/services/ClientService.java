package com.cloudcomputing.services;

import com.cloudcomputing.config.FrontEndUrlConfig;
import com.cloudcomputing.dtos.ClientProfileDto;
import com.cloudcomputing.dtos.ClientPostDto;
import com.cloudcomputing.dtos.LoginGetDto;
import com.cloudcomputing.entities.Client;
import com.cloudcomputing.exceptions.ClientNotFoundException;
import com.cloudcomputing.exceptions.EmailConflictException;
import com.cloudcomputing.exceptions.PasswordIncorrectException;
import com.cloudcomputing.repositories.ClientRepository;
import com.cloudcomputing.utils.mapper.ClientMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final AuthenticationManager authenticationManager;
    private final ClientMapper clientMapper;
    private final FrontEndUrlConfig urlConfig;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public LoginGetDto login(String email, String password){
        Client foundClient = clientRepository.findInvalidClientByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("No such client"));

        LoginGetDto userInfoDto = clientMapper.fromEntity(foundClient);
        Authentication authenticate = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        log.info(String.valueOf(authenticate));
        return userInfoDto;
    }

    public boolean isClientVerified(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("No such client"));
        return client.isVerified();
    }

    public String createClientAndGenerateLink(ClientPostDto clientPostDto){
        if(clientRepository.existsClientByEmail(clientPostDto.getEmail())){
            throw new EmailConflictException("This email has been used!");
        }
        Client client = clientMapper.toEntity(clientPostDto);
        client.setPassword(passwordEncoder.encode(clientPostDto.getPassword()));
        client.setVerified(false);
        clientRepository.save(client);
        return generateVerifyLink(client.getEmail());
    };

    public String generateVerifyLink(String email) {
        String verifyLink = urlConfig.getUrl() + "/verify-link/verify?code=" + generateJws(email);
        log.info("Verify Link: {}", verifyLink);
        return verifyLink;
    }

    private String generateJws(String email) {
        String jws = Jwts.builder()
                .setSubject("signUp")
                .claim("email", email)
                .signWith(Keys.hmacShaKeyFor(this.jwtSecret.getBytes()))
                .compact();
        log.info("jwt token: " + jws);

        return jws;
    }

    @Transactional
    public Boolean verifyAccountWithJwt(String code) {
        String email = decodedEmail(code);
        int numberOfActiveUser = activateUser(email);

        log.debug("number of activated userEntity" + numberOfActiveUser);

        return numberOfActiveUser != 0;
    }

    private String decodedEmail(String code) {

        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(this.jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(code);
        Claims body = jws.getBody();

        return body.get("email").toString();
    }

    private int activateUser(String email) {
        return clientRepository.verifyByEmail(email);
    }


    public void checkPassword(Long id, String currentPassword) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("No client with " + id));
        if(!passwordEncoder.matches(currentPassword,client.getPassword())){
            log.info("input password: " + passwordEncoder.encode(currentPassword));
            log.info("real password: " + client.getPassword());
            throw new PasswordIncorrectException("The password is incorrect!");
        }
    }

    @Transactional
    public void changePassword(Long id, String newPassword) {
        clientRepository.updatePasswordById(id, passwordEncoder.encode(newPassword));
    }

    public ClientProfileDto fetchClientProfile(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("No client with Id: " + id));
        ClientProfileDto clientProfileDto = clientMapper.profileFromEntity(client);
        return clientProfileDto;
    }

    @Transactional
    public ClientProfileDto changeProfile(ClientProfileDto dto) {
        clientRepository.updateFullNameAndPhoneNumberById(dto.getId(),dto.getFullName(),dto.getPhoneNumber());
        ClientProfileDto returnedDto = clientMapper.profileFromEntity(clientRepository.findById(dto.getId()).get());
        return returnedDto;
    }




}
