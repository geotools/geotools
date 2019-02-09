/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 */

package com.esri.sde.sdk.client;

public class SeInstance {

    public SeInstance(String s, int i) throws SeException {}

    public String getServerName() {
        return null;
    }

    public SeInstanceStatus getStatus() {
        return null;
    }

    public SeInstanceConfiguration getConfiguration() {
        return null;
    }

    public class SeInstanceStatus {
        public SeRelease getSeRelease() {
            return null;
        }

        public boolean isAccepting() {
            return false;
        }

        public boolean isBlocking() {
            return false;
        }
    }

    public class SeInstanceConfiguration {
        public boolean getReadOnlyInstance() {
            return false;
        }

        public String getHomePath() {
            return null;
        }

        public String getLogPath() {
            return null;
        }

        public int getStreamPoolSize() {
            return 0;
        }

        public int getMaxConnections() {
            return 0;
        }

        public int getMaxLayers() {
            return 0;
        }

        public int getMaxStreams() {
            return 0;
        }
    }
}
