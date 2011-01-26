package com.esri.sde.sdk.client; 

public class SeRaster {
    
    public static int SE_RASTER_INTERLEAVE_NONE = 0;
    public static int SE_RASTER_INTERLEAVE_BSQ_91 = 0;
    public static int SE_RASTER_INTERLEAVE_BIP_91 = 0;
    public static int SE_RASTER_INTERLEAVE_BIP = 0;
    public static int SE_RASTER_INTERLEAVE_BIL_91 = 0;
    public static int SE_RASTER_INTERLEAVE_BIL = 0;
    public static int SE_COLORMAP_DATA_BYTE = 0;
    public static int SE_COLORMAP_DATA_SHORT = 0;
    public static int SE_COLORMAP_RGBA = 0;
    public static int SE_COLORMAP_RGB = 0;
    public static int SE_PIXEL_TYPE_64BIT_REAL = 0;
    public static int SE_PIXEL_TYPE_32BIT_S = 0;
    public static int SE_PIXEL_TYPE_32BIT_U = 0;
    public static int SE_PIXEL_TYPE_16BIT_S = 0;
    public static int SE_PIXEL_TYPE_16BIT_U = 0;
    public static int SE_PIXEL_TYPE_4BIT = 0;
    public static int SE_INTERPOLATION_BILINEAR = 3;
    public static int SE_INTERPOLATION_NONE = 2;
    public static int SE_INTERPOLATION_NEAREST = 1;
    public static int SE_INTERPOLATION_BICUBIC = 0;
    public static int SE_RASTER_INTERLEAVE_BSQ = 0;
    public static int SE_PIXEL_TYPE_8BIT_U = 1;
    public static int SE_PIXEL_TYPE_8BIT_S = 2;
    public static int SE_PIXEL_TYPE_1BIT = 3;
    public static int SE_PIXEL_TYPE_32BIT_REAL = 5;
    
    public static int SE_COMPRESSION_NONE = 100;
    public static int SE_COMPRESSION_LZ77 = 101;
    public static int SE_COMPRESSION_JPEG = 102;
    public static int SE_COMPRESSION_JP2 = 103;

    public SeRasterBand[] getBands() {
        return null;
    }

    public SeObjectId getRasterId() {
        return null;
    }

    public void getInfoById(SeObjectId rasterId) throws SeException{    
    }
    
    
}
