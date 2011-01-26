package org.geotools.arcsde.raster.io;


public class BitmaskToNoDataConverterTest {

    // @Test
    // public void testGetInstance8BitU() throws IOException {
    // RasterDatasetInfo rasterInfo;
    // BitmaskToNoDataConverter noData;
    //
    // int noDataValue;
    // double statsMin;
    // double statsMax;
    //
    // noDataValue = 0;
    // statsMin = 1;
    // statsMax = 255;
    // rasterInfo = createRasterInfo(TYPE_8BIT_U, noDataValue, statsMin, statsMax);
    // noData = BitmaskToNoDataConverter.getInstance(rasterInfo, 1L);
    // assertNotNull(noData);
    //
    // noDataValue = 255;
    // statsMin = 0;
    // statsMax = 254;
    // rasterInfo = createRasterInfo(TYPE_8BIT_U, noDataValue, statsMin, statsMax);
    // noData = BitmaskToNoDataConverter.getInstance(rasterInfo, 1L);
    // assertNotNull(noData);
    // assertTrue(noData instanceof BitmaskToNoDataConverter.Unsigned8bitConverter);
    //
    // final int samplesPerTile = rasterInfo.getTileDimension(0).width
    // * rasterInfo.getTileDimension(0).height;
    //
    // /*
    // * bulk set, a whole no-data tile
    // */
    // byte[] tileData = new byte[samplesPerTile];
    // noData.setAll(1L, tileData);
    // DataInputStream in = new DataInputStream(new ByteArrayInputStream(tileData));
    // for (int sampleN = 0; sampleN < samplesPerTile; sampleN++) {
    // assertEquals(255, in.readByte() & 0xFF);
    // }
    //
    // /*
    // * bitmask data states the first 8 and last 8 samples are no-data
    // */
    // byte[] bitmaskData = new byte[(int) Math.ceil(samplesPerTile / 8D)];
    // Arrays.fill(bitmaskData, (byte) 0xFF);
    // // set the first 8 and last 8 samples to no-data
    // bitmaskData[0] = 0x00;
    // bitmaskData[bitmaskData.length - 1] = 0x00;
    //
    // final byte dataValue = 5;
    // byte[] expected = new byte[samplesPerTile];
    // Arrays.fill(expected, dataValue);
    // Arrays.fill(expected, 0, 8, (byte) noDataValue);
    // Arrays.fill(expected, expected.length - 8, expected.length, (byte) noDataValue);
    //
    // tileData = new byte[samplesPerTile];
    // Arrays.fill(tileData, dataValue);
    // noData.setNoData(1L, tileData, bitmaskData);
    // assertTrue("Arrays differ, expected:" + Arrays.toString(expected) + ", actual:"
    // + Arrays.toString(tileData), Arrays.equals(expected, tileData));
    //
    // /*
    // * set individual sample
    // */
    // tileData = new byte[samplesPerTile];
    // noData.setNoData(1L, 0, tileData);
    // assertEquals(noDataValue, tileData[0] & 0xFF);
    // noData.setNoData(1L, 5, tileData);
    // assertEquals(noDataValue, tileData[5] & 0xFF);
    // noData.setNoData(1L, tileData.length - 1, tileData);
    // assertEquals(noDataValue, tileData[tileData.length - 1] & 0xFF);
    // }
    //
    // @Test
    // public void testGetInstance1Bit() {
    // RasterDatasetInfo rasterInfo;
    // BitmaskToNoDataConverter noData;
    //
    // int noDataValue;
    // double statsMin;
    // double statsMax;
    //
    // noDataValue = 0;
    // statsMin = 0;
    // statsMax = 1;
    // rasterInfo = createRasterInfo(TYPE_1BIT, noDataValue, statsMin, statsMax);
    // try {
    // noData = BitmaskToNoDataConverter.getInstance(rasterInfo, 1L);
    // fail("Expected UOE, noDataValue == 0 is non valid");
    // } catch (UnsupportedOperationException e) {
    // assertTrue(true);
    // }
    //
    // noDataValue = 2;
    // statsMin = 0;
    // statsMax = 1;
    // rasterInfo = createRasterInfo(TYPE_1BIT, noDataValue, statsMin, statsMax);
    // noData = BitmaskToNoDataConverter.getInstance(rasterInfo, 1L);
    // assertNotNull(noData);
    // // make sure promotion from 1 to 8 bit is being taking place here and hence we got a 8-bit-u
    // // no-data setter
    // assertTrue(noData instanceof Unsigned8bitConverter);
    // }
    //
    // @Test
    // public void testGetInstance4Bit() {
    // RasterDatasetInfo rasterInfo;
    // BitmaskToNoDataConverter noData;
    //
    // int noDataValue;
    // double statsMin;
    // double statsMax;
    //
    // statsMin = TYPE_4BIT.getSampleValueRange().getMinimum();
    // statsMax = TYPE_4BIT.getSampleValueRange().getMaximum();
    // noDataValue = 0;
    //
    // rasterInfo = createRasterInfo(TYPE_4BIT, noDataValue, statsMin, statsMax);
    // try {
    // noData = BitmaskToNoDataConverter.getInstance(rasterInfo, 1L);
    // fail("Expected UOE, noDataValue == 0 is non valid");
    // } catch (UnsupportedOperationException e) {
    // assertTrue(true);
    // }
    //
    // noDataValue = (int) (statsMax + 1);
    //
    // rasterInfo = createRasterInfo(TYPE_4BIT, noDataValue, statsMin, statsMax);
    // noData = BitmaskToNoDataConverter.getInstance(rasterInfo, 1L);
    // assertNotNull(noData);
    // // make sure promotion from 4 to 8 bit is being taking place here and hence we got a 8-bit-u
    // // no-data setter
    // assertTrue(noData instanceof Unsigned8bitConverter);
    // }
    //
    // @Test
    // public void testGetInstance16BitU() throws IOException {
    // RasterDatasetInfo rasterInfo;
    // BitmaskToNoDataConverter noData;
    //
    // final int noDataValue = (int) TYPE_16BIT_U.getSampleValueRange().getMaximum();
    // double statsMin;
    // double statsMax;
    //
    // statsMin = TYPE_16BIT_U.getSampleValueRange().getMinimum();
    // statsMax = TYPE_16BIT_U.getSampleValueRange().getMaximum() - 1;
    // rasterInfo = createRasterInfo(TYPE_16BIT_U, noDataValue, statsMin, statsMax);
    // noData = BitmaskToNoDataConverter.getInstance(rasterInfo, 1L);
    // assertNotNull(noData);
    //
    // final int samplesPerTile = rasterInfo.getTileDimension(0).width
    // * rasterInfo.getTileDimension(0).height;
    //
    // final int dataValue = 5;
    //
    // byte[] tileData = new byte[TYPE_16BIT_U.getBitsPerSample() * samplesPerTile];
    // noData.setAll(1L, tileData);
    // DataInputStream in = new DataInputStream(new ByteArrayInputStream(tileData));
    // for (int sampleN = 0; sampleN < samplesPerTile; sampleN++) {
    // assertEquals(noDataValue, in.readUnsignedShort());
    // }
    //
    // byte[] bitmaskData = new byte[(int) Math.ceil(samplesPerTile / 8D)];
    // Arrays.fill(bitmaskData, (byte) 0xFF);
    // // set the first 8 and last 8 samples to no-data
    // bitmaskData[0] = 0x00;
    // bitmaskData[bitmaskData.length - 1] = 0x00;
    //
    // byte[] expected;
    // {
    // ByteArrayOutputStream actualOut = new ByteArrayOutputStream();
    // DataOutputStream actualWriter = new DataOutputStream(actualOut);
    //
    // ByteArrayOutputStream expectedOut = new ByteArrayOutputStream();
    // DataOutputStream expectedWriter = new DataOutputStream(expectedOut);
    // for (int i = 0; i < samplesPerTile; i++) {
    // actualWriter.writeShort(dataValue);
    //
    // if (i < 8 || i >= samplesPerTile - 8) {
    // expectedWriter.writeShort(noDataValue);
    // } else {
    // expectedWriter.writeShort(dataValue);
    // }
    // }
    // tileData = actualOut.toByteArray();
    // expected = expectedOut.toByteArray();
    // }
    //
    // noData.setNoData(1L, tileData, bitmaskData);
    // assertTrue("Arrays differ, expected:" + Arrays.toString(expected) + ", actual:"
    // + Arrays.toString(tileData), Arrays.equals(expected, tileData));
    //
    // /*
    // * set individual sample
    // */
    // final int bitsPerSample = TYPE_16BIT_U.getBitsPerSample();
    // tileData = new byte[bitsPerSample * samplesPerTile];
    //
    // in = new DataInputStream(new ByteArrayInputStream(tileData));
    // noData.setNoData(1L, 0, tileData);
    // assertEquals(noDataValue, in.readUnsignedShort());
    //
    // in = new DataInputStream(new ByteArrayInputStream(tileData, 5 * (bitsPerSample / 8), 2));
    // noData.setNoData(1L, 5, tileData);
    // assertEquals(noDataValue, in.readUnsignedShort());
    //
    // in = new DataInputStream(new ByteArrayInputStream(tileData, (samplesPerTile - 1)
    // * (bitsPerSample / 8), 2));
    // noData.setNoData(1L, samplesPerTile - 1, tileData);
    // assertEquals(noDataValue, in.readUnsignedShort());
    // }
    //
    // private RasterDatasetInfo createRasterInfo(RasterCellType nativeType, Number noDataValue,
    // double statsMin, double statsMax) {
    //
    // RasterDatasetInfo datasetInfo = new RasterDatasetInfo();
    //
    // List<RasterInfo> datasetRasters = new ArrayList<RasterInfo>();
    // // fake a 3x8 pixel raster so it's a matrix of 24 elements and it matches a full bitmask
    // // array (no unused bitmask bits)
    // RasterInfo rasterInfo = new RasterInfo(1L, 3, 8);
    // datasetRasters.add(rasterInfo);
    //
    // rasterInfo.addPyramidLevel(0, new ReferencedEnvelope(), new Point(), new Point(), 10, 10,
    // new Dimension(100, 100));
    // List<RasterBandInfo> bands = new ArrayList<RasterBandInfo>();
    // RasterBandInfo bandInfo = new RasterBandInfo(1L, nativeType, noDataValue, statsMin,
    // statsMax);
    // bands.add(bandInfo);
    //
    // // bandInfo.bandId = 1L;
    // // // the native type
    // // bandInfo.cellType = nativeType;
    // // // the target type will be determined based on the native type bounds and the band's
    // // // statistics
    // // bandInfo.noDataValue = noDataValue;
    // // bandInfo.statsMin = statsMin;
    // // bandInfo.statsMax = statsMax;
    //
    // rasterInfo.setBands(bands);
    //
    // datasetInfo.setPyramidInfo(datasetRasters);
    //
    // return datasetInfo;
    // }

}
