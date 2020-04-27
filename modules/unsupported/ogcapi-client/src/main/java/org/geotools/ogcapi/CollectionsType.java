package org.geotools.ogcapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class CollectionsType {
    Map<String, CollectionType> collections = new HashMap<>();
    ArrayList<CoordinateReferenceSystem> crs = new ArrayList<>();

    @Override
    public String toString() {
        return "collections=" + collections;
    }
}
