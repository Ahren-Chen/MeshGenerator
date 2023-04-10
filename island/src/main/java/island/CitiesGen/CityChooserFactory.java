package island.CitiesGen;

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
    private final City capital;
    private final Set<City> cities;
    public CityChooserFactory (RandomGen bag, List<Vertex> vertexList, int numCities) {
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

        for (int i = 0; i < mediumCityCount; i++) {
            index = bag.nextInt(0, vertexList.size());

            vertex = vertexList.get(index);

            //Technical debt, not every city should be the capital
            if (! usedVertices.contains(vertex)) {
                city = new MediumCity(vertex);

                cities.add(city);
                usedVertices.add(vertex);
            }
        }

        for (int i = 0; i < numCities - mediumCityCount; i++) {
            index = bag.nextInt(0, vertexList.size());

            vertex = vertexList.get(index);

            //Technical debt, not every city should be the capital
            if (! usedVertices.contains(vertex)) {
                city = new SmallCity(vertex);

                cities.add(city);
                usedVertices.add(vertex);
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
