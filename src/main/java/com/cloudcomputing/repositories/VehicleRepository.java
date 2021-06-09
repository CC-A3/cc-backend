package com.cloudcomputing.repositories;

import com.cloudcomputing.entities.CarType;
import com.cloudcomputing.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findAllByTitleEquals(CarType type);


}
