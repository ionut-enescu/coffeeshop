CoffeeShop API v1
-
----

The CoffeeShop API v1 reads from a .csv file all the coffeeshops and displays them either by proximity to given coordinates or all-in-one take for debug purposes.

Usage examples:
-
- http://localhost:9090/coffeeshop/api/v1/nearme/45.5/47.5
- http://localhost:9090/coffeeshop/api/v1/nearme/-48.545/-49.6879
- http://localhost:9090/coffeeshop/api/v1/coffeeshops

Entities:
-
- Location - pair of latitude + longitude coordinates.
- CoffeeShop - mapping between a Location and CoffeeShop name.
- CoffeeShopDistance - mapping between a CoffeeShop and distance in meters to a given Location.

Nearby CoffeeShops algorithm:
- 
- In the absence of an actual database, a .csv file serves as the source of information, containing all CoffeeShops which are indexed by name, longitude, latitude. The file is assumed to contain completely unsorted data, therefore no partial reads could help improve performance. For demo purposes, two randomized sets have been added consisting of 100k pairs of lat/lon data each, representing the [-50.0, -45.0] and [45.0, 50.0] coordinates intervals.

- first, a filtering by a tuneable tolerance (default: 0.05°) to given coordinates is performed, to avoid extra calculation on unnecesary CoffeeShop entries. Then, the filtered list is furthermore refined by comparing it to a tuneable distance (default: 5km).
Results are returned ordered by proximity to given initial coordinates.

Observations:
-
- for this version no optimization was introduced for reading from the file. It is irrelevant for the scope of the task. Moreover, in a real scenario, the presence of a database would greatly improve retrieving/filtering for the needed data.
the H2 database was added as a dependency for testing purposes initially, but due to some errors while attempting its removal, it was left in there, together with the CoffeeShop JpaRepository. It is not used at this point.

Other possible implementations:
-
- splitting the whole area of the Earth into polygons and create mappings between each polygons and the CoffeeShop locations within those polygons. Depending on the granularity chosen, a user would tipically not need to look into a different polygon for CoffeeShops and even if that would be the case, a coouple of near polygons could also be pre-fetched when retrieving the initial one.

- one alternative for this v1 algorithm would be to pre-order all coordinates by latitude and then create a cached index for it so that, given a single set of coordinates, a radius can be calculated and the file would be read from a certain position in the buffer that is defined in that index to determine the list of CoffeeShops. This approach would imply extra database tuning in a real case scenario.

- another possibility would be to use Breadth First Search on a subset of CoffeeShops (i.e. pre-filtering as in the current version). The resulted list must be sorted by distance to original coordinates. Also, the BFS algorithm is notorious for consuming a lot of memory and being particularly tedious when the targets are farther from the origin.

- use one of the many dedicated libraries (i.e. Distance Matrix Service from Google).