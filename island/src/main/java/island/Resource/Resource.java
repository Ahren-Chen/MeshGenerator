package island.Resource;

import Logging.ParentLogger;
import island.IOEncapsulation.Polygon;
import island.IOEncapsulation.Vertex;
import island.Tiles.LakeTile;
import island.Tiles.OceanTile;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Resource {
    private final double normalThickness = 5;
    private final Color seafoodResources = Color.CYAN;

    private final Color freshwaterFish = Color.BLUE;

    private final Color cropResources = Color.ORANGE;

    private final Color mineralsResource = Color.PINK;

    private final Color fruitResource = Color.RED;

    private final Color oil_gasResource = Color.BLACK;

    private static final ParentLogger logger = new ParentLogger();


    public Resource() {
        //System.out.println();
    }

    public Map <Integer, Polygon> resourceCalculation(Map<Integer, Polygon> polygonMap){

        for (Polygon p: polygonMap.values()){
            if(p.getIsWater()){
                seafood(p);
                freshwater(p);
            } else if (p.getElevation()>1500&&!p.getIsWater()) {
                minerals(p);
            } else if (p.getPrecipitation()<30&&!p.getIsWater()) {
                oil_gas(p);
            } else if (p.getTemperature()>15&&!p.getIsWater()) {
                fruitResource(p);
            } else if (p.getElevation()<500&&!p.getIsWater()) {
                crop(p);
            }
            }
        return polygonMap;
    }

    private void seafood(Polygon p ){                           // it's only depend on if the polygon is an ocean tile or not
        if(p.getClass().equals(OceanTile.class)){
            p.getCentroid().setColor(this.seafoodResources);
            p.getCentroid().setThickness(normalThickness);
            logger.trace("seafood is this tile's resource production");
        }
    }
    private void freshwater(Polygon p ){                        // it's only depend on if the polygon is a lake tile or not
        if(p.getClass().equals(LakeTile.class)){
            p.getCentroid().setColor(this.freshwaterFish);
            p.getCentroid().setThickness(normalThickness);
            logger.trace("freshwater is this tile's resource production");
        }

    }private void crop(Polygon p ){                             // it only will have crop resource if the elevation is low and precipitation will determine the size of crop yields
        double precipitation = p.getPrecipitation();
        double thickness  = normalThickness + precipitation/100;
        p.getCentroid().setColor(this.cropResources);
        p.getCentroid().setThickness(thickness);
        logger.trace("crop is this tile's resource production");

    }private void minerals(Polygon p ){                         // it only will have minerals when It's a high elevation tile, and the height of the mountain determines whether the mineral resources are rich or not
        double elevation = p.getElevation();
        double thickness = normalThickness + elevation/1000;
        p.getCentroid().setColor(this.mineralsResource);
        p.getCentroid().setThickness(thickness);
        logger.trace("minerals is this tile's resource production");

    }private void fruitResource(Polygon p ){                    //it only will have fruit when the elevation is low and has aquifer under the ground
            p.getCentroid().setColor(this.fruitResource);
        p.getCentroid().setThickness(normalThickness);
        logger.trace("fruit is this tile's resource production");
    }
    private void oil_gas(Polygon p){                            //it only will have oil_gas when the precipitation is really low
        double precipitation = p.getPrecipitation();
        double thickness = normalThickness + (20-precipitation)/100;
        p.getCentroid().setColor(this.oil_gasResource);
        p.getCentroid().setThickness(thickness);
        logger.trace("oil_gas is this tile's resource production");
    }
}
