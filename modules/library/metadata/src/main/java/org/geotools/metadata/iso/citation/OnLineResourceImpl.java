/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.citation;

import java.net.URI;
import java.net.URISyntaxException;
import net.opengis.ows11.OnlineResourceType;
import org.geotools.api.metadata.citation.OnLineFunction;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;

/**
 * Information about on-line sources from which the dataset, specification, or community profile name and extended
 * metadata elements can be obtained.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 */
public class OnLineResourceImpl extends MetadataEntity implements OnLineResource {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 5412370008274334799L;

    /**
     * The online resources for the <A HREF="http://www.opengeospatial.org">Open Geospatial Consortium</A>. "Open
     * Geospatial consortium" is the new name for "OpenGIS consortium".
     *
     * @see #OPEN_GIS
     */
    public static final OnLineResource OGC;

    static {
        final OnLineResourceImpl r;
        OGC = r = new OnLineResourceImpl("http://www.opengeospatial.org/");
        r.freeze();
    }

    /**
     * The online resources for the <A HREF="http://www.opengis.org">OpenGIS consortium</A>. "OpenGIS consortium" is the
     * old name for "Open Geospatial consortium".
     *
     * @see #OGC
     */
    public static final OnLineResource OPEN_GIS;

    static {
        final OnLineResourceImpl r;
        OPEN_GIS = r = new OnLineResourceImpl("http://www.opengis.org");
        r.freeze();
    }

    /** The online resources for the <A HREF="http://www.epsg.org">European Petroleum Survey Group</A>. */
    public static final OnLineResource EPSG;

    static {
        final OnLineResourceImpl r;
        EPSG = r = new OnLineResourceImpl("http://www.epsg.org");
        r.freeze();
    }

    /** The online resources for the <A HREF="http://www.remotesensing.org/geotiff/geotiff.html">GeoTIFF</A> group. */
    public static final OnLineResource GEOTIFF;

    static {
        final OnLineResourceImpl r;
        GEOTIFF = r = new OnLineResourceImpl("http://www.remotesensing.org/geotiff");
        r.freeze();
    }

    /** The online resources for <A HREF="http://www.esri.com">ESRI</A>. */
    public static final OnLineResource ESRI;

    static {
        final OnLineResourceImpl r;
        ESRI = r = new OnLineResourceImpl("http://www.esri.com");
        r.freeze();
    }

    /** The online resources for <A HREF="https://www.iau.org">IAU</A>. */
    public static final OnLineResource IAU;

    static {
        final OnLineResourceImpl r;
        IAU = r = new OnLineResourceImpl("https://www.iau.org");
        r.freeze();
    }

    /** The online resources for <A HREF="http://www.oracle.com">Oracle</A>. */
    public static final OnLineResource ORACLE;

    static {
        final OnLineResourceImpl r;
        ORACLE = r = new OnLineResourceImpl("http://www.oracle.com");
        r.freeze();
    }

    /**
     * The online resources for <A HREF="http://postgis.refractions.net">PostGIS</A>.
     *
     * @since 2.4
     */
    public static final OnLineResource POSTGIS;

    static {
        final OnLineResourceImpl r;
        POSTGIS = r = new OnLineResourceImpl("http://postgis.refractions.net");
        r.freeze();
    }

    /** The online resources for <A HREF="https://proj.org">PROJ</A>. */
    public static final OnLineResource PROJ;

    static {
        final OnLineResourceImpl r;
        PROJ = r = new OnLineResourceImpl("https://proj.org");
        r.freeze();
    }

    /**
     * The online resources for <A HREF="http://java.sun.com/">Sun Microsystems</A>. This online resources point to the
     * Java developper site.
     *
     * @since 2.2
     */
    public static final OnLineResource SUN_MICROSYSTEMS;

    static {
        final OnLineResourceImpl r;
        SUN_MICROSYSTEMS = r = new OnLineResourceImpl("http://java.sun.com");
        r.freeze();
    }

    /** The online resources for the <A HREF="http://www.geotools.org">Geotools</A> project. */
    public static final OnLineResource GEOTOOLS;

