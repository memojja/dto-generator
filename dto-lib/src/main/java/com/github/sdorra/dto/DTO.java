/*
 * The MIT License
 *
 * Copyright 2015 Sebastian Sdorra <sebastian.sdorra@triology.de>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */



package com.github.sdorra.dto;

//~--- JDK imports ------------------------------------------------------------

import java.net.URI;

import java.util.LinkedHashMap;

/**
 *
 * @author Sebastian Sdorra
 */
public class DTO extends LinkedHashMap<String, Object>
{

  /** self link */
  public static final String HREF = "href";

  /** meta data */
  public static final String META = "meta";

  //~--- methods --------------------------------------------------------------

  /**
   * Creates the self uri.
   *
   * @param dtoContext dto context
   * @param resourcePath resource path
   * @param id id object
   *
   * @return self href uri
   */
  protected URI createSelfHref(DTOContext dtoContext, String resourcePath,
    Object id)
  {
    return dtoContext.getUriInfo().getBaseUriBuilder().path(resourcePath).path(
      id.toString()).build();
  }

  /**
   * Append object meta data.
   *
   * @param dtoContext dto context
   * @param resourcePath resource path
   * @param id id object
   */
  protected void putMetaObject(DTOContext dtoContext, String resourcePath,
    Object id)
  {
    //J-
    this.put(META, new Meta(
      createSelfHref(dtoContext, resourcePath, id),
      dtoContext.getMediaType()
    ));
    //J+
  }

  /**
   * Put value to map, if value is not null or is not in the context.
   *
   * @param dtoContext dto context
   * @param key name of key
   * @param value value object
   */
  protected void putNonNull(DTOContext dtoContext, String key, Object value)
  {
    if (dtoContext.isFieldInContext(key))
    {
      this.putNonNull(key, value);
    }
  }

  /**
   * Put value to map, if value is not null.
   *
   * @param key name of key
   * @param value value object
   */
  protected void putNonNull(String key, Object value)
  {
    if ((key == null) || (key.length() == 0))
    {
      throw new IllegalArgumentException("key is null");
    }

    if (value != null)
    {
      this.put(key, value);
    }
  }
}
