package org.geotools.map;


import static org.easymock.EasyMock.*;

import java.io.IOException;

import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;

public class DefaultMapContextTest {

	MapLayer mapLayerBoundsNull = createNiceMock(MapLayer.class);

	/**
	 * if a {@link MapLayer#getBounds()} returns null a NullPointerException will be thrown 
	 */
	@Test
	public void testNPELayerBounds() {
		expect(mapLayerBoundsNull.getBounds()).andReturn(null);
		
		replay(mapLayerBoundsNull);
		
		DefaultMapContext mapContext = new DefaultMapContext(DefaultGeographicCRS.WGS84);
		try {
			mapContext.addLayer(mapLayerBoundsNull);
			// throws a NullPointerException
			mapContext.getLayerBounds();
		} catch (IOException e) {
			// don't care about is right here
		}
	}
}
