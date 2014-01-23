package org.geotools.geopkg;

import java.util.ArrayList;
import java.util.List;

public class TileEntry extends Entry {

    List<TileMatrix> tileMatricies = new ArrayList();

    public TileEntry() {
        setDataType(DataType.Tile);
    }

    public List<TileMatrix> getTileMatricies() {
        return tileMatricies;
    }

    void setTileMatricies(List<TileMatrix> tileMatricies) {
        this.tileMatricies = tileMatricies;
    }

    void init(TileEntry e) {
        super.init(e);
        setTileMatricies(e.getTileMatricies());
    }

}
