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

/**
 *
 * @author Sebastian Sdorra
 */
public final class DTOModelField
{

  /**
   * Constructs ...
   *
   *
   * @param constant
   * @param variable
   * @param type
   * @param id
   * @param dto
   */
  public DTOModelField(String constant, String variable, String type,
    boolean id, String dto)
  {
    this.constant = constant;
    this.variable = variable;
    this.type = type;
    this.id = id;
    this.dto = dto;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getConstant()
  {
    return constant;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDto()
  {
    return dto;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getGetter()
  {
    return getter;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSetter()
  {
    return setter;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getType()
  {
    return type;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getVariable()
  {
    return variable;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isId()
  {
    return id;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param getter
   */
  public void setGetter(String getter)
  {
    this.getter = getter;
  }

  /**
   * Method description
   *
   *
   * @param setter
   */
  public void setSetter(String setter)
  {
    this.setter = setter;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final String constant;

  /** Field description */
  private final String dto;

  /** Field description */
  private final boolean id;

  /** Field description */
  private final String type;

  /** Field description */
  private final String variable;

  /** Field description */
  private String getter;

  /** Field description */
  private String setter;
}
