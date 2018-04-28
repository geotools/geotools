/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.transform.sld;

import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/** Handles xml parse events for {@link org.geotools.styling.TextSymbolizer} elements. */
public class TextSymbolizerHandler extends SymbolizerHandler {
    @Override
    public void element(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("TextSymbolizer".equals(name)) {
            context.mapping().scalar("text").mapping();
        } else if ("Label".equals(name)) {
            context.scalar("label").push(new ExpressionHandler());
        } else if ("Font".equals(name)) {
            context.push(new FontHandler());
        } else if ("LabelPlacement".equals(name)) {
            context.scalar("placement").push(new PlacementHandler());
        } else if ("Halo".equals(name)) {
            context.scalar("halo").push(new HaloHandler());
        } else if ("Fill".equals(name)) {
            context.push(new FillHandler());
        } else {
            super.element(xml, context);
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("TextSymbolizer".equals(name)) {
            dumpOptions(context).endMapping().endMapping().pop();
        } else {
            super.endElement(xml, context);
        }
    }

    static class FontHandler extends SldTransformHandler {

        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("CssParameter".equals(name) || "SvgParameter".equals(name)) {
                context.push(new ParameterHandler());
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("Font".equals(name)) {
                context.pop();
            }
        }
    }

    static class PlacementHandler extends SldTransformHandler {

        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("LabelPlacement".equals(name)) {
                context.mapping();
            } else if ("PointPlacement".equals(name)) {
                context.scalar("type").scalar("point");
            } else if ("LinePlacement".equals(name)) {
                context.scalar("type").scalar("line");
            } else if ("AnchorPoint".equals(name)) {
                context.scalar("anchor").push(new AnchorHandler());
            } else if ("Displacement".equals(name)) {
                context.scalar("displacement").push(new DisplacementHandler());
            } else if ("Rotation".equals(name)) {
                context.scalar("rotation").push(new ExpressionHandler());
            } else if ("PerpendicularOffset".equals(name)) {
                context.scalar("offset").push(new ExpressionHandler());
            } else if ("IsRepeated".equals(name)) {
                context.scalar("repeat").scalar(xml.getElementText());
            } else if ("IsAligned".equals(name)) {
                context.scalar("align").scalar(xml.getElementText());
            } else if ("GeneralizeLine".equals(name)) {
                context.scalar("generalize").scalar(xml.getElementText());
            } else if ("InitialGap".equals(name)) {
                context.scalar("initial-gap").push(new ExpressionHandler());
            } else if ("Gap".equals(name)) {
                context.scalar("gap").push(new ExpressionHandler());
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("LabelPlacement".equals(name)) {
                context.endMapping().pop();
            }
        }
    }

    static class HaloHandler extends SldTransformHandler {
        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("Halo".equals(name)) {
                context.mapping();
            } else if ("Fill".equals(name)) {
                context.push(new FillHandler());
            } else if ("Radius".equals(name)) {
                context.scalar("radius").push(new ExpressionHandler());
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("Halo".equals(name)) {
                context.endMapping().pop();
            }
        }
    }
}
