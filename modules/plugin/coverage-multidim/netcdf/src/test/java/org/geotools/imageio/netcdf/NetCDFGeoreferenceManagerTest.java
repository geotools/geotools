package org.geotools.imageio.netcdf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.MalformedURLException;
import org.geotools.test.TestData;
import org.junit.Test;
import ucar.nc2.dataset.NetcdfDataset;

public class NetCDFGeoreferenceManagerTest {

    @Test
    public void testCoordinateAxisExclusion() throws MalformedURLException, IOException {
        final NetcdfDataset dataset =
                NetcdfDataset.openDataset(TestData.url(this, "axistime.nc").toExternalForm());

        NetCDFGeoreferenceManager manager = new NetCDFGeoreferenceManager(dataset);
        assertNull(manager.getCoordinateVariable("sst_time"));
        assertNotNull(manager.getCoordinateVariable("time"));
    }
}
