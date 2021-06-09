package com.cloudcomputing.repositories;

import com.cloudcomputing.entities.CarType;
import com.cloudcomputing.entities.Status;
import com.cloudcomputing.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("select v from Vehicle v where v.type = :type and v.status = 'AVAILABLE'")
    List<Vehicle> findAllAvailableByTitle(CarType type);

    List<Vehicle> findByOwnerId(Long ownerId);

    @Modifying
    @Query("update Vehicle v set v.price = :price where v.id = :id")
    int updatePriceById(@Param("price") BigDecimal price, @Param("id") Long id);

    @Modifying
    @Query("update Vehicle v set v.status = :status where v.id = :id")
    int updateStatusById(@Param("status") Status status, @Param("id") Long id);
}
