package com.cloudcomputing.services;

import com.cloudcomputing.dtos.VehicleGetDto;
import com.cloudcomputing.dtos.VehiclePostDto;
import com.cloudcomputing.entities.*;
import com.cloudcomputing.exceptions.ClientNotFoundException;
import com.cloudcomputing.exceptions.VehicleNotFoundException;
import com.cloudcomputing.repositories.ClientRepository;
import com.cloudcomputing.repositories.ImageRepository;
import com.cloudcomputing.repositories.VehicleRepository;
import com.cloudcomputing.utils.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    private final VehicleMapper vehicleMapper;
    private final ImageRepository imageRepository;


public VehicleGetDto createVehicle(VehiclePostDto vehiclePostDto) {
    Vehicle vehicle = vehicleMapper.toEntity(vehiclePostDto);
    vehicle.setStatus(Status.AVAILABLE);
    vehicleRepository.save(vehicle);
    imageRepository.add(Image.builder()
            .id(vehicle.getId())
            .url(vehiclePostDto.getImgUrl())
            .build());

    VehicleGetDto dto = vehicleMapper.fromEntity(vehicle);
    dto.setImgUrl(vehiclePostDto.getImgUrl());
    return dto;
}

public List<VehicleGetDto> fetchVehiclesByType(CarType type, Long clientId) {
    List<Vehicle> vehicleList = vehicleRepository.findAllAvailableByTitle(type);
    List<VehicleGetDto> vehicles = new ArrayList<>();
    if(!vehicleList.isEmpty()){
        vehicles = vehicleList.stream()
                .map(vehicle -> vehicleMapper.fromEntity(vehicle))
                .collect(Collectors.toList());

        Client client = clientRepository.findById(clientId)
                .orElseThrow(()-> new ClientNotFoundException("No such client with id: " + clientId));
        for(VehicleGetDto v : vehicles) {
            Vehicle vehicle = vehicleRepository.findById(v.getId())
                    .orElseThrow(() -> new VehicleNotFoundException("No such car"));
            v.setIsWatched(vehicle.getClients().contains(client));
            v.setImgUrl(imageRepository.getImageById(v.getId()).getUrl());
        }
        Collections.sort(vehicles, new Comparator<VehicleGetDto>() {
            @Override
            public int compare(VehicleGetDto o1, VehicleGetDto o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });
    }
    return vehicles;
}

public VehicleGetDto fetchVehicleById(Long vehicleId,Long clientId) {
    Vehicle vehicle = vehicleRepository.findById(vehicleId)
            .orElseThrow(()->new VehicleNotFoundException("No such vehicle with id: " + vehicleId));
    Client client = clientRepository.findById(clientId)
            .orElseThrow(()-> new ClientNotFoundException("No such client with id: " + clientId));
    VehicleGetDto returnedDto = vehicleMapper.fromEntity(vehicle);
    returnedDto.setIsWatched(vehicle.getClients().contains(client));
    Image image = imageRepository.getImageById(vehicleId);
    returnedDto.setImgUrl(image.getUrl());
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
    for(VehicleGetDto v : vehicleList) {
        v.setImgUrl(imageRepository.getImageById(v.getId()).getUrl());

    }
    Collections.sort(vehicleList, new Comparator<VehicleGetDto>() {
        @Override
        public int compare(VehicleGetDto o1, VehicleGetDto o2) {
            return o2.getId().compareTo(o1.getId());
        }
    });
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
        for(VehicleGetDto v : returnedWatchList) {
            v.setIsWatched(true);
            v.setImgUrl(imageRepository.getImageById(v.getId()).getUrl());
        }
        Collections.sort(returnedWatchList, new Comparator<VehicleGetDto>() {
            @Override
            public int compare(VehicleGetDto o1, VehicleGetDto o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });
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

@Transactional
public BigDecimal changePrice(Long id, BigDecimal price) {
    vehicleRepository.updatePriceById(price,id);
    return price;
}

@Transactional
public void changeStatus(Long id, Status status) {
    vehicleRepository.updateStatusById(status, id);
}

}
