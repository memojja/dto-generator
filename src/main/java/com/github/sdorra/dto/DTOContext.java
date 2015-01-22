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

//~--- non-JDK imports --------------------------------------------------------

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collection;

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
        Collections2.transform(fields, new NestedFieldsTransformer(field)),
        Collections2.transform(expandingFields,new NestedFieldsTransformer(field)), 
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
      expandingFields = ImmutableSet.of();
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
      fields = ImmutableSet.of();
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
    public Builder addExpandingFields(Iterable<String> expandingFields)
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
    public Builder addFields(Iterable<String> fields)
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
      return new DTOContext(uriInfo, mediaType, fields.build(),
        expandingFields.build(), false);
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
     * @param builder
     * @param field
     */
    private void parseAndAppend(ImmutableSet.Builder<String> builder,
      String field)
    {
      if (!Strings.isNullOrEmpty(field))
      {
        builder.addAll(Splitter.on(',').trimResults().split(field));
      }
    }

    /**
     * Method description
     *
     *
     * @param builder
     * @param request
     * @param key
     */
    private void parseAndAppendParameter(ImmutableSet.Builder<String> builder,
      HttpServletRequest request, String key)
    {
      String[] values = request.getParameterValues(key);

      if (values != null)
      {
        for (String value : values)
        {
          parseAndAppend(builder, value);
        }
      }
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private final ImmutableSet.Builder<String> fields = ImmutableSet.builder();

    /** Field description */
    private final ImmutableSet.Builder<String> expandingFields =
      ImmutableSet.builder();

    /** Field description */
    private final UriInfo uriInfo;

    /** Field description */
    private String mediaType;
  }


  /**
   * Class description
   *
   *
   * @version        Enter version here..., 15/01/22
   * @author         Enter your name here...
   */
  private static class NestedFieldsTransformer
    implements Function<String, String>
  {

    /**
     * Constructs ...
     *
     *
     * @param field
     */
    public NestedFieldsTransformer(String field)
    {
      this.prefix = field.concat(SEPARATOR_FIELD);
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param input
     *
     * @return
     */
    @Override
    public String apply(String input)
    {
      String result = null;

      if (input.startsWith(prefix))
      {
        result = input.substring(prefix.length());
      }

      return result;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private final String prefix;
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
