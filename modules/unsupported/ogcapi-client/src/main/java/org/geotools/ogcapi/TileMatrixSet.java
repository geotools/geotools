package org.geotools.ogcapi;

import java.util.ArrayList;

public class TileMatrixSet extends org.geotools.ows.wmts.model.TileMatrixSet {
    private String title;

    private String supportedCRS;
    private ArrayList<Link> links = new ArrayList<>();

    public TileMatrixSet() {
        // TODO Auto-generated constructor stub
    }

    public ArrayList<Link> getLinks() {

        return links;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSupportedCRS(String supportedCRS) {
        this.supportedCRS = supportedCRS;
        this.setCoordinateReferenceSystem(OGCUtilities.parseCRS(supportedCRS));
    }

    @Override
    public String toString() {

        String name;
        if (getCoordinateReferenceSystem() != null)
            name = getCoordinateReferenceSystem().getName().toString();
        else name = supportedCRS;

        return getIdentifier() + ", " + name + ", " + title;
    }
}
