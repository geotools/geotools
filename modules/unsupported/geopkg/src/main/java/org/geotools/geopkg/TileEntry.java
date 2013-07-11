package org.geotools.geopkg;

import java.util.ArrayList;
import java.util.List;

public class TileEntry extends Entry {

    List<TileMatrix> tileMatricies = new ArrayList();
    Boolean timesTwoZoom;

    public TileEntry() {
        setDataType(DataType.Tile);
    }

    public List<TileMatrix> getTileMatricies() {
        return tileMatricies;
    }

    void setTileMatricies(List<TileMatrix> tileMatricies) {
        this.tileMatricies = tileMatricies;
    }

    public Boolean isTimesTwoZoom() {
        return timesTwoZoom;
    }

    public void setTimesTwoZoom(Boolean timesTwoZoom) {
        this.timesTwoZoom = timesTwoZoom;
    }

    void init(TileEntry e) {
        super.init(e);
        setTileMatricies(e.getTileMatricies());
        setTimesTwoZoom(e.isTimesTwoZoom());
    }

}
