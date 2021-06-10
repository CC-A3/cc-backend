package com.cloudcomputing.entities;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDBTable(tableName = "vehicle_img")
public class Image {

    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = "vehicle_id")
    private Long id;

    @DynamoDBAttribute(attributeName = "url")
    private String url;
}
