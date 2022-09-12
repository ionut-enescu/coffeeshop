package com.microservices.coffeeshops.coffeeshop;

import com.microservices.coffeeshops.location.Location;
import com.microservices.coffeeshops.location.LocationCalc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

@RestController
@RequestMapping("/coffeeshop/api/v1")
public class CoffeeShopController {
    private final Logger logger = LoggerFactory.getLogger(CoffeeShopController.class);

    @Autowired
    private CoffeeShopService coffeeShopService;

    @GetMapping("/nearme/{lat}/{lon}")
    public ResponseEntity<Set<LocationCalc.CoffeeShopDistance>> scanForCoffeeShops(@PathVariable String lat, @PathVariable String lon) {
        double latitude, longitude;

        try {
            latitude = Double.parseDouble(lat);
            longitude = Double.parseDouble(lon);
        } catch (NumberFormatException e) {
            logger.error("Cannot parse as double: {}, {} ", lat, lon);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(coffeeShopService.scanForCoffeeShopsNearMe(new Location(latitude, longitude)));
    }

    @GetMapping("/coffeeshops")
    public ResponseEntity<Set<CoffeeShop>> scanForAllCoffeeShops() {
        return ResponseEntity.ok(coffeeShopService.scanForAllCoffeeShops());
    }
}
