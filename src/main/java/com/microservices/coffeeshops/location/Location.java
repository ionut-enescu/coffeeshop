package com.microservices.coffeeshops.location;

import com.microservices.coffeeshops.coffeeshop.CoffeeShop;
import jakarta.persistence.*;
import org.w3c.dom.ranges.RangeException;

@Entity(name="location")
public class Location {
    @Id
    @GeneratedValue
    private long id;
    private double lat;

    private double lon;

    @OneToOne
    @JoinColumn(name="id")
    private CoffeeShop coffeeShop;

    public Location(double lat, double lon) {
        if (lat < LocationCalc.MIN_COORD || lat > LocationCalc.MAX_COORD ||
                lon < LocationCalc.MIN_COORD || lon > LocationCalc.MAX_COORD) {
            throw new RangeException(RangeException.BAD_BOUNDARYPOINTS_ERR, "Illegal inputs for Location: " + lat + ", " + lon);
        }

        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }

}
