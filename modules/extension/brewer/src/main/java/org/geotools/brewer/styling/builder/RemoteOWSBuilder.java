/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.brewer.styling.builder;

import org.geotools.styling.RemoteOWS;

public class RemoteOWSBuilder extends AbstractSLDBuilder<RemoteOWS> {
    private String service;

    private String onlineResource;

    public RemoteOWSBuilder() {
        this(null);
    }

    public RemoteOWSBuilder(AbstractSLDBuilder<?> parent) {
        super(parent);
        reset();
    }

    public RemoteOWSBuilder resource(String onlineResource) {
        this.onlineResource = onlineResource;
        this.unset = false;
        return this;
    }

    public RemoteOWSBuilder service(String service) {
        this.service = service;
        this.unset = false;
        return this;
    }

    public RemoteOWS build() {
        if (unset) {
            return null;
        }
        RemoteOWS remote = sf.createRemoteOWS(service, onlineResource);
        return remote;
    }

    public RemoteOWSBuilder reset() {
        unset = true;
        this.onlineResource = null;
        this.service = null;
        return this;
    }

    public RemoteOWSBuilder reset(RemoteOWS remote) {
        if (remote == null) {
            return unset();
        }
        this.onlineResource = remote.getOnlineResource();
        this.service = remote.getService();
        unset = false;
        return this;
    }

    public RemoteOWSBuilder unset() {
        return (RemoteOWSBuilder) super.unset();
    }

    @Override
    protected void buildSLDInternal(StyledLayerDescriptorBuilder sb) {
        throw new UnsupportedOperationException(
                "Cannot build a SLD out of a simple remote ows spec");
    }
}
