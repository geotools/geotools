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

/** Handles xml parse events for {@link org.opengis.style.RasterSymbolizer} elements. */
public class RasterSymbolizerHandler extends SymbolizerHandler {
    @Override
    public void element(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("RasterSymbolizer".equals(name)) {
            context.mapping().scalar("raster").mapping();
        } else if ("Opacity".equals(name)) {
            context.scalar("opacity").push(new ExpressionHandler());
        } else if ("ColorMap".equals(name)) {
            context.scalar("color-map").push(new ColorMapHandler());
        } else if ("ContrastEnhancement".equals(name)) {
            context.scalar("contrast-enhancement").push(new ContrastEnhancementHandler());
        } else if ("ChannelSelection".equals(name)) {
            context.scalar("channels").push(new ChannelSelectionHandler());
        } else if ("OverlapBehavior".equals(name)) {
            context.scalar("overlap").push(new OverlapBehaviorHandler());
        } else if ("ShadedRelief".equals(name)) {
            context.scalar("shaded-relief").push(new ShadedReliefHandler());
        } else if ("ImageOutline".equals(name)) {
            context.scalar("image-outline").push(new ImageOutlineHandler());
        } else {
            super.element(xml, context);
        }
    }

    @Override
    public void endElement(XMLStreamReader xml, SldTransformContext context)
            throws XMLStreamException, IOException {
        String name = xml.getLocalName();
        if ("RasterSymbolizer".equals(name)) {
            dumpOptions(context).endMapping().endMapping().pop();
        }
        super.endElement(xml, context);
    }

    static class ColorMapHandler extends SldTransformHandler {
        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("ColorMap".equals(name)) {
                context.mapping();

                String type = xml.getAttributeValue(null, "type");
                if (type != null) {
                    context.scalar("type").scalar(type);
                }

                context.scalar("entries").sequence();
            } else if ("ColorMapEntry".equals(name)) {
                StringBuilder sb = new StringBuilder("(");
                sb.append(xml.getAttributeValue(null, "color"));

                String opacity = xml.getAttributeValue(null, "opacity");
                sb.append(",").append(opacity != null ? opacity : "");

                String quantity = xml.getAttributeValue(null, "quantity");
                sb.append(",").append(quantity != null ? quantity : "");

                String label = xml.getAttributeValue(null, "label");
                sb.append(",").append(label != null ? label : "");

                context.scalar(sb.append(")").toString());
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            if ("ColorMap".equals(xml.getLocalName())) {
                context.endSequence().endMapping().pop();
            }
        }
    }

    static class ContrastEnhancementHandler extends SldTransformHandler {
        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("ContrastEnhancement".equals(name)) {
                context.mapping();
            } else if ("Normalize".equals(name)) {
                context.scalar("mode").scalar("normalize");
            } else if ("Histogram".equals(name)) {
                context.scalar("mode").scalar("histogram");
            } else if ("GammaValue".equals(name)) {
                context.scalar("gamma").scalar(xml.getElementText());
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            if ("ContrastEnhancement".equals(xml.getLocalName())) {
                context.endMapping().pop();
            }
        }
    }

    static class ChannelSelectionHandler extends SldTransformHandler {
        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("ChannelSelection".equals(name)) {
                context.sequence();
            } else if ("RedChannel".equals(name)) {
                context.scalar("red").push(new ChannelHandler());
            } else if ("GreenChannel".equals(name)) {
                context.scalar("green").push(new ChannelHandler());
            } else if ("BlueChannel".equals(name)) {
                context.scalar("blue").push(new ChannelHandler());
            } else if ("GrayChannel".equals(name)) {
                context.scalar("gray").push(new ChannelHandler());
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("ChannelSelection".equals(name)) {
                context.endSequence();
            }
        }
    }

    static class ChannelHandler extends SldTransformHandler {
        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if (name.endsWith("Channel")) {
                context.mapping();
            } else if ("SourceChannelName".equals(name)) {
                context.scalar("source").scalar(xml.getElementText());
            } else if ("ContrastEnhancement".equals(name)) {
                context.scalar("contrast-enhancement").push(new ContrastEnhancementHandler());
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if (name.endsWith("Channel")) {
                context.endMapping().pop();
            }
        }
    }

    static class OverlapBehaviorHandler extends SldTransformHandler {
        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("LATEST_ON_TOP".equals(name)) {
                context.scalar("latest-on-top").pop();
            } else if ("EARLIEST_ON_TOP".equals(name)) {
                context.scalar("earliest-on-top").pop();
            } else if ("AVERAGE".equals(name)) {
                context.scalar("average").pop();
            } else if ("RANDOM".equals(name)) {
                context.scalar("random").pop();
            }
        }
    }

    static class ShadedReliefHandler extends SldTransformHandler {
        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("ShadedRelief".equals(name)) {
                context.mapping();
            } else if ("BrightnessOnly".equals(name)) {
                context.scalar("brightness-only").scalar(xml.getElementText());
            } else if ("ReliefFactor".equals(name)) {
                context.scalar("relief-factory").scalar(xml.getElementText());
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("ShadedRelief".equals(name)) {
                context.endMapping().pop();
            }
        }
    }

    static class ImageOutlineHandler extends SldTransformHandler {
        @Override
        public void element(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("ImageOutline".equals(name)) {
                context.scalar("image-outline").mapping();
            } else if ("LineSymbolizer".equals(name)) {
                context.push(new LineSymbolizerHandler());
            } else if ("PolygonSymbolizer".equals(name)) {
                context.push(new PolygonSymbolizerHandler());
            }
        }

        @Override
        public void endElement(XMLStreamReader xml, SldTransformContext context)
                throws XMLStreamException, IOException {
            String name = xml.getLocalName();
            if ("ImageOutline".equals(name)) {
                context.endMapping().pop();
            }
        }
    }
}
