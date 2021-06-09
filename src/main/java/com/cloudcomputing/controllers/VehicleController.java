package com.cloudcomputing.controllers;

import com.cloudcomputing.dtos.VehicleGetDto;
import com.cloudcomputing.dtos.VehiclePostDto;
import com.cloudcomputing.entities.CarType;
import com.cloudcomputing.entities.Status;
import com.cloudcomputing.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;


    @PostMapping("/vehicles/add")
    public ResponseEntity addCarForSale(@RequestBody VehiclePostDto vehiclePostDto) {
        VehicleGetDto dto = vehicleService.createVehicle(vehiclePostDto);
        return new ResponseEntity(dto, OK);
    }

    @GetMapping("/vehicles")
    public ResponseEntity fetchVehiclesByType(@RequestParam("type") CarType type) {
        List<VehicleGetDto> vehicleList = vehicleService.fetchVehiclesByType(type);
        return new ResponseEntity(vehicleList, OK);
    }

    @GetMapping("/vehicles/own-vehicles/{ownerId}")
    public ResponseEntity fetchVehiclesByOwnerId(@PathVariable Long ownerId) {
        List<VehicleGetDto> vehicles = vehicleService.fetchVehicleList(ownerId);
        return new ResponseEntity(vehicles, OK);
    }

    @GetMapping("/vehicles/watch-list/{clientId}")
    public ResponseEntity fetchWatchList(@PathVariable Long clientId) {
        List<VehicleGetDto> watchList = vehicleService.fetchWatchList(clientId);
        return new ResponseEntity(watchList, OK);
    }

    @Transactional
    @PutMapping("/vehicles/{id}/price/{price}")
    public ResponseEntity modifyPrice(@PathVariable Long id, @PathVariable BigDecimal price) {
        BigDecimal newPrice = vehicleService.changePrice(id, price);
        return new ResponseEntity(newPrice, OK);
    }

    @PutMapping("/vehicles/{id}/status/{status}")
    public ResponseEntity changeStatus(@PathVariable Long id, @PathVariable Status status) {
        vehicleService.changeStatus(id,status);
        return new ResponseEntity("Succeed!", OK);
    }

    @PostMapping("/vehicles/{vehicleId}/subscribe/{clientId}")
    public ResponseEntity subscribeVehicle(@PathVariable Long vehicleId, @PathVariable Long clientId) {
        vehicleService.addToWatchList(clientId,vehicleId);
        return new ResponseEntity("Added to watch list", OK);
    }
}
