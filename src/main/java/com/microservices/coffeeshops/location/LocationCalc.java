package com.microservices.coffeeshops.location;

import com.microservices.coffeeshops.coffeeshop.CoffeeShop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class LocationCalc {
    public static final double MIN_COORD = -90.0;
    public static final double MAX_COORD = 90.0;

    @Value("${coffeeshops.default.radius-meters}")
    private double radiusMeters;

    @Value("${coffeeshops.default.min-max-coord-tolerance}")
    private double minMaxCoordTolerance;
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6378100; // Radius of Earth in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

   public boolean isWithinCoords(CoffeeShop cs, Location location) {
       return cs.getLocation().getLat() >= location.getLat() - minMaxCoordTolerance &&
               cs.getLocation().getLat() <= location.getLat() + minMaxCoordTolerance &&
               cs.getLocation().getLon() >= location.getLon() - minMaxCoordTolerance &&
               cs.getLocation().getLon() <= location.getLon() + minMaxCoordTolerance;
   }

    public Set<CoffeeShopDistance> scanForCoffeeShopsNearMe(Set<CoffeeShop> allCoffeeShops, Location location) {
        Set<CoffeeShopDistance> coffeeShops = new TreeSet<>(new CoffeeShopsDistanceComparator());

        Set<CoffeeShop> prelimFiltered =
                allCoffeeShops.stream().filter(cs -> isWithinCoords(cs, location)).collect(Collectors.toSet());

        prelimFiltered.stream().forEach(cs -> {
            double distance= calculateDistance(cs.getLocation().getLat(), cs.getLocation().getLon(),
                location.getLat(), location.getLon());
            if (distance <= radiusMeters) {
                coffeeShops.add(new CoffeeShopDistance(cs, distance));
            }
        });

        return coffeeShops;
    }

    public static class CoffeeShopDistance {
        private CoffeeShop coffeeShop;
        private double distance;

        public CoffeeShopDistance(CoffeeShop coffeeShop, double distance) {
            this.coffeeShop = coffeeShop;
            this.distance = distance;
        }

        public CoffeeShop getCoffeeShop() {
            return coffeeShop;
        }

        public void setCoffeeShop(CoffeeShop coffeeShop) {
            this.coffeeShop = coffeeShop;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public double getDistance() {
            return distance;
        }
    }

    public static class CoffeeShopsDistanceComparator implements Comparator<CoffeeShopDistance> {
        @Override
        public int compare(CoffeeShopDistance o1, CoffeeShopDistance o2) {
            if (o1.getDistance() > o2.getDistance()) {
                return 1;
            } else if (o1.getDistance() < o2.getDistance()) {
                return -1;
            }
            return 0;
        }
    }

    public double getRadiusMeters() {
        return radiusMeters;
    }

    public void setRadiusMeters(double radiusMeters) {
       radiusMeters = radiusMeters;
    }

    public double getMinMaxCoordTolerance() {
        return minMaxCoordTolerance;
    }

    public void setMinMaxCoordTolerance(double minMaxCoordTolerance) {
        minMaxCoordTolerance = minMaxCoordTolerance;
    }
}
