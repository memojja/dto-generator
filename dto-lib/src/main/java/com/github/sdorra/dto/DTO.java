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
import java.util.Map;

/**
 * Base class for generated DTO's.
 *
 * @author Sebastian Sdorra
 * @param <E> entity type
 */
public abstract class DTO<E> extends LinkedHashMap<String, Object>
{

  /** self link */
  public static final String LINK_SELF = "self";
  
  /** next link */
  public static final String LINK_NEXT = "next";
  
  /** prev link */
  public static final String LINK_PREV = "prev";

  /** link element */
  private static final String LINKS = "_links";

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
   * Append self link.
   *
   * @param dtoContext dto context
   * @param resourcePath resource path
   * @param id id object
   */
  protected void putSelfLink(DTOContext dtoContext, String resourcePath, Object id){
    putLink(LINK_SELF, createSelfHref(dtoContext, resourcePath, id));
  }

  /**
   * Append link.
   *
   * @param name name of link
   * @param uri uri
   * 
   * @return created link
   */
  public Link putLink(String name, String uri)
  {
    Link link = new Link(uri);
    putLink(name, link);
    return link;
  }
  
  /**
   * Append link.
   *
   * @param name name of link
   * @param uri uri
   * 
   * @return created link
   */
  public Link putLink(String name, URI uri)
  {
    Link link = new Link(uri.toString());
    putLink(name, link);
    return link;
  }
  
  /**
   * Append link.
   * 
   * @param name name of link
   * @param link link
   */
  public void putLink(String name, Link link){
    Map<String,Link> links = (Map<String,Link>) get(LINKS);
    if (links == null){
      links = new LinkedHashMap<String, Link>();
      put(LINKS, links);
    }
    links.put(name, link);
  }

  /**
   * Put value to map, if value is not null or is not in the context.
   *
   * @param dtoContext dto context
   * @param key name of key
   * @param value value object
   */
  public void putNonNull(DTOContext dtoContext, String key, Object value)
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
  public void putNonNull(String key, Object value)
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
  
   /**
   * Converts the dto back to a entity.
   *
   * @return entity
   **/
  public abstract E toEntity();
  
  /**
   * Creates entity from dto.
   *
   * @param entity
   **/
  public abstract void mergeWithEntity(E entity);
}
