/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import org.geotools.api.metadata.citation.ResponsibleParty;
import org.geotools.api.metadata.constraint.Constraints;
import org.geotools.api.metadata.content.ContentInformation;
import org.geotools.api.metadata.distribution.Distribution;
import org.geotools.api.metadata.identification.CharacterSet;
import org.geotools.api.metadata.identification.Identification;
import org.geotools.api.metadata.maintenance.MaintenanceInformation;
import org.geotools.api.metadata.maintenance.ScopeCode;
import org.geotools.api.metadata.quality.DataQuality;
import org.geotools.api.metadata.spatial.SpatialRepresentation;
import org.geotools.api.referencing.ReferenceSystem;

/**
 * Root entity which defines metadata about a resource or resources.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 2.0
 */
public interface MetaData {
    /**
     * Unique identifier for this metadata file, or {@code null} if none.
     *
     * @return Unique identifier for this metadata file, or {@code null}.
     */
    String getFileIdentifier();

    /**
     * Language used for documenting metadata.
     *
     * @return Language used for documenting metadata, or {@code null}.
     */
    Locale getLanguage();

    /**
     * Full name of the character coding standard used for the metadata set.
     *
     * @return character coding standard used for the metadata, or {@code null}.
     */
    CharacterSet getCharacterSet();

    /**
     * File identifier of the metadata to which this metadata is a subset (child).
     *
     * @return Identifier of the metadata to which this metadata is a subset, or {@code null}.
     */
    String getParentIdentifier();

    /**
     * Scope to which the metadata applies.
     *
     * @return Scope to which the metadata applies.
     */
    Collection<ScopeCode> getHierarchyLevels();

    /**
     * Name of the hierarchy levels for which the metadata is provided.
     *
     * @return Hierarchy levels for which the metadata is provided.
     */
    Collection<String> getHierarchyLevelNames();

    /**
     * Parties responsible for the metadata information.
     *
     * @return Parties responsible for the metadata information.
     * @since GeoAPI 2.1
     */
    Collection<? extends ResponsibleParty> getContacts();

    /**
     * Date that the metadata was created.
     *
     * @return Date that the metadata was created.
     */
    Date getDateStamp();

    /**
     * Name of the metadata standard (including profile name) used.
     *
     * @return Name of the metadata standard used, or {@code null}.
     */
    String getMetadataStandardName();

    /**
     * Version (profile) of the metadata standard used.
     *
     * @return Version of the metadata standard used, or {@code null}.
     */
    String getMetadataStandardVersion();

    /**
     * Uniformed Resource Identifier (URI) of the dataset to which the metadata applies.
     *
     * @return Uniformed Resource Identifier of the dataset, or {@code null}.
     * @since GeoAPI 2.1
     */
    String getDataSetUri();

    /**
     * Provides information about an alternatively used localized character string for a linguistic
     * extension.
     *
     * @return Alternatively used localized character string for a linguistic extension.
     * @since GeoAPI 2.1
     */
    Collection<Locale> getLocales();

    /**
     * Digital representation of spatial information in the dataset.
     *
     * @return Digital representation of spatial information in the dataset.
     */
    Collection<? extends SpatialRepresentation> getSpatialRepresentationInfo();

    /**
     * Description of the spatial and temporal reference systems used in the dataset.
     *
     * @return Spatial and temporal reference systems used in the dataset.
     */
    Collection<? extends ReferenceSystem> getReferenceSystemInfo();

    /**
     * Information describing metadata extensions.
     *
     * @return Metadata extensions.
     */
    Collection<? extends MetadataExtensionInformation> getMetadataExtensionInfo();

    /**
     * Basic information about the resource(s) to which the metadata applies.
     *
     * @return The resource(s) to which the metadata applies.
     */
    Collection<? extends Identification> getIdentificationInfo();

    /**
     * Provides information about the feature catalogue and describes the coverage and image data
     * characteristics.
     *
     * @return The feature catalogue, coverage descriptions and image data characteristics.
     */
    Collection<? extends ContentInformation> getContentInfo();

    /**
     * Provides information about the distributor of and options for obtaining the resource(s).
     *
     * @return The distributor of and options for obtaining the resource(s).
     */
    Distribution getDistributionInfo();

    /**
     * Provides overall assessment of quality of a resource(s).
     *
     * @return Overall assessment of quality of a resource(s).
     */
    Collection<? extends DataQuality> getDataQualityInfo();

    /**
     * Provides information about the catalogue of rules defined for the portrayal of a resource(s).
     *
     * @return The catalogue of rules defined for the portrayal of a resource(s).
     */
    Collection<? extends PortrayalCatalogueReference> getPortrayalCatalogueInfo();

    /**
     * Provides restrictions on the access and use of data.
     *
     * @return Restrictions on the access and use of data.
     */
    Collection<? extends Constraints> getMetadataConstraints();

    /**
     * Provides information about the conceptual schema of a dataset.
     *
     * @return The conceptual schema of a dataset.
     */
    Collection<? extends ApplicationSchemaInformation> getApplicationSchemaInfo();

    /**
     * Provides information about the frequency of metadata updates, and the scope of those updates.
     *
     * @return The frequency of metadata updates and their scope.
     */
    MaintenanceInformation getMetadataMaintenance();
}
