package com.cloudcomputing.repositories;

import com.cloudcomputing.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    @Query("select c from Client c where c.email = :email and c.isVerified = true ")
    Optional<Client> findInvalidClientByEmail(@Param("email") String email);

    boolean existsClientByEmail(String email);

    @Query("select case when count(c)>0 then true else false end from Client c where c.email = :email")
    boolean existsClientByEmailAndVerifiedFalse(@Param("email") String email);

    @Modifying
    @Query("update Client c set c.isVerified = true where c.email = :email")
    int verifyByEmail(@Param("email") String email);

    @Modifying
    @Query("update Client c set c.password = :password where c.id = :id")
    int updatePasswordById(@Param("id") Long id, @Param("password") String password);

    @Modifying
    @Query("update Client c set c.fullName = :fullName, c.phoneNumber = :phoneNumber where c.id = :id")
    int updateFullNameAndPhoneNumberById(@Param("id") Long id,
                                         @Param("fullName") String fullName,
                                         @Param("phoneNumber") String phoneNumber);

}