    static {
        final OnLineResourceImpl r;
        GEOTOOLS = r = new OnLineResourceImpl("http://www.geotools.org");
        r.freeze();
    }

    /**
     * The download link for <A HREF="http://portal.opengis.org/files/?artifact_id=5316">Web Map Service</A>
     * specification. The download link may change in future Geotools versions in order to point toward the latest
     * specification.
     *
     * @since 2.2
     */
    public static final OnLineResource WMS;

    static {
        final OnLineResourceImpl r;
        WMS = r = new OnLineResourceImpl("http://portal.opengis.org/files/?artifact_id=5316");
        r.setFunction(OnLineFunction.DOWNLOAD);
        r.freeze();
    }

    /** Name of an application profile that can be used with the online resource. */
    private String applicationProfile;

    /** Detailed text description of what the online resource is/does. */
    private InternationalString description;

    /** Code for function performed by the online resource. */
    private OnLineFunction function;

    /**
     * Location (address) for on-line access using a Uniform Resource Locator address or similar addressing scheme such
     * as http://www.statkart.no/isotc211.
     */
    private URI linkage;

    /** Name of the online resources. */
    private String name;

    /** Creates an initially empty on line resource. */
    public OnLineResourceImpl() {}

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public OnLineResourceImpl(final OnLineResource source) {
        super(source);
    }

    /**
     * Creates an on line resource initialized to the given URI. This method is private for now since, if this
     * constructor was public, some users may expect a string argument to be for the description text instead.
     * Furthermore, a public method should not catch the {@link URISyntaxException} and should not set a function.
     */
    private OnLineResourceImpl(final String linkage) {
        try {
            setLinkage(new URI(linkage));
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException(exception);
        }
        setFunction(OnLineFunction.INFORMATION);
    }

    /** Creates an on line resource initialized to the given URI. */
    public OnLineResourceImpl(final URI linkage) {
        setLinkage(linkage);
    }

    public OnLineResourceImpl(OnlineResourceType onlineResource) {

        this(onlineResource.getHref());
    }

    /**
     * Returns the name of an application profile that can be used with the online resource. Returns {@code null} if
     * none.
     */
    @Override
    public String getApplicationProfile() {
        return applicationProfile;
    }

    /** Set the name of an application profile that can be used with the online resource. */
    public void setApplicationProfile(final String newValue) {
        checkWritePermission();
        applicationProfile = newValue;
    }

    /**
     * Name of the online resource. Returns {@code null} if none.
     *
     * @since 2.4
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the name of the online resource.
     *
     * @since 2.4
     */
    public void setName(final String newValue) {
        checkWritePermission();
        name = newValue;
    }

    /** Returns the detailed text description of what the online resource is/does. Returns {@code null} if none. */
    @Override
    public InternationalString getDescription() {
        return description;
    }

    /** Set the detailed text description of what the online resource is/does. */
    public void setDescription(final InternationalString newValue) {
        checkWritePermission();
        description = newValue;
    }

    /** Returns the code for function performed by the online resource. Returns {@code null} if unspecified. */
    @Override
    public OnLineFunction getFunction() {
        return function;
    }

    /** Set the code for function performed by the online resource. */
    public void setFunction(final OnLineFunction newValue) {
        checkWritePermission();
        function = newValue;
    }

    /**
     * Returns the location (address) for on-line access using a Uniform Resource Locator address or similar addressing
     * scheme such as http://www.statkart.no/isotc211.
     */
    @Override
    public URI getLinkage() {
        return linkage;
    }

    /**
     * Set the location (address) for on-line access using a Uniform Resource Locator address or similar addressing
     * scheme such as http://www.statkart.no/isotc211.
     */
    public void setLinkage(final URI newValue) {
        checkWritePermission();
        linkage = newValue;
    }

    /** Returns the connection protocol to be used. Returns {@code null} if none. */
    @Override
    public String getProtocol() {
        final URI linkage = this.linkage;
        return linkage != null ? linkage.getScheme() : null;
    }
}
