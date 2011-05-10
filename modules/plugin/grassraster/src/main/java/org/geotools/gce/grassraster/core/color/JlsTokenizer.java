/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
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
 /*******************************************************************************
            FILE:  JLSTokenizer.java
   
     DESCRIPTION:  Class providing much the same functionality as 
                   java.util.StringTokenizer, but avoiding some of its
                   more common problems. In particular:
                   <ul>
                   <li><pre>x,,y</pre> yields a blank token between x
                   and y, whereas with StringTokenizer it is swallowed.
                   <li><pre>,a,</pre> yields a blank token both before
                   and after a, whereas with StringTokenizer both are
                   swallowed.
                   </ul>

                   Some pieces of StringTokenizer aren't supported, such
                   as returning delimiters and changing delimiters during
                   operation.
                   <p>
                   This class is deliberately <b>not</b> a subclass of
                   StringTokenizer as the behaviour is significantly 
                   different. It can, however, be used in many places
                   where StringTokenizer is appropriate.
           NOTES:  
          AUTHOR:  JSkeet
           EMAIL:  
         COMPANY:  
       COPYRIGHT:  
         VERSION:  
         CREATED:  
        REVISION:  ---

  ******************************************************************************

    This library is free software; you can redistribute it and/or 
    modify it under the terms of the GNU Library General Public 
    License as published by the Free Software Foundation; either 
    version 2 of the License, or (at your option) any later version. 
 
    This library is distributed in the hope that it will be useful, 
    but WITHOUT ANY WARRANTY; without even the implied warranty of 
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
    Library General Public License for more details. 
 
    You should have received a copy of the GNU Library General Public 
    License along with this library; if not, write to the Free 
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 
    USA 

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:
    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.
    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.

  ******************************************************************************

      CHANGE LOG:

         version: 
        comments: changes
          author: 
         created:  
  *****************************************************************************/

package org.geotools.gce.grassraster.core.color;

import java.util.NoSuchElementException;

//import java.util.NoSuchElementException;

public class JlsTokenizer
{
  /* Data to parse */
  private String data;

  /* Length of data */
  private int dataLength;

  /* Delimiter string */
  private String delimiters;

  /* Current position. This is usually one place
   * after the previously seen delimiter, so if the
   * character at this position *is* a delimiter, the
   * next token is empty. If the position is at the
   * end of the string (after the last character), that
   * shows that there is one final empty token to return.
   * If the position is *past* the very end of the string
   * (ie dataLength+1 or higher) then there are no more
   * tokens. */
  private int position=0;

  /**
   * Constructs a string tokenizer for the specified string. The 
   * tokenizer uses the default delimiter set, which is 
   * <code>"&nbsp;&#92;t&#92;n&#92;r&#92;f"</code>: the space character, 
   * the tab character, the newline character, the carriage-return character,
   * and the form-feed character. Delimiter characters themselves will 
   * not be treated as tokens.
   *
   * @param   str   a string to be parsed.
   */
  public JlsTokenizer (String str)
  {
    this (str, "\t\n\r\f");
  }

  /**
   * Constructs a string tokenizer for the specified string. The 
   * characters in the <code>delim</code> argument are the delimiters 
   * for separating tokens. Delimiter characters themselves will not 
   * be treated as tokens.
   *
   * @param   str     a string to be parsed.
   * @param   delim   the delimiters.
   */
  public JlsTokenizer (String str, String delim)
  {
    this.data = str;
    this.delimiters=delim;
    this.dataLength = data.length();
  }

  /**
   * Tests if there are more tokens available from this tokenizer's string. 
   * If this method returns <tt>true</tt>, then a subsequent call to 
   * <tt>nextToken</tt> with no argument will successfully return a token.
   *
   * @return  <code>true</code> if and only if there is at least one token 
   *          in the string after the current position; <code>false</code> 
   *          otherwise.
   */
  public boolean hasMoreTokens() 
  {
    return !(position > dataLength);
  }

  /**
   * Returns the next token from this string tokenizer.
   *
   * @return     the next token from this string tokenizer.
   * @exception  NoSuchElementException  if there are no more tokens in this
   *             tokenizer's string.
   */
  public String nextToken() 
  {
    int delPos;
    /* Scan for the next delimiter or end of data */
    for (delPos = position; delPos < dataLength; delPos++)
    {
      char c = data.charAt (delPos);
      if (delimiters.indexOf (c) != -1)
        break;
    }
    /* Find out what the token should be */
    String ret = data.substring (position, delPos);
    /* Set the position to the next position */
    position = delPos+1;
    return ret;
  }

  /**
   * Returns the rest of the string.
   *
   * @return     the rest of the string.
   * @exception  NoSuchElementException  if there are no more tokens in this
   *             tokenizer's string.
   */
  public String remainingToken()
  {
    return data.substring(position);
//    int delPos;
//    /* Scan for the next delimiter or end of data */
//    for (delPos = position; delPos < dataLength; delPos++)
//    {
//        char c = data.charAt (delPos);
//        if (delimiters.indexOf (c) != -1)
//            break;
//    }
//    /* Find out what the token should be */
//    String ret = data.substring (position, delPos);
//    /* Set the position to the next position */
//    position = delPos+1;
//    return ret;
  }

  /**
   * Returns the same value as the <code>hasMoreTokens</code>
   * method. It exists so that this class can implement the
   * <code>Enumeration</code> interface. 
   *
   * @return  <code>true</code> if there are more tokens;
   *          <code>false</code> otherwise.
   */
  public boolean hasMoreElements() 
  {
    return hasMoreTokens();
  }

  /**
   * Returns the same value as the <code>nextToken</code> method,
   * except that its declared return value is <code>Object</code> rather than
   * <code>String</code>. It exists so that this class can implement the
   * <code>Enumeration</code> interface. 
   *
   * @return     the next token in the string.
   * @exception  NoSuchElementException  if there are no more tokens in this
   *             tokenizer's string.
   */
  public Object nextElement() 
  {
    return nextToken();
  }

  /**
   * Calculates the number of times that this tokenizer's 
   * <code>nextToken</code> method can be called before it generates an 
   * exception. The current position is not advanced.
   *
   * @return  the number of tokens remaining in the string using the current
   *          delimiter set.
   * @see     uk.org.skeet.util.JlsTokenizer#nextToken()
   */
  public int countTokens() 
  {
    int savedPosition = position;
    int count=0;
    while (hasMoreTokens())
    {
      nextToken();
      count++;
    }
    position = savedPosition;
    return count;
  }
}
