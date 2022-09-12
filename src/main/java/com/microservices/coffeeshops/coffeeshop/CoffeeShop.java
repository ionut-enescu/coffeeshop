package com.microservices.coffeeshops.coffeeshop;

import com.microservices.coffeeshops.location.Location;
import jakarta.persistence.*;

@Entity
public class CoffeeShop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id")
    private Location location;
    private String name;

    public CoffeeShop (){}

    public CoffeeShop(String name, Location location) {
        if (name == null) {
            throw new RuntimeException("A CoffeeShop must have a non-null name!");
        }

        this.name = name;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CoffeeShop{" +
                "id=" + id +
                ", location=" + location +
                ", name='" + name + '\'' +
                '}';
    }
}
