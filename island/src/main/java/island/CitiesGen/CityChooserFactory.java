package island.CitiesGen;

import Logging.ParentLogger;
import island.CityTypes.Capital;
import island.CityTypes.City;
import island.CityTypes.MediumCity;
import island.CityTypes.SmallCity;
import island.IOEncapsulation.Vertex;
import island.Utility.RandomGen;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CityChooserFactory {
    private static final ParentLogger logger = new ParentLogger();
    private final City capital;
    private final Set<City> cities;
    public CityChooserFactory (RandomGen bag, List<Vertex> vertexList, int numCities) {
        if (vertexList == null || vertexList.size() == 0 || numCities <= 0) {
            this.capital = null;
            this.cities = new HashSet<>();
            logger.error("Vertex list is null or empty, or 0 cities selected");
            return;
        }

        if (vertexList.size() < numCities) {
            logger.error("More cities selected than possible vertices, using all possible vertices");
            numCities = vertexList.size();
        }

        Set<City> cities = new HashSet<>();
        Set<Vertex> usedVertices = new HashSet<>();

        //Deciding the capital city first
        int index = bag.nextInt(0, vertexList.size());

        Vertex vertex = vertexList.get(index);

        City city = new Capital(vertex);

        this.capital = city;
        usedVertices.add(vertex);
        numCities--;

        int percentOfMediumCities = bag.nextInt(0, 100);
        int mediumCityCount = numCities * percentOfMediumCities / 100;

        int i = 0;
        while (i < mediumCityCount) {
            index = bag.nextInt(0, vertexList.size());

            vertex = vertexList.get(index);

            //Technical debt, not every city should be the capital
            if (! usedVertices.contains(vertex)) {
                city = new MediumCity(vertex);

                cities.add(city);
                usedVertices.add(vertex);
                i++;
            }
        }

        i = 0;
        while (i < numCities - mediumCityCount) {
            index = bag.nextInt(0, vertexList.size());

            vertex = vertexList.get(index);

            //Technical debt, not every city should be the capital
            if (! usedVertices.contains(vertex)) {
                city = new SmallCity(vertex);

                cities.add(city);
                usedVertices.add(vertex);
                i++;
            }
        }

        this.cities = cities;
    }

    public City getCapital() {
        return capital;
    }

    public Set<City> getCities() {
        return cities;
    }
}
