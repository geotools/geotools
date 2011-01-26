package com.esri.sde.sdk.client;

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;

public class SeRasterBand {
    
    public SeObjectId getId() { return null; }
    public boolean hasColorMap() { return false; }
    
    public SeRasterBandColorMap getColorMap() { return null; }
    
    public class SeRasterBandColorMap {
        
    }

    public void setColorMap(int colorMapType, DataBufferByte dataBuffer) {

    }
    public void alter() throws SeException{
    }
    public DataBuffer getColorMapData() throws SeException{
        return null;
    }
    public int getColorMapType() throws SeException{
        return 0;
    }
    public int getColorMapNumBanks() throws SeException{
        return 0;
    }
    public int getColorMapDataType() throws SeException{
        return 0;
    }
    public int getColorMapNumEntries() throws SeException{
        return 0;
    }
    public int getBandNumber() {
        return 0;
    }
    public boolean hasStats() {
        return false;
    }
    public double getStatsMin() throws SeException{
        return 0;
    }
    public double getStatsMax() throws SeException{
        return 0;
    }
    public String getBandName() {
        // TODO Auto-generated method stub
        return null;
    }
    public SeObjectId getRasterId() {
        // TODO Auto-generated method stub
        return null;
    }
    public SeObjectId getRasterColumnId() {
        // TODO Auto-generated method stub
        return null;
    }
    public int getBandHeight() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getBandWidth() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getCompressionType() {
        // TODO Auto-generated method stub
        return 0;
    }
    public SeExtent getExtent() {
        // TODO Auto-generated method stub
        return null;
    }
    public int getPixelType() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getInterleave() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getInterpolation() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getMaxLevel() {
        // TODO Auto-generated method stub
        return 0;
    }
    public boolean skipLevelOne() {
        // TODO Auto-generated method stub
        return false;
    }
    public int getTileWidth() {
        // TODO Auto-generated method stub
        return 0;
    }
    public int getTileHeight() {
        // TODO Auto-generated method stub
        return 0;
    }
    public SDEPoint getTileOrigin() throws SeException{
        // TODO Auto-generated method stub
        return null;
    }
    public double getStatsMean() {
        // TODO Auto-generated method stub
        return 0;
    }
    public double getStatsStdDev() {
        // TODO Auto-generated method stub
        return 0;
    }
    public void setColorMap(int colorMapType, DataBuffer dataBuffer) {
        // TODO Auto-generated method stub
        
    }

}
