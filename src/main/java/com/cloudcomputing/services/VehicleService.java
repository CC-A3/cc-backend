package com.cloudcomputing.services;

import com.cloudcomputing.dtos.VehicleGetDto;
import com.cloudcomputing.dtos.VehiclePostDto;
import com.cloudcomputing.entities.CarType;
import com.cloudcomputing.entities.Client;
import com.cloudcomputing.entities.Status;
import com.cloudcomputing.entities.Vehicle;
import com.cloudcomputing.exceptions.ClientNotFoundException;
import com.cloudcomputing.exceptions.VehicleNotFoundException;
import com.cloudcomputing.repositories.ClientRepository;
import com.cloudcomputing.repositories.VehicleRepository;
import com.cloudcomputing.utils.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final VehicleMapper vehicleMapper;

public VehicleGetDto createVehicle(VehiclePostDto vehiclePostDto) {
    Vehicle vehicle = vehicleMapper.toEntity(vehiclePostDto);
    vehicle.setStatus(Status.AVAILABLE);
    vehicleRepository.save(vehicle);
    return vehicleMapper.fromEntity(vehicle);
}

public List<VehicleGetDto> fetchVehiclesByType(CarType type) {
    List<Vehicle> vehicleList = vehicleRepository.findAllAvailableByTitle(type);
    List<VehicleGetDto> vehicles = new ArrayList<>();
    if(!vehicleList.isEmpty()){
        vehicles = vehicleList.stream()
                .map(vehicle -> vehicleMapper.fromEntity(vehicle))
                .collect(Collectors.toList());
    }
    return vehicles;
}

public VehicleGetDto fetchVehicleById(Long id) {
    Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(()->new VehicleNotFoundException("No such vehicle with id: " + id));
    VehicleGetDto returnedDto = vehicleMapper.fromEntity(vehicle);
    return returnedDto;
}

public List<VehicleGetDto> fetchVehicleList(Long ownerId) {
    List<Vehicle> list = vehicleRepository.findByOwnerId(ownerId);
    List<VehicleGetDto> vehicleList = new ArrayList<>();
    if(!list.isEmpty()) {
        vehicleList = list.stream()
                .map(vehicle -> vehicleMapper.fromEntity(vehicle))
                .collect(Collectors.toList());
    }
    return vehicleList;
}

public List<VehicleGetDto> fetchWatchList(Long clientId) {
    Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ClientNotFoundException("No such client with id: " + clientId));
    List<Vehicle> watchList = client.getVehicles();
    List<VehicleGetDto> returnedWatchList = new ArrayList<>();
    if(watchList != null){
        returnedWatchList = watchList.stream()
                .map(vehicle -> vehicleMapper.fromEntity(vehicle))
                .collect(Collectors.toList());
    }
    return returnedWatchList;
}

public void addToWatchList(Long clientId, Long vehicleId) {
    Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ClientNotFoundException("No such client with id: " + clientId));
    Vehicle vehicle = vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new VehicleNotFoundException("No such vehicle with id: " + vehicleId));

    client.getVehicles().add(vehicle);
    vehicle.getClients().add(client);

    clientRepository.save(client);
    vehicleRepository.save(vehicle);

}

public void removeFromWatchList(Long clientId, Long vehicleId) {
    Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ClientNotFoundException("No such client with id: " + clientId));
    Vehicle vehicle = vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new VehicleNotFoundException("No such vehicle with id: " + vehicleId));

    client.getVehicles().remove(vehicle);
    vehicle.getClients().remove(client);

    clientRepository.save(client);
    vehicleRepository.save(vehicle);

}

public BigDecimal changePrice(Long id, BigDecimal price) {
    vehicleRepository.updatePriceById(price,id);
    return price;
}

public void changeStatus(Long id, Status status) {
    vehicleRepository.updateStatusById(status, id);
}


}
