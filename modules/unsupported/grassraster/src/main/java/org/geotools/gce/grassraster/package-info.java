/**
 * <H1>GRASS input/output drivers</H1>
 * 
 * <p>
 * In this package the I/O drivers for reading and writing GRASS raster maps
 * are placed. 
 * </p>
 * <p>
 * To better understand the code a good overview should be taken on the 
 * GRASS database-Location-Mapset structure, of which a short introduction 
 * is given below.
 * </p>
 * 
 * <h2>File structure of GRASS Location</h2>
 *
 * A GRASS raster map consists of several files in several subdirectories in a mapset, organized as follows: 
 *
 * <ul>
 * <li><b>cellhd/</b>: map header including projection code, coordinates representing the spatial extent of the raster map, number of rows and columns, resolution, and information about map compression;</li>
 * <li><b>cell/, fcell/ or grid3/</b>: generic matrix of values in a compressed, portable format which depends on the raster data type (integer, floating point or 3D grid);</li>
 * <li><b>hist/</b>: history file which contains metadata such as the data source, the command that was used to generate the raster map, or other information provided by the user;</li>
 * <li><b>cats/</b>: optional category file which contains text or numeric labels assigned to the raster map categories;</li>
 * <li><b>colr/</b>: optional color table;</li>
 * <li><b>cell_misc/</b>: optional timestamp, range of values, quantization rules (for floating point maps) and null (no-data) files;</li>
 * </ul>
 *
 * Most important files and folders for now:
 *
 * <ul>
 * <li>WIND - contains the active processing region and the resolution</li>
 * <li>PROJ_INFO - contains the information about the projection</li>
 * <li>PROJ_UNITS - contains the information about projection units used</li>
 * <li>cell, fcell - contain the raster files</li>
 * <li>vector - contain the vector data since GRASS 6</li>
 * <li>sites_list - contain the sites type data, deprecated from GRASS 6 on, but maintained in JGrass</li>
 * </ul>
 * 
 * <h2>The cell header file</h2>
 *<p>
 * A typical grass map header looks like the following: <br>
 * <code>
 * proj: 1 <br>
 * zone: 13 <br>
 * north: 4928000 <br>
 * south: 4914000 <br>
 * east: 609000 <br>
 * west: 590000 <br>
 * cols: 950 <br>
 * rows: 700 <br>
 * e-w resol: 20 <br>
 * n-s resol: 20 <br>
 * format: 0 <br>
 * compressed: 1 
 * </code>
 * <br>
 * <br>
 * 
 * <h3>Reclassified files</h3>
 * 
 * If the first line reports 'reclass' then this file is a 
 * reclassified file and the original data file is given by the following
 *  two lines: <br>
 * <code>
 * reclass <br>
 * name: soils <br>
 * mapset: PERMANENT <br>
 * #1 5 3 8 .... .... 
 * </code>
 * 
 * <h2>The color table file</h2>
 * <p>
 * Colortables for GRASS 5 and greater are supported. 
 * </p>
 * 
 * <p>The format of the color file, which is located in 
 * <b>location/mapset/colr/mapname</b> is the following:</p>
 * <p>The first line is a % character and two numbers indicating the minimum 
 * and maximum data values which have colors. <b>Note that in JGrass after the
 * range values we add a third value for alpha support.</b></p>
 * <p>After the first line, the list of color rules appears, that can be 
 * of the following formats:
 * <ul>
 * <li><code>value1:r:g:b value2:r:g:b</code> interpolation of colors between 
 * the two values with the two colors</li> 
 * <li><code>value1:grey value2:grey</code interpolation of grayscale between 
 * the two values with the two grey values></li> 
 * <li><code>value1:r:g:b</code> assumption that it means that value1 == 
 * value2</li> 
 * <li><code>nv:r:g:b</code> novalues could also have color with such a rule.</li>
 * </ul>
 * </p>
 * 
 */
package org.geotools.gce.grassraster;
