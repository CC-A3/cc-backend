package com.cloudcomputing.controllers;

import com.cloudcomputing.dtos.*;
import com.cloudcomputing.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginPostDto loginPostDto) {
        if(!clientService.isClientVerified(loginPostDto.getEmail())){
            return new ResponseEntity(clientService.generateVerifyLink(loginPostDto.getEmail()), NON_AUTHORITATIVE_INFORMATION);
        }
        LoginGetDto loginGetDto = clientService.login(loginPostDto.getEmail(), loginPostDto.getPassword());
        return new ResponseEntity(loginGetDto, OK);
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody ClientPostDto clientPostDto) {
        String link = clientService.createClientAndGenerateLink(clientPostDto);
        return new ResponseEntity(link, OK);
    }

    @PutMapping("/verify")
    public ResponseEntity verifyClient(@RequestParam(value = "code") String code) throws URISyntaxException {
        log.info("Encrypted Code:", code);
        if(clientService.verifyAccountWithJwt(code)){
            return new ResponseEntity("Succeed!", OK);
        }
        return new ResponseEntity("Fail to verify", NON_AUTHORITATIVE_INFORMATION);
    }

    @PutMapping("/change-password")
    public ResponseEntity changePassword(@RequestBody PasswordDto passwordDto){
        log.info("Id:", passwordDto.getId());
        clientService.checkPassword(passwordDto.getId(),passwordDto.getPreviousPassword());
        clientService.changePassword(passwordDto.getId(), passwordDto.getNewPassword());
        return new ResponseEntity("Successful modification", OK);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity fetchClientProfile(@PathVariable Long id) {
        log.info("Client id ---> ", id);
        ClientProfileDto dto = clientService.fetchClientProfile(id);
        return new ResponseEntity(dto, OK);
    }

}
