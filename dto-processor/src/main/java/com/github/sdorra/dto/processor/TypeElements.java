/**
 * The MIT License
 *
 * Copyright (c) 2015, Sebastian Sdorra
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

import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 *
 * @author Sebastian Sdorra
 */
public class TypeElements
{

  private TypeElements()
  {
  }

  public static boolean isAssignableFrom(final TypeElement typeElement, final Class<?> type)
  {
    if (type.getName().equals(typeElement.getQualifiedName().toString()))
    {
      return true;
    }
    for (Class<?> intf : type.getInterfaces())
    {
      if (isAssignableFrom(typeElement, intf))
      {
        return true;
      }
    }
    return (type.getSuperclass() != null && isAssignableFrom(typeElement, type.getSuperclass()));
  }

  public static boolean isAssignableFrom(final TypeMirror typeMirror, final Class<?> type)
  {
    if (typeMirror instanceof DeclaredType)
    {
      final DeclaredType dclType = (DeclaredType) typeMirror;
      final TypeElement typeElement = (TypeElement) dclType.asElement();
      return isAssignableFrom(typeElement, type);
    }
    for (Class<?> intf : type.getInterfaces())
    {
      if (isAssignableFrom(typeMirror, intf))
      {
        return true;
      }
    }
    return (type.getSuperclass() != null && isAssignableFrom(typeMirror, type.getSuperclass()));
  }

  public static boolean isAssignableFrom(final Class<?> type, final TypeElement typeElement)
  {
    if (type.getName().equals(typeElement.getQualifiedName().toString()))
    {
      return true;
    }
    final List<? extends TypeMirror> types = typeElement.getInterfaces();
    for (TypeMirror typeMirror : types)
    {
      if (isAssignableFrom(type, typeMirror))
      {
        return true;
      }
    }
    return isAssignableFrom(type, typeElement.getSuperclass());
  }

  public static boolean isAssignableFrom(final Class<?> type, final TypeMirror typeMirror)
  {
    if (typeMirror instanceof DeclaredType)
    {
      final DeclaredType dclType = (DeclaredType) typeMirror;
      final TypeElement typeElement = (TypeElement) dclType.asElement();
      return isAssignableFrom(type, typeElement);
    }
    return false;
  }
  
  public static boolean isMultiValue(TypeElement el){
    return isAssignableFrom(Iterable.class, el);
  }

}
