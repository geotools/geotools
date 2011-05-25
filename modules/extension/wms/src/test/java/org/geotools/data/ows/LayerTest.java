package org.geotools.data.ows;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;
/**
 * Simple API designed to keep the API stable.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/wms/src/test/java/org/geotools/data/ows/LayerTest.java $
 */
public class LayerTest {
    public static final HashMap<String, CRSEnvelope> BBOXES;
    static{
        BBOXES = new HashMap<String, CRSEnvelope>();
        CRSEnvelope generalEnvelope = new CRSEnvelope("EPSG:4326",-180,-90,180,90); //$NON-NLS-1$
        BBOXES.put("EPSG:4326", generalEnvelope); //$NON-NLS-1$
    }
    @Test
    public void testLayer(){
        Layer world = new Layer("world"); //$NON-NLS-1$
        world.setBoundingBoxes(BBOXES);
        world.setSrs(BBOXES.keySet());
    }
    
    public void cacheTest(){
        Layer parent = new Layer("parent");
        Layer child = new Layer("child");
        child.setParent(parent);
        assertTrue( parent.getLayerChildren().contains( child ) );
    }
}
