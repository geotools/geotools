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

import java.math.BigInteger;


public class Mail {
    BigInteger id;
    Envelope envelope;
    String body;
    Attachment[] attachments;

    public Mail(BigInteger id, String body, Envelope envelope, Attachment[] attachments) {
        super();

        this.id = id;
        this.body = body;
        this.envelope = envelope;
        this.attachments = attachments;
    }

    public BigInteger getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public Attachment[] getAttachments() {
        return attachments;
    }
}
