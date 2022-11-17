import sys, os
sys.path.append(os.path.abspath('..'))
from common import *

html_theme='geotools-index'
html_title='GeoTools Documentation'

# do not use user extlink for index page generation
del extlinks['user']
