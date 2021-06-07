package com.cloudcomputing.auth;

import com.cloudcomputing.entities.Client;
import com.cloudcomputing.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {
    private final ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = mapToUserDetails(email);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("role:fake");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);

        return new User(client.getEmail(),
                client.getPassword().replaceAll("\\s+", ""),
                authorities);
    }

    private Client mapToUserDetails (String email) {
        return clientRepository.findInvalidClientByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("No user with %s", email)));
    }
}
