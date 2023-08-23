/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.distribution;

import java.util.Collection;

/**
 * Information about the distributor of and options for obtaining the resource.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface Distribution {
    /**
     * Provides a description of the format of the data to be distributed.
     *
     * @return Description of the format of the data to be distributed.
     */
    Collection<? extends Format> getDistributionFormats();

    /**
     * Provides information about the distributor.
     *
     * @return Information about the distributor.
     */
    Collection<? extends Distributor> getDistributors();

    /**
     * Provides information about technical means and media by which a resource is obtained from the
     * distributor.
     *
     * @return Technical means and media by which a resource is obtained from the distributor.
     */
    Collection<? extends DigitalTransferOptions> getTransferOptions();
}
