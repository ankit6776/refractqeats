
/*
 *
 *  * Copyright (c) Crio.Do 2019. All rights reserved
 *
 */

package com.crio.qeats.services;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.repositoryservices.RestaurantRepositoryService;
import com.crio.qeats.repositoryservices.RestaurantRepositoryServiceDummyImpl;
import com.crio.qeats.utils.GeoLocation;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import org.mapstruct.BeanMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestaurantServiceImpl implements RestaurantService {

  private final Double peakHoursServingRadiusInKms = 3.0;
  private final Double normalHoursServingRadiusInKms = 5.0;
  @Autowired
  private RestaurantRepositoryService restaurantRepositoryService;

  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Implement findAllRestaurantsCloseby.
  // Check RestaurantService.java file for the interface contract.
  @Override
  public GetRestaurantsResponse findAllRestaurantsCloseBy(GetRestaurantsRequest getRestaurantsRequest,
      LocalTime currentTime) {
    Double latitude = getRestaurantsRequest.getLatitude();
    Double longitude = getRestaurantsRequest.getLongitude();
    int flag = 0;
    LocalTime t1 = LocalTime.of(7, 59);
    LocalTime t2 = LocalTime.of(10, 1);
    LocalTime t3 = LocalTime.of(12, 59);
    LocalTime t4 = LocalTime.of(14, 1);
    LocalTime t5 = LocalTime.of(18, 59);
    LocalTime t6 = LocalTime.of(21, 1);
    if (isBetween(currentTime, t1, t2) || isBetween(currentTime, t3, t4) || isBetween(currentTime, t5, t6)) {
      flag = 1;
    }
    double servingRadiusInKms;
    if (flag == 0) {
      servingRadiusInKms = 5.0;
    } else {
      servingRadiusInKms = 3.0;
    }

    List<Restaurant> result = restaurantRepositoryService.findAllRestaurantsCloseBy(latitude, longitude, currentTime,
        servingRadiusInKms);
    GetRestaurantsResponse getRestaurantsResponse = new GetRestaurantsResponse();
    getRestaurantsResponse.setRestaurants(result);
    return getRestaurantsResponse;
  }

  public static boolean isBetween(LocalTime candidate, LocalTime start, LocalTime end) {
    return !candidate.isBefore(start) && !candidate.isAfter(end); // Inclusive.
  }

}
