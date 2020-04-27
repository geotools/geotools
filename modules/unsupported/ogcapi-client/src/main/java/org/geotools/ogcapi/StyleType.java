package org.geotools.ogcapi;

import java.io.IOException;
import java.net.URL;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.styling.SLDParser;

public class StyleType {
    String identifier;
    String title;
    Style sld;
    static StyleFactory factory = CommonFactoryFinder.getStyleFactory();

    private Style loadStyleFromSLD(URL url) throws IOException {

        SLDParser stylereader = new SLDParser(factory, url);
        Style[] style = stylereader.readXML();

        return style[0];
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Style getSld() {
        return sld;
    }

    public void setSld(Style sld) {
        this.sld = sld;
    }

    public void setSld(URL url) throws IOException {
        this.sld = loadStyleFromSLD(url);
    }

    @Override
    public String toString() {
        return "StyleType [identifier=" + identifier + ", title=" + title + "]";
    }
}
