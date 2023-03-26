package island.resource;

import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Vertex;

import java.awt.*;
import java.util.List;

public class Resource {
    private final Color seafoodResources = Color.CYAN;

    private final Color freshwaterFish = Color.BLUE;

    private final Color cropResources = Color.ORANGE;

    private final Color mineralsResource = Color.PINK;

    private final Color fruitResource = Color.RED;

    private final Color oil_gasResource = Color.BLACK;
    public void Resource(List<Polygon> polygons){


    }


    private void resourceCalculation(List<Polygon> polygons){
        Vertex representation;
        for (Polygon p :polygons) {
            if(p.getIsWater()){
                representation = p.getCentroid();
            }
        }

    }

    private void seafood(Polygon p ){                           // it's only depend on if the polygon is an ocean tile or not
        if(p.getColor().equals(Color.BLUE)){
            p.getCentroid().setColor(this.seafoodResources);
        }
    }
    private void freshwater(Polygon p ){                        // it's only depend on if the polygon is a lake tile or not
        if(p.getColor().equals(Color.CYAN)){
            p.getCentroid().setColor(this.freshwaterFish);
        }

    }private void crop(Polygon p ){                             // it only will have crop resource if the elevation is low and precipitation will determine the size of crop yields
        double precipitation = p.getPrecipitation();
        double thickness  = precipitation/100;
    }private void minerals(Polygon p ){                         // it only will have minerals when It's a high elevation tile, and the height of the mountain determines whether the mineral resources are rich or not

    }private void fruitResource(Polygon p ){                    //it only will have fruit when the elevation is low and Humidity determines the yield of fruit

    }
    private void oil_gas(Polygon p){                            //it only will have oil_gas when the humidity is really low

    }
}
