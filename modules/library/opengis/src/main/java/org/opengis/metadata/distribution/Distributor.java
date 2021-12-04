/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.distribution;

import static org.opengis.annotation.Obligation.CONDITIONAL;
import static org.opengis.annotation.Obligation.MANDATORY;
import static org.opengis.annotation.Obligation.OPTIONAL;
import static org.opengis.annotation.Specification.ISO_19115;

import java.util.Collection;
import org.opengis.annotation.UML;
import org.opengis.metadata.citation.ResponsibleParty;

/**
 * Information about the distributor.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier = "MD_Distributor", specification = ISO_19115)
public interface Distributor {
    /**
     * Party from whom the resource may be obtained. This list need not be exhaustive.
     *
     * @return Party from whom the resource may be obtained.
     */
    @UML(identifier = "distributorContact", obligation = MANDATORY, specification = ISO_19115)
    ResponsibleParty getDistributorContact();

    /**
     * Provides information about how the resource may be obtained, and related instructions and fee
     * information.
     *
     * @return Information about how the resource may be obtained.
     */
    @UML(identifier = "distributionOrderProcess", obligation = OPTIONAL, specification = ISO_19115)
    Collection<? extends StandardOrderProcess> getDistributionOrderProcesses();

    /**
     * Provides information about the format used by the distributor.
     *
     * @return Information about the format used by the distributor.
     */
    @UML(identifier = "distributorFormat", obligation = CONDITIONAL, specification = ISO_19115)
    Collection<? extends Format> getDistributorFormats();

    /**
     * Provides information about the technical means and media used by the distributor.
     *
     * @return Information about the technical means and media used by the distributor.
     */
    @UML(
            identifier = "distributorTransferOptions",
            obligation = OPTIONAL,
            specification = ISO_19115)
    Collection<? extends DigitalTransferOptions> getDistributorTransferOptions();
}
