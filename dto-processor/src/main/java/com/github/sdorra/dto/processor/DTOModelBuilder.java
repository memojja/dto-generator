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

//~--- non-JDK imports --------------------------------------------------------

import com.github.sdorra.dto.DTOField;
import com.github.sdorra.dto.GenerateDTO;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import javax.tools.Diagnostic;

/**
 *
 * @author Sebastian Sdorra
 */
public class DTOModelBuilder
{

  /** Field description */
  private static final String PACKAGE_JAVA_LANG = "java.lang.";
  
  /** Field description */
  private static final String CLASS_FQ_LIST = "java.util.List";
  
  /** Field description */
  private static final String CLASS_FQ_ARRYALIST = "java.util.ArrayList";
  
  /** Field description */
  private static final String CLASS_LIST = "List";
  
  /** Field description */
  private static final String CLASS_ARRYALIST = "ArrayList";

  /** Field description */
  private static final String PREFIX_GET = "get";

  /** Field description */
  private static final String PREFIX_SET = "set";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param processingEnv
   */
  public DTOModelBuilder(ProcessingEnvironment processingEnv)
  {
    this.processingEnv = processingEnv;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param classElement
   *
   * @return
   *
   * @throws IOException
   */
  public CodeGeneratorModel process(TypeElement classElement) throws IOException
  {
    String className = classElement.getQualifiedName().toString();
    String packageName = getPackageName(classElement);

    log("process class ".concat(className));

    List<DTOModelField> fields = Lists.newArrayList();

    DTOModelField idField = collectFields(fields, classElement);

    if (idField == null)
    {
      throw new IOException("no id field specified");
    }

    TypeMirror superClass = classElement.getSuperclass();

    while ((superClass != null) && (superClass.getKind() == TypeKind.DECLARED))
    {
      DeclaredType type = (DeclaredType) superClass;
      Element e = type.asElement();

      if (e.getKind() == ElementKind.CLASS)
      {
        TypeElement parent = (TypeElement) e;

        collectFields(fields, parent);
        superClass = parent.getSuperclass();
      }
      else
      {
        superClass = null;
      }
    }

    GenerateDTO dto = classElement.getAnnotation(GenerateDTO.class);

    //J-
    return new CodeGeneratorModel(
      packageName,
      classElement.getSimpleName().toString(),
      dto.suffix(),
      createResourcePath(dto, classElement),
      idField,
      fields
    );
    //J+
  }
  
  private String createResourcePath(GenerateDTO dto, TypeElement classElement) {
    String value = dto.value();
    if ("__default".equals(value)){
      String name = classElement.getSimpleName().toString();
      value = Character.toLowerCase(name.charAt(0)) + name.substring(1) + "s";
    }
    return value;
  }

  /**
   * Method description
   *
   *
   * @param fields
   * @param getters
   * @param setters
   */
  private void applyGetterAndSetter(List<DTOModelField> fields,
    Set<String> getters, Set<String> setters)
  {
    for (DTOModelField field : fields)
    {
      String variable = field.getVariable();
      String method = Character.toUpperCase(variable.charAt(0))
                      + variable.substring(1);
      String getter = PREFIX_GET.concat(method);
      String setter = PREFIX_SET.concat(method);

      if (getters.contains(getter))
      {
        field.setGetter(getter);
      }

      if (setters.contains(setter))
      {
        field.setSetter(setter);
      }
    }

  }

  /**
   * Method description
   *
   *
   * @param fields
   * @param classElement
   *
   * @return
   */
  private DTOModelField collectFields(List<DTOModelField> fields,
    TypeElement classElement)
  {
    DTOModelField idField = null;
    Set<String> getters = Sets.newHashSet();
    Set<String> setters = Sets.newHashSet();

    for (Element e : classElement.getEnclosedElements())
    {
      if (e.getKind() == ElementKind.FIELD)
      {
        DTOModelField field = convert((VariableElement) e);

        if (field != null)
        {
          if (field.isId())
          {
            if (idField != null)
            {
              throw new IllegalStateException("found multiple id fields");
            }

            idField = field;
          }

          fields.add(field);
        }
      }
      else if (e.getKind() == ElementKind.METHOD)
      {
        String name = e.getSimpleName().toString();

        if (name.startsWith(PREFIX_GET))
        {
          getters.add(name);
        }
        else if (name.startsWith(PREFIX_SET))
        {
          setters.add(name);
        }
      }
    }

    applyGetterAndSetter(fields, getters, setters);

    return idField;
  }

  /**
   * Method description
   *
   *
   * @param var
   *
   * @return
   */
  private String constantName(String var)
  {
    StringBuilder name = new StringBuilder("FIELD_");

    for (char c : var.toCharArray())
    {
      if (Character.isUpperCase(c))
      {
        name.append("_");
      }

      name.append(Character.toUpperCase(c));
    }

    return name.toString();
  }

  /**
   * Method description
   *
   *
   * @param field
   *
   * @return
   */
  private DTOModelField convert(VariableElement field)
  {
    DTOModelField modelField = null;

    if (!isConstant(field))
    {
      String varname = field.getSimpleName().toString();
      TypeMirror typeMirror = field.asType();
      String typeValue = type(typeMirror.toString());
      boolean id = false;
      boolean multiValue = false;

      DTOField dtoField = field.getAnnotation(DTOField.class);

      if (dtoField != null)
      {
        id = dtoField.id();
      }

      String dtoName = null;

      if (!(typeMirror instanceof PrimitiveType))
      {
        TypeElement element = (TypeElement) processingEnv.getTypeUtils().asElement(typeMirror);
        
        if (TypeElements.isMultiValue(element)){
          DeclaredType dt = (DeclaredType) typeMirror;
          
          List<? extends TypeMirror> params = dt.getTypeArguments();
          if (params.size() == 1){
            TypeMirror generic = params.get(0);
            TypeElement genericElement = (TypeElement) processingEnv.getTypeUtils().asElement(generic);
            GenerateDTO dto = genericElement.getAnnotation(GenerateDTO.class);
            dtoName = createDTOName(genericElement, dto);
            multiValue = true;
          }
        } else {
          GenerateDTO dto = element.getAnnotation(GenerateDTO.class);

          dtoName = createDTOName(element, dto);
        }
      }

      modelField = new DTOModelField(constantName(varname), varname, typeValue,
        id, dtoName, multiValue);
    }

    return modelField;
  }

  /**
   * Method description
   *
   *
   * @param el
   * @param dto
   *
   * @return
   */
  private String createDTOName(TypeElement el, GenerateDTO dto)
  {
    String name = null;

    if (dto != null)
    {
      name = el.getQualifiedName().toString().concat(dto.suffix());
    }

    return name;
  }

  /**
   * Method description
   *
   *
   * @param msg
   */
  private void log(String msg)
  {
    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
  }

  /**
   * Method description
   *
   *
   * @param type
   *
   * @return
   */
  private String type(String type)
  {
    String result = type;

    if (type.startsWith(PACKAGE_JAVA_LANG))
    {
      result = type.substring(PACKAGE_JAVA_LANG.length());
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param classElement
   *
   * @return
   */
  private String getPackageName(TypeElement classElement)
  {
    return ((PackageElement) classElement.getEnclosingElement())
      .getQualifiedName().toString();
  }

  /**
   * Method description
   *
   *
   * @param field
   *
   * @return
   */
  private boolean isConstant(VariableElement field)
  {
    Set<Modifier> modifiers = field.getModifiers();

    return modifiers.contains(Modifier.STATIC)
      && modifiers.contains(Modifier.FINAL);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final ProcessingEnvironment processingEnv;
}
