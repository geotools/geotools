import sys, os
sys.path.append(os.path.abspath('..'))
from common import *

# Options for HTML output
# -----------------------

html_title='GeoTools %s Tutorial' % release
html_theme = 'geotools-tutorial'

# -- General configuration -----------------------------------------------------
# Add any Sphinx extension module names here, as strings. They can be extensions
# coming with Sphinx (named 'sphinx.ext.*') or your custom ones.
#
# extension for pdf
# extensions = ['sphinx.ext.autodoc','rst2pdf.pdfbuilder']

# options for pdf
pdf_documents = [
  ('quickstart/eclipse', u'eclipseQuickstart', u'Eclipse Quickstart', u'Jody Garnett\\Micheal Bedward'),
#  ('quickstart/netbeans', u'eclipseQuickstart', u'Eclipse Quickstart', u'Jody Garnett\\Micheal Bedward'),
]

# A comma-separated list of custom stylesheets. Removed 'kerning' to prevent failure on mac
#
# 
pdf_stylesheets = ['sphinx','a4']

# Create a compressed PDF
# Use True/False or 1/0
# Example: compressed=True
#pdf_compressed = False

# A colon-separated list of folders to search for fonts. Example:
# pdf_font_path = ['/usr/share/fonts', '/usr/share/texmf-dist/fonts/']

# Language to be used for hyphenation support
#pdf_language = "en_US"

# Mode for literal blocks wider than the frame. Can be
# overflow, shrink or truncate
pdf_fit_mode = "shrink"

# Section level that forces a break page.
# For example: 1 means top-level sections start in a new page
# 0 means disabled
#pdf_break_level = 0

# When a section starts in a new page, force it to be 'even', 'odd',
# or just use 'any'
#pdf_breakside = 'any'

# Insert footnotes where they are defined instead of
# at the end.
#pdf_inline_footnotes = True

# verbosity level. 0 1 or 2
#pdf_verbosity = 0

# If false, no index is generated.
#pdf_use_index = True

# If false, no modindex is generated.
#pdf_use_modindex = True

# If false, no coverpage is generated.
#pdf_use_coverpage = True

# Name of the cover page template to use
#pdf_cover_template = 'sphinxcover.tmpl'

# Documents to append as an appendix to all manuals.
#pdf_appendices = []

# Enable experimental feature to split table cells. Use it
# if you get "DelayedTable too big" errors
#pdf_splittables = False

# Set the default DPI for images
#pdf_default_dpi = 72

# Enable rst2pdf extension modules (default is empty list)
# you need vectorpdf for better sphinx's graphviz support
#pdf_extensions = ['vectorpdf']

# Page template name for "regular" pages
#pdf_page_template = 'cutePage'



# Options for LaTeX output
# ------------------------

# The paper size ('letter' or 'a4').
#latex_paper_size = 'letter'

# The font size ('10pt', '11pt' or '12pt').
#latex_font_size = '10pt'

# Grouping the document tree into LaTeX files. List of tuples
# (source start file, target name, title, author, document class [howto/manual]).
latex_documents = [
  ('quickstart/eclipse', 'EclipseQuickstart.tex', u'Eclipse Quickstart', u'Jody Garnett /and Micheal Bedward','howto'),
  ('quickstart/netbeans', 'NetBeansQuickstart.tex', u'Netbeans Quickstart', u'Jody Garnett /and Micheal Bedward','howto'),
  ('feature/csv2shp', 'FeatureTutorial.tex', u'Feature Tutorial',u'Jody Garnett /and Micheal Bedward','howto'),
  ('geometry/geometrycrs', 'GeometryCRS.tex', u'Geometry and CRS Tutorial',u'Jody Garnett /and Micheal Bedward','howto'),
  ('filter/query', 'Query.tex', u'Query Tutorial',u'Jody Garnett /and Micheal Bedward','howto'),
 # ('raster/image', 'Image.tex', u'Image Tutorial',u'Jody Garnett /and Micheal Bedward','howto'),
  ('map/style', 'Style.tex', u'Style Tutorial',u'Jody Garnett /and Micheal Bedward','howto'),
]

# The name of an image file (relative to this directory) to place at the top of
# the title page.
#latex_logo = None

# For "manual" documents, if this is true, then toplevel headings are parts,
# not chapters.
#latex_use_parts = False

# Additional stuff for the LaTeX preamble.
#latex_preamble = ''

# Documents to append as an appendix to all manuals.
#latex_appendices = []

# If false, no module index is generated.
#latex_use_modindex = True
