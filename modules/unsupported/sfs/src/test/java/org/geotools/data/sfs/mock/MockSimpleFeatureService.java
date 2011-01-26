/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.sfs.mock;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.StringRepresentation;

/**
 * This is the mock service class it serves following urls
 *
 * http://localhost:8082/simplefeatureservice/capabilities
 * http://localhost:8082/simplefeatureservice/describe/layerAsia
 * http://localhost:8082/simplefeatureservice/data/layerAsia?mode=features
 * http://localhost:8082/simplefeatureservice/data/layerAsia?mode=count
 * http://localhost:8082/simplefeatureservice/data/layerAsia?mode=bounds
 * http://localhost:8082/simplefeatureservice/data/layerAsia/tiger_roads.1
 *
 *
 * -----
 * Following commands can be used to test mockup-service at terminal
 * curl -i -H "Accept: application/json" http://localhost:8082/simplefeatureservice/data/layerAsia?mode=features
 * curl -i -H "Accept: application/json" http://localhost:8082/simplefeatureservice/data/layerAsia?mode=count
 * curl -i -H "Accept: application/json" http://localhost:8082/simplefeatureservice/data/layerAsia?mode=bounds
 * curl -i -H "Accept: application/json" -X POST -d "mode=bounds" http://localhost:8082/simplefeatureservice/data/layerAsia
 * curl -i -H "Accept: application/json" http://localhost:8082/simplefeatureservice/data/layerAsia/tiger_roads.1
 *
 * -----
 *
 * There are not test cases for this project as
 * you can just fire up your favorite browser and test the mock service
 *
 * @author 
 */
public class MockSimpleFeatureService extends Application {

    public MockSimpleFeatureService() {
        super();
    }

    public MockSimpleFeatureService(Context context) {
        super(context);
    }

    /**
     * Creates a root Rest-let that will receive all incoming calls.
     *
     */
    @Override
    public synchronized Restlet createRoot() {
        // Create a router Restlet that routes each call to a
        // new instance of HelloWorldResource.
        Router router = new Router(this.getContext());

        /* Add all the supported mock service urls*/
        router.attach("/capabilities", CapabilitiesResource.class);

        router.attach("/describe/layerAsia", DescribeLayerResource.class);

        router.attach("/data/layerAsia?mode=features", DataLayerResource.class);

        router.attach("/data/layerAsia", DataLayerResource.class);

        router.attach("/data/layerAsia?mode=count", FeatureCountResource.class);

        router.attach("/data/layerAsia?mode=bounds", FeatureBoundsResource.class);

        router.attach("/data/layerAsia/tiger_roads.1", SingleFeatureResource.class);

        Restlet mainpage = new Restlet() {

            @Override
            public void handle(Request request, Response response) {
                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("<html>");
                stringBuilder.append("<head><title>Mockup Service to test OpenDataStore</title></head>");
                stringBuilder.append("<body bgcolor=white>");

                stringBuilder.append("<table border=\"0\">");
                stringBuilder.append("<tr>");
                stringBuilder.append("<td>");
                stringBuilder.append("<h1>Main Page</h1>");
                stringBuilder.append("</td>");
                stringBuilder.append("</tr>");
                stringBuilder.append("</table>");
                stringBuilder.append("</body>");
                stringBuilder.append("</html>");

                response.setEntity(new StringRepresentation(stringBuilder.toString(), MediaType.APPLICATION_JSON));

            }
        };
        router.attach("", mainpage);
        return router;
    }
    
    public static void main(String[] args) {
        try {
            // Create a new Component.
            Component component = new Component();

            // Add a new HTTP server listening on port 8082
            component.getServers().add(Protocol.HTTP, 8082);

            // Attach the application which has all the mock url added
            component.getDefaultHost().attach("/simplefeatureservice", new MockSimpleFeatureService());

            // Start the component.
            component.start();
        } catch (Exception e) {
            
            System.out.println("Exception in StandAloneApplication "+e.getMessage());
        }
    }
}
