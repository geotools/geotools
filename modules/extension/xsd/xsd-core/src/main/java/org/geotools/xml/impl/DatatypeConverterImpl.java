/*
 * Copyright 2003,2004  The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.geotools.xml.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.util.Calendar;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;


/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 *
 *
 * @source $URL$
 */
public class DatatypeConverterImpl implements DatatypeConverterInterface {
    
    private static DatatypeConverterImpl instance = new DatatypeConverterImpl();
    public static DatatypeConverterImpl getInstance() {
        return instance;
    }
    
    public String parseString(String arg0) {
        return arg0;
    }
    
    public BigInteger parseInteger(String arg0) {
        return new BigInteger(arg0);
    }
    
    public int parseInt(String arg0) {
        return Integer.parseInt(arg0);
    }
    
    public long parseLong(String arg0) {
        return Long.parseLong(arg0);
    }
    
    public short parseShort(String arg0) {
        return Short.parseShort(arg0);
    }
    
    public BigDecimal parseDecimal(String arg0) {
        return new BigDecimal(arg0);
    }
    
    public float parseFloat(String arg0) {
        return Float.parseFloat(arg0);
    }
    
    public double parseDouble(String arg0) {
        return Double.parseDouble(arg0);
    }
    
    public boolean parseBoolean(String arg0) {
        return Boolean.valueOf(arg0).booleanValue();
    }
    
    public byte parseByte(String arg0) {
        return Byte.parseByte(arg0);
    }
    
    public QName parseQName(String arg0, NamespaceContext arg1) {
        int offset = arg0.indexOf(':');
        String uri;
        String localName;
        switch (offset) {
            case -1:
                localName = arg0;
                uri = arg1.getNamespaceURI("");
                if (uri == null) {
                    // Should not happen, indicates an error in the NamespaceContext implementation
                    throw new IllegalArgumentException("The default prefix is not bound.");
                }
                break;
               case 0:
                   throw new IllegalArgumentException("Default prefix must be indicated by not using a colon: " + arg0);
                  default:
                      String prefix = arg0.substring(0, offset);
                      localName = arg0.substring(offset+1);
                      uri = arg1.getNamespaceURI(prefix);
                      if (uri == null) {
                          throw new IllegalArgumentException("The prefix " + prefix + " is not bound.");
                      }
        }
        return new QName(uri, localName);
    }

    public Calendar parseDateTime(String arg0) {
        XsDateTimeFormat format = new XsDateTimeFormat();
        ParsePosition pos = new ParsePosition(0);
        Calendar cal = (Calendar) format.parseObject(arg0, pos);
        if (cal == null) {
            throw new IllegalArgumentException("Failed to parse dateTime " + arg0 +
                    						   " at:" + arg0.substring(pos.getErrorIndex()));
        }
        return cal;
    }

