package org.geotools.javafx;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.wms.WebMapServer;
import org.geotools.geometry.GeneralEnvelope;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class FXMapTest extends ApplicationTest {

    private static FXMap map;

    private static final int sceneHeight = 750;
    private static final int sceneWidth = 750;

    private static GeneralEnvelope initialBounds;
    private static final Logger log = Logger.getLogger(FXMapTest.class.getName());
    private static final String mapEPSG = "EPSG:4326";
    private static final String wmsLayer = "OpenStreetMap WMS - by terrestris";
    private static final String wmsURL = "http://ows.terrestris.de/osm/service";

    @BeforeClass
    public static void initTests() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Pane(), sceneWidth, sceneHeight);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void MapCreationTest() {
        WebMapServer wms = null;
        try {
            wms = new WebMapServer(new URL(wmsURL));
            initialBounds = new GeneralEnvelope(new double[] {-22, 31}, new double[] {50, 67});
        } catch (Exception e) {
            log.log(Level.SEVERE, e.toString());
        }
        Layer displayLayer = wms.getCapabilities().getLayer();
        for (Layer layer : wms.getCapabilities().getLayerList()) {
            if (layer.getTitle().toLowerCase().equals(wmsLayer.toLowerCase())) {
                displayLayer = layer;
                break;
            }
        }
        try {
            displayLayer.setBoundingBoxes(new CRSEnvelope(initialBounds));
            map = new FXMap(wms, displayLayer, sceneHeight, sceneWidth, initialBounds);
        } catch (Exception e) {
            fail("Map creation failed: " + e.getMessage());
        }
        assertTrue(map.isInitialized());
    }
}
