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



package com.github.sdorra.dto.processor;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public final class CodeGeneratorModel
{

  /**
   * Constructs ...
   *
   *
   * @param packageName
   * @param className
   * @param classNameSuffix
   * @param resourcePath
   * @param idField
   * @param fields
   */
  public CodeGeneratorModel(String packageName, String className,
    String classNameSuffix, String resourcePath, DTOModelField idField,
    List<DTOModelField> fields)
  {
    this.packageName = packageName;
    this.className = className;
    this.classNameSuffix = classNameSuffix;
    this.resourcePath = resourcePath;
    this.instanceName = Character.toLowerCase(className.charAt(0))
      + className.substring(1);
    this.idField = idField;
    this.fields = fields;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getClassName()
  {
    return className;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getClassNameSuffix()
  {
    return classNameSuffix;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<DTOModelField> getFields()
  {
    return fields;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getFullDTOName()
  {
    return packageName.concat(".").concat(getSimpleDTOName());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DTOModelField getIdField()
  {
    return idField;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getInstanceName()
  {
    return instanceName;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPackageName()
  {
    return packageName;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getResourcePath()
  {
    return resourcePath;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSimpleDTOName()
  {
    return className.concat(classNameSuffix);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final String className;

  /** Field description */
  private final String classNameSuffix;

  /** Field description */
  private final List<DTOModelField> fields;

  /** Field description */
  private final DTOModelField idField;

  /** Field description */
  private final String instanceName;

  /** Field description */
  private final String packageName;

  /** Field description */
  private final String resourcePath;
}
