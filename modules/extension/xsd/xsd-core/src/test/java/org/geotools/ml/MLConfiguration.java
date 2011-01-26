/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ml;

import org.picocontainer.MutablePicoContainer;
import org.geotools.ml.bindings.ML;
import org.geotools.ml.bindings.MLAttachmentTypeBinding;
import org.geotools.ml.bindings.MLBodyTypeBinding;
import org.geotools.ml.bindings.MLEnvelopeTypeBinding;
import org.geotools.ml.bindings.MLMailTypeBinding;
import org.geotools.ml.bindings.MLMailsTypeBinding;
import org.geotools.ml.bindings.MLMimeTopLevelTypeBinding;
import org.geotools.xml.Configuration;


public class MLConfiguration extends Configuration {
    public MLConfiguration() {
        super(ML.getInstance());
    }

    protected final void registerBindings(MutablePicoContainer container) {
        container.registerComponentImplementation(ML.ATTACHMENTTYPE, MLAttachmentTypeBinding.class);
        container.registerComponentImplementation(ML.BODYTYPE, MLBodyTypeBinding.class);
        container.registerComponentImplementation(ML.ENVELOPETYPE, MLEnvelopeTypeBinding.class);
        container.registerComponentImplementation(ML.MAILSTYPE, MLMailsTypeBinding.class);
        container.registerComponentImplementation(ML.MAILTYPE, MLMailTypeBinding.class);
        container.registerComponentImplementation(ML.MIMETOPLEVELTYPE,
            MLMimeTopLevelTypeBinding.class);
    }
}
