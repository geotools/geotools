import sys, os
sys.path.append(os.path.abspath('..'))
from common import *

# Add any Sphinx extension module names here, as strings. They can be extensions
# coming with Sphinx (named 'sphinx.ext.*') or your custom ones.
extensions = ['sphinx.ext.extlinks']

extlinks = { 
    'geoserver': ('http://docs.geoserver.org/latest/en/user/%s','')
}

# The suffix of source filenames.
source_suffix = '.rst'

# The encoding of source files.
#source_encoding = 'utf-8'

# The master toctree document.
master_doc = 'index'

html_title='GeoTools %s User Guide' % release
