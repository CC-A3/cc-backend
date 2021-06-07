package com.cloudcomputing.repositories;

import com.cloudcomputing.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    @Query("select c from Client c where c.email = :email and c.isVerified = true ")
    Optional<Client> findInvalidClientByEmail(String email);

}
