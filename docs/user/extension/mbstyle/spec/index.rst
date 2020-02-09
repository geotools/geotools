Mapbox Style Specification
==========================

A Mapbox style is a document that defines the visual appearance of a map: what data to draw, the order to draw it in, and how to style the data when drawing it. A style document is a `JSON <http://www.json.org/>`__ object with specific root level and nested properties. This specification defines and describes these properties.

The intended audience of this quick reference includes:

- Advanced designers and cartographers who want to write styles by hand.
- GeoTools developers using the gt-mbstyle extension
- Authors of software that generates or processes Mapbox styles.

Feature support is provided for the `Mapbox GL JS <https://www.mapbox.com/mapbox-gl-js/api/>`__, the `Open Layers Mapbox Style utility <https://npmjs.com/package/ol-mapbox-style>`__ and the GeoTools mbstyle module.

.. toctree::
   :maxdepth: 1

   root
   light
   sources
   sprite
   glyphs
   transition
   layers
   types
   expressions
   other

.. include:: footer.txt