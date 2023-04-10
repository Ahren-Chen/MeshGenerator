package island.CitiesGen;

import island.CityTypes.Capital;
import island.CityTypes.City;
import island.IOEncapsulation.Vertex;
import island.Utility.RandomGen;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CityChooserFactory {
    public static Set<City> randomCities (RandomGen bag, List<Vertex> vertexList, int numCities) {
        Set<City> cities = new HashSet<>();
        Set<Vertex> usedVertices = new HashSet<>();

        for (int i = 0; i < numCities; i++) {
            int index = bag.nextInt(0, vertexList.size());

            Vertex vertex = vertexList.get(index);

            //Technical debt, not every city should be the capital
            if (! usedVertices.contains(vertex)) {
                City city = new Capital(vertex);

                cities.add(city);
                usedVertices.add(vertex);
            }
        }

        return cities;
    }
}
