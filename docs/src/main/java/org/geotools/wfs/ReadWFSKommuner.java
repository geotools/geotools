/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.wfs;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import org.geotools.data.FeatureSource;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.impl.WFSContentDataAccess;
import org.geotools.data.wfs.impl.WFSDataAccessFactory;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * Read the contents of a WFS with complex structure.
 *
 * @author Roar Brænden
 */
public class ReadWFSKommuner {

    /*
    	 *   Content of a single feature with only one AdministrativEnhetNavn
    	 *
    <wfs:member>
        <app:Kommune xmlns:app="http://skjema.geonorge.no/SOSI/produktspesifikasjon/AdmEnheter/4.1" gml:id="kommune_view.357">
          <app:identifikasjon>
            <app:Identifikasjon>
              <app:lokalId>173103</app:lokalId>
              <app:navnerom>https://data.geonorge.no/sosi/administrativeenheter/fylker_kommuner</app:navnerom>
              <app:versjonId>4.1</app:versjonId>
            </app:Identifikasjon>
          </app:identifikasjon>
          <app:oppdateringsdato>2020-02-07T00:00:00</app:oppdateringsdato>
          <app:datauttaksdato>2021-01-04T10:07:10</app:datauttaksdato>
          <app:område>
            <!--Inlined geometry 'kommune_view.357_APP_OMRÅDE'-->
            <gml:Polygon gml:id="kommune_view.357_APP_OMRÅDE" srsName="urn:ogc:def:crs:EPSG::4258">
              <gml:exterior>
                <gml:LinearRing>
                  <gml:posList>58.426581 6.605405 58.420867 6.599559 .......
                </gml:LinearRing>
              </gml:exterior>
            </gml:Polygon>
          </app:område>
          <app:kommunenummer>4207</app:kommunenummer>
          <app:kommunenavn>
            <app:AdministrativEnhetNavn>
              <app:navn>Flekkefjord</app:navn>
              <app:språk>nor</app:språk>
            </app:AdministrativEnhetNavn>
          </app:kommunenavn>
          <app:samiskForvaltningsområde>false</app:samiskForvaltningsområde>
        </app:Kommune>
      </wfs:member>
    	 *
    	 */

    private static final String SCHEMA_CACHE = "../../schemas";

    private static final String CAPABILITIES_URL =
            "https://wfs.geonorge.no/skwms1/wfs.administrative_enheter?&service=WFS&acceptversions=2.0.0&request=GetCapabilities";

    /** Calls the WFS server and prints out the content. */
    public void read() throws IOException {

        final HashMap<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(WFSDataStoreFactory.URL.key, new URL(CAPABILITIES_URL));
        params.put(WFSDataStoreFactory.TIMEOUT.key, 60000); // 60 seconds
        params.put(WFSDataStoreFactory.BUFFER_SIZE.key, 100);
        params.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.FALSE); // Prefer GET to support Schema
        params.put("WFSDataStoreFactory:SCHEMA_CACHE_LOCATION", SCHEMA_CACHE);

        WFSContentDataAccess complexDataAccess =
                (WFSContentDataAccess) new WFSDataAccessFactory().createDataStore(params);
        Name featureName = null;
        for (Name name : complexDataAccess.getNames()) {
            if ("Kommune".equals(name.getLocalPart())) {
                featureName = name;
                break;
            }
        }
        if (featureName == null) {
            throw new RuntimeException("WFS didn't return a Kommune feature.");
        }

        FeatureSource<FeatureType, Feature> kommuneSource =
                complexDataAccess.getFeatureSource(featureName);

        try (FeatureIterator<Feature> features = kommuneSource.getFeatures().features()) {

            while (features.hasNext()) {
                Feature nextFeature = features.next();
                String kommunenr = (String) nextFeature.getProperty("kommunenummer").getValue();
                String kommunenavn = "Uten norsk navn";
                ComplexAttribute kommunenavnAttribute =
                        (ComplexAttribute) nextFeature.getProperty("kommunenavn");
                for (Property administrativEnhetNavn : kommunenavnAttribute.getProperties()) {
                    ComplexAttribute innerElement = innerComplex(administrativEnhetNavn);
                    if ("nor".equals(innerElement.getProperty("språk").getValue())) {
                        kommunenavn = (String) innerElement.getProperty("navn").getValue();
                        break;
                    }
                }

                System.out.printf("%s : %s\n-------------\n", kommunenr, kommunenavn);
                for (Property prop : nextFeature.getProperties()) {
                    System.out.println(prop.toString());
                }
                System.out.println("-------------");
            }
        }
    }

    private static ComplexAttribute innerComplex(Property complex) {
        return (ComplexAttribute) ((ComplexAttribute) complex).getProperties().iterator().next();
    }

    /** Call read */
    public static void main(String[] args) {
        try {
            new ReadWFSKommuner().read();
        } catch (Exception e) {
            System.out.println("ReadWFSKommuner ended with an exception: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
}