    public byte[] parseBase64Binary(String arg0) {
        try {
            return Base64Binary.decode(arg0);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse " + arg0 + ": " +
                    e.getMessage());
        }
    }
    
    public byte[] parseHexBinary(String arg0) {
        return HexBinary.decode(arg0);
    }
    
    private static final long MAX_UNSIGNED_INT = (((long) Integer.MAX_VALUE) * 2) + 1; 
    public long parseUnsignedInt(String arg0) {
        long l = Long.parseLong(arg0);
        if (l < 0) {
            throw new IllegalArgumentException("Failed to parse UnsignedInt " + arg0 + ": result is negative");
        }
        if (l > MAX_UNSIGNED_INT) {
            throw new IllegalArgumentException("Failed to parse UnsignedInt " + arg0 +
                    ": result exceeds maximum value " + MAX_UNSIGNED_INT);
        }
        return l;
    }
    
    private static final int MAX_UNSIGNED_SHORT = Short.MAX_VALUE * 2 + 1;
    public int parseUnsignedShort(String arg0) {
        int i = Integer.parseInt(arg0);
        if (i < 0) {
            throw new IllegalArgumentException("Failed to parse UnsignedShort " + arg0 + ": result is negative");
        }
        if (i > MAX_UNSIGNED_SHORT) {
            throw new IllegalArgumentException("Failed to parse UnsignedShort " + arg0 +
                    ": result exceeds maximum value " + MAX_UNSIGNED_SHORT);
        }
        return i;
    }

  	public Calendar parseTime(String arg0) {
  	    XsTimeFormat format = new XsTimeFormat();
  	    ParsePosition pos = new ParsePosition(0);
  	    Calendar cal = (Calendar) format.parseObject(arg0, pos);
  	    if (cal == null) {
  	        throw new IllegalArgumentException("Failed to parse time " + arg0 +
  	                						   " at:" + arg0.substring(pos.getErrorIndex()));
  	    }
  	    return cal;
  	}

  	public Calendar parseDate(String arg0) {
  	    XsDateFormat format = new XsDateFormat();
  	    ParsePosition pos = new ParsePosition(0);
  	    Calendar cal = (Calendar) format.parseObject(arg0, pos);
  	    if (cal == null) {
  	        throw new IllegalArgumentException("Failed to parse date " + arg0 +
  	                						   " at:" + arg0.substring(pos.getErrorIndex()));
  	    }
  	    return cal;
  	}

  	public String parseAnySimpleType(String arg0) {
  	    return arg0;
  	}
  	
  	public Duration parseDuration(String pDuration) {
  	    return Duration.valueOf(pDuration);
  	}
  	
  	public String printString(String arg0) {
  	    return arg0;
  	}
  	
  	public String printInteger(BigInteger arg0) {
  	    return arg0.toString();
  	}
  	
  	public String printInt(int arg0) {
  	    return Integer.toString(arg0);
  	}
  	
  	public String printLong(long arg0) {
  	    return Long.toString(arg0);
  	}
  	
  	public String printShort(short arg0) {
  	    return Short.toString(arg0);
  	}
  	
  	public String printDecimal(BigDecimal arg0) {
  	    return arg0.toString();
  	}
  	
  	public String printFloat(float arg0) {
  	    return Float.toString(arg0);
  	}
  	
  	public String printDouble(double arg0) {
  	    return Double.toString(arg0);
  	}

  	public String printBoolean(boolean arg0) {
  	    return (arg0 ? Boolean.TRUE : Boolean.FALSE).toString();
  	}

  	public String printByte(byte arg0) {
  	    return Byte.toString(arg0);
  	}

  	public String printQName(QName arg0, NamespaceContext arg1) {
  	    String prefix = arg1.getPrefix(arg0.getNamespaceURI());
  	    if (prefix == null) {
  	        throw new IllegalArgumentException("The namespace URI " +
  	                						   arg0.getNamespaceURI() +
                                          	   " is not bound.");
  	    } else if (XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
  	        return arg0.getLocalPart();
  	    } else {
  	        return prefix + ":" + arg0.getLocalPart();
  	    }
  	}

    public String printDateTime(Calendar arg0) {
        return new XsDateTimeFormat().format(arg0);
    }

    public String printBase64Binary(byte[] arg0) {
        return Base64Binary.encode(arg0);
    }

  	public String printHexBinary(byte[] arg0) {
  	    return HexBinary.encode(arg0);
  	}

  	public String printUnsignedInt(long arg0) {
  	    return Long.toString(arg0);
  	}

  	public String printUnsignedShort(int arg0) {
  	    return Integer.toString(arg0);
  	}

  	public String printTime(Calendar arg0) {
  	    return new XsTimeFormat().format(arg0);
  	}

  	public String printDate(Calendar arg0) {
  	    return new XsDateFormat().format(arg0);
  	}

  	public String printAnySimpleType(String arg0) {
  	    return arg0;
  	}

  	public String printDuration(Duration pDuration) {
  	    return pDuration.toString();
  	}
}


