package com.microservices.coffeeshops;

import com.microservices.coffeeshops.coffeeshop.CoffeeShop;
import com.microservices.coffeeshops.coffeeshop.CoffeeShopController;
import com.microservices.coffeeshops.location.Location;
import com.microservices.coffeeshops.location.LocationCalc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.w3c.dom.ranges.RangeException;

import java.util.Set;


@SpringBootTest
class CoffeeShopServiceApplicationTests {

	Logger logger = LoggerFactory.getLogger(CoffeeShopServiceApplicationTests.class);

	@Test
	void contextLoads() {
	}

	@Test
	void testLocationInputs() {

		Assertions.assertThrows(RangeException.class, () -> {
			new Location(-91, 45);
		});

		Assertions.assertThrows(RangeException.class, () -> {
			new Location(90.00001, 45);
		});

		Assertions.assertThrows(RangeException.class, () -> {
			new Location(90.0, 90.00001);
		});

		Assertions.assertThrows(RangeException.class, () -> {
			new Location(-90.0000, -90.00001);
		});

		Assertions.assertDoesNotThrow(() -> {
			new Location(25.8979, 45.9861);
		});

	}

	@Test
	void testScanForCoffeeShopsInputs() {
		CoffeeShopController coffeeShopController = new CoffeeShopController();

		ResponseEntity<Set<LocationCalc.CoffeeShopDistance>> response =
				coffeeShopController.scanForCoffeeShops("abc", "#$#$");
		Assertions.assertTrue(response.getStatusCode().is4xxClientError());
	}

	@Test
	void testCoffeeShopName() {
		Assertions.assertThrows(RuntimeException.class,
				() -> { CoffeeShop c =  new CoffeeShop(null, new Location(10.0, -78.0));
			});
	}

	@Test
	void testCoffeeShopDistance() {
		Assertions.assertThrows(RuntimeException.class,
				() -> { CoffeeShop c =  new CoffeeShop(null, new Location(10.0, -78.0));
				});
	}
}
