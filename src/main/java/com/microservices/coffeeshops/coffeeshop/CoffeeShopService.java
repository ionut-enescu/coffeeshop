package com.microservices.coffeeshops.coffeeshop;

import com.microservices.coffeeshops.location.Location;
import com.microservices.coffeeshops.location.LocationCalc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class CoffeeShopService {

    private static final Logger log = LoggerFactory.getLogger(CoffeeShopService.class);
    private static final Set<CoffeeShop> allCoffeeShops = new HashSet<>();

    @Autowired
    private LocationCalc locationUtil;

    @Value("${coffeeshops.filePath}")
    private String coffeeShopsFilePath;

    public Set<LocationCalc.CoffeeShopDistance> scanForCoffeeShopsNearMe(Location location) {
        return locationUtil.scanForCoffeeShopsNearMe(scanForAllCoffeeShops(), location);
    }

    public Set<CoffeeShop> scanForAllCoffeeShops() {
        if (allCoffeeShops.size() == 0) {
            allCoffeeShops.addAll(retrieveCoffeeShopsFromFile(new File(coffeeShopsFilePath)));
        }
        return allCoffeeShops;
    }

    private List<CoffeeShop> retrieveCoffeeShopsFromFile(File file) {
        List<CoffeeShop> allCoffeeShops = new ArrayList<>();
        try (Stream<String> lines = Files.lines(file.toPath())) {
            lines.filter(line -> !line.contains("latitude")).forEach(line -> {
                String[] oneLine = line.split(",");
                allCoffeeShops.add(new CoffeeShop(oneLine[0], new Location(Double.parseDouble(oneLine[1]), Double.parseDouble(oneLine[2]))));
            });
        } catch (IOException e) {
            log.error("Could not extract contents of file at: " + file.getAbsolutePath() + e.getMessage());
        }

        return allCoffeeShops;
    }


}
