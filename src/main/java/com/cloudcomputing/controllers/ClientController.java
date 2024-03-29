package com.cloudcomputing.controllers;

import com.cloudcomputing.dtos.*;
import com.cloudcomputing.services.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/login") //done
    public ResponseEntity login(@RequestBody LoginPostDto loginPostDto) {
        if(!clientService.isClientVerified(loginPostDto.getEmail())){
            return new ResponseEntity(clientService.generateVerifyLink(loginPostDto.getEmail()), NON_AUTHORITATIVE_INFORMATION);
        }
        LoginGetDto loginGetDto = clientService.login(loginPostDto.getEmail(), loginPostDto.getPassword());
        return new ResponseEntity(loginGetDto, OK);
    }

    @PostMapping("/signup") //done
    public ResponseEntity signUp(@RequestBody ClientPostDto clientPostDto) {
        String link = clientService.createClientAndGenerateLink(clientPostDto);
        return new ResponseEntity(link, OK);
    }

    @PutMapping("/verify") //done
    public ResponseEntity verifyClient(@RequestParam(value = "code") String code) throws URISyntaxException {
        log.info("Encrypted Code:", code);
        if(clientService.verifyAccountWithJwt(code)){
            return new ResponseEntity("Succeed!", OK);
        }
        return new ResponseEntity("Fail to verify", NON_AUTHORITATIVE_INFORMATION);
    }

    @PutMapping("/change-password") //done
    public ResponseEntity changePassword(@RequestBody PasswordDto passwordDto){
        log.info("Id:", passwordDto.getId());
        clientService.checkPassword(passwordDto.getId(),passwordDto.getPreviousPassword());
        clientService.changePassword(passwordDto.getId(), passwordDto.getNewPassword());
        return new ResponseEntity("Successful modification", OK);
    }

    @GetMapping("/profile/{id}") //done
    public ResponseEntity fetchClientProfile(@PathVariable Long id) {
        log.info("Client id ---> ", id);
        ClientProfileDto dto = clientService.fetchClientProfile(id);
        return new ResponseEntity(dto, OK);
    }

    @PutMapping("/profile/change") //done
    public ResponseEntity changeProfile(@RequestBody ClientProfileDto clientProfileDto) {
        ClientProfileDto dto = clientService.changeProfile(clientProfileDto);
        return new ResponseEntity(dto, OK);
    }


}
