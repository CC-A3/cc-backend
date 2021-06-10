package com.cloudcomputing.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.cloudcomputing.entities.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final DynamoDBMapper mapper;

    public Image getImageById(Long id){
        return mapper.load(Image.class,id);
    }
}
