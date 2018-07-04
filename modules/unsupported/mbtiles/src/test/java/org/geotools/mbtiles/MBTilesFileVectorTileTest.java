package org.geotools.mbtiles;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.geotools.mbtiles.MBTilesMetadata.t_format;
import org.geotools.mbtiles.MBTilesMetadata.t_type;
import org.geotools.referencing.CRS;
import org.geotools.util.URLs;
import org.junit.Before;
import org.junit.Test;

public class MBTilesFileVectorTileTest {

    File dbfile;

    @Before
    public void setUp() {
        dbfile = URLs.urlToFile(MBTilesFileVectorTileTest.class.getResource("planet.mbtiles"));
    }

    @Test
    public void testMBTilesMetaData() throws Exception {
        try (MBTilesFile file = new MBTilesFile(dbfile); ) {

            MBTilesMetadata metadata2 = file.loadMetaData();
            assertEquals("osm2vectortiles", metadata2.getName());
            assertEquals("Extract from http://osm2vectortiles.org", metadata2.getDescription());
            assertEquals("2.0", metadata2.getVersion());
            metadata2.getBounds().getLowerCorner().getCoordinate();
            double delta = 0.0001;
            assertEquals(-180.0, metadata2.getBounds().getLowerCorner().getOrdinate(0), delta);
            assertEquals(180.0, metadata2.getBounds().getUpperCorner().getOrdinate(0), delta);
            assertEquals(-85.0511, metadata2.getBounds().getLowerCorner().getOrdinate(1), delta);
            assertEquals(85.0511, metadata2.getBounds().getUpperCorner().getOrdinate(1), delta);
            assertEquals(
                    CRS.decode("EPSG:4326", true),
                    metadata2.getBounds().getCoordinateReferenceSystem());
            assertEquals(t_format.PBF, metadata2.getFormat());
            assertEquals(t_type.BASE_LAYER, metadata2.getType());
            assertEquals(0, metadata2.getMinZoom());
            assertEquals(5, metadata2.getMaxZoom());
        }
    }

    @Test
    public void testMBTilesGetTile() throws Exception {
        byte[] expected;
        try (InputStream is =
                MBTilesFileVectorTileTest.class.getResourceAsStream("tile_data.pbf.gz")) {
            expected = IOUtils.toByteArray(is);
        }
        try (MBTilesFile file = new MBTilesFile(dbfile); ) {

            MBTilesTile tile = file.loadTile(0, 0, 0);
            assertThat(tile.getData(), notNullValue());
            assertThat(tile.getData(), equalTo(expected));
        }
    }
}
