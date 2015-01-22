/*
 * The MIT License
 *
 * Copyright 2015 Sebastian Sdorra.
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Sebastian Sdorra
 */
public final class DTOContext
{

  /** Field description */
  private static final String PARAMETER_EXPAND = "expand";

  /** Field description */
  private static final String PARAMETER_FIELDS = "fields";

  /** Field description */
  private static final String SEPARATOR_FIELD = ".";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param uriInfo
   * @param mediaType
   * @param fields
   * @param expandingFields
   * @param nested
   */
  private DTOContext(UriInfo uriInfo, String mediaType,
    Collection<String> fields, Collection<String> expandingFields,
    boolean nested)
  {
    this.uriInfo = uriInfo;
    this.mediaType = mediaType;
    this.fields = fields;
    this.expandingFields = expandingFields;
    this.nested = nested;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param uriInfo
   *
   * @return
   */
  public static Builder builder(UriInfo uriInfo)
  {
    return new Builder(uriInfo);
  }

  /**
   * Method description
   *
   *
   *
   * @param field
   * @return
   */
  public DTOContext nested(String field)
  {

    // TODO change fields and expanding
    DTOContext ctx;

    if (isExpandingField(field))
    {

      // what about deep nested fields ??
      ctx = new DTOContext(uriInfo, mediaType, null, expandingFields, false);
    }
    else
    {
      //J-
      ctx = new DTOContext(
        uriInfo, 
        mediaType,
        createNestedFieldCollection(fields, field),
        createNestedFieldCollection(expandingFields, field),
        true
      );
      //J+
    }

    return ctx;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Collection<String> getExpandingFields()
  {
    if (expandingFields == null)
    {
      expandingFields = Collections.emptySet();
    }

    return expandingFields;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Collection<String> getFields()
  {
    if (fields == null)
    {
      fields = Collections.emptySet();
    }

    return fields;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMediaType()
  {
    return mediaType;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public UriInfo getUriInfo()
  {
    return uriInfo;
  }

  /**
   * Method description
   *
   *
   * @param field
   *
   * @return
   */
  public boolean isExpandingField(String field)
  {
    return getExpandingFields().contains(field);
  }

  /**
   * Method description
   *
   *
   * @param field
   *
   * @return
   */
  public boolean isFieldInContext(String field)
  {
    Collection<String> f = getFields();

    return isExpandingField(field)
      || ((nested && (f.contains(field)))
        || (!nested && (f.isEmpty() || f.contains(field))));
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param collection
   * @param field
   *
   * @return
   */
  private Collection<String> createNestedFieldCollection(
    Collection<String> collection, String field)
  {
    Set<String> set = new HashSet<String>();
    String prefix = field.concat(SEPARATOR_FIELD);

    for (String f : collection)
    {

      if (f.startsWith(prefix))
      {
        set.add(f.substring(prefix.length()));
      }

    }

    return set;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 15/01/22
   * @author         Enter your name here...
   */
  public static class Builder
  {

    /**
     * Constructs ...
     *
     *
     * @param uriInfo
     */
    private Builder(UriInfo uriInfo)
    {
      this.uriInfo = uriInfo;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param expandingField
     * @param expandingFields
     *
     * @return
     */
    public Builder addExpandingFields(String expandingField,
      String... expandingFields)
    {
      parseAndAppend(this.expandingFields, expandingField);

      for (String ef : expandingFields)
      {
        this.parseAndAppend(this.expandingFields, ef);
      }

      return this;
    }

    /**
     * Method description
     *
     *
     * @param expandingFields
     *
     * @return
     */
    public Builder addExpandingFields(Collection<String> expandingFields)
    {
      for (String ef : expandingFields)
      {
        this.parseAndAppend(this.expandingFields, ef);
      }

      return this;
    }

    /**
     * Method description
     *
     *
     * @param field
     * @param fields
     *
     * @return
     */
    public Builder addFields(String field, String... fields)
    {
      parseAndAppend(this.fields, field);

      for (String f : fields)
      {
        parseAndAppend(this.fields, f);
      }

      return this;
    }

    /**
     * Method description
     *
     *
     * @param fields
     *
     * @return
     */
    public Builder addFields(Collection<String> fields)
    {
      for (String f : fields)
      {
        parseAndAppend(this.fields, f);
      }

      return this;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public DTOContext build()
    {
      return new DTOContext(uriInfo, mediaType,
        Collections.unmodifiableSet(fields),
        Collections.unmodifiableSet(expandingFields), false);
    }

    /**
     * Method description
     *
     *
     * @param request
     *
     * @return
     */
    public Builder fromRequest(HttpServletRequest request)
    {
      parseAndAppendParameter(fields, request, PARAMETER_FIELDS);
      parseAndAppendParameter(expandingFields, request, PARAMETER_EXPAND);

      return this;
    }

    /**
     * Method description
     *
     *
     * @param mediaType
     *
     * @return
     */
    public Builder mediaType(String mediaType)
    {
      this.mediaType = mediaType;

      return this;
    }

    /**
     * Method description
     *
     *
     * @param mediaType
     *
     * @return
     */
    public Builder mediaType(MediaType mediaType)
    {
      this.mediaType = mediaType.toString();

      return this;
    }

    /**
     * Method description
     *
     *
     * @param collection
     * @param fields
     */
    private void parseAndAppend(Collection<String> collection, String fields)
    {
      if (fields != null)
      {
        for (String field : fields.split(","))
        {
          field = field.trim();

          if (field.length() > 0)
          {
            collection.add(field);
          }
        }
      }
    }

    /**
     * Method description
     *
     *
     * @param collection
     * @param request
     * @param key
     */
    private void parseAndAppendParameter(Collection<String> collection,
      HttpServletRequest request, String key)
    {
      String[] values = request.getParameterValues(key);

      if (values != null)
      {
        for (String value : values)
        {
          parseAndAppend(collection, value);
        }
      }
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private final Set<String> fields = new HashSet<String>();

    /** Field description */
    private final Set<String> expandingFields = new HashSet<String>();

    /** Field description */
    private final UriInfo uriInfo;

    /** Field description */
    private String mediaType;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final String mediaType;

  /** Field description */
  private final boolean nested;

  /** Field description */
  private final UriInfo uriInfo;

  /** Field description */
  private Collection<String> expandingFields;

  /** Field description */
  private Collection<String> fields;
}
