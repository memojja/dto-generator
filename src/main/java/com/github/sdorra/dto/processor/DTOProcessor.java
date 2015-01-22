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

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.sdorra.dto.GenerateDTO;

import com.google.common.base.Throwables;

import org.kohsuke.MetaInfServices;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.Writer;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import javax.tools.JavaFileObject;

/**
 *
 * @author Sebastian Sdorra
 */
@MetaInfServices(Processor.class)
@SuppressWarnings({ "Since16", "Since15" })
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("*")
public class DTOProcessor extends AbstractProcessor
{

  /** Field description */
  private static final String TEMPLATE =
    "com/github/sdorra/dto/processor/mapping.mustache";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param annotations
   * @param roundEnv
   *
   * @return
   */
  @Override
  public boolean process(Set<? extends TypeElement> annotations,
    RoundEnvironment roundEnv)
  {
    DTOModelBuilder builder = new DTOModelBuilder(processingEnv);

    for (Element e : roundEnv.getElementsAnnotatedWith(GenerateDTO.class))
    {
      if (e.getKind() == ElementKind.CLASS)
      {
        TypeElement typeElement = (TypeElement) e;

        handle(builder, typeElement);
      }
    }

    return true;
  }

  /**
   * Method description
   *
   *
   * @param builder
   * @param typeElement
   */
  private void handle(DTOModelBuilder builder, TypeElement typeElement)
  {
    try
    {
      CodeGeneratorModel model = builder.process(typeElement);

      write(model);
    }
    catch (IOException ex)
    {
      throw Throwables.propagate(ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param model
   *
   * @throws IOException
   */
  private void write(CodeGeneratorModel model) throws IOException
  {
    Filer filer = processingEnv.getFiler();

    JavaFileObject jfo = filer.createSourceFile(model.getFullDTOName());

    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache mustache = mf.compile(TEMPLATE);

    Writer writer = null;

    try
    {
      writer = jfo.openWriter();
      mustache.execute(writer, model).flush();
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
    }
  }
}
