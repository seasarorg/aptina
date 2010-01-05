/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.aptina.unit;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import javax.tools.Diagnostic.Kind;

/**
 * 
 * @author koichik
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.seasar.aptina.unit.Hoge")
public class TestProcessor extends AbstractProcessor {

    boolean called;

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        if (called) {
            return false;
        }
        called = true;
        processingEnv.getMessager().printMessage(Kind.OTHER, "hoge");
        try {
            final Filer filer = processingEnv.getFiler();
            final JavaFileObject file = filer.createSourceFile(
                "foo.bar.Baz",
                null);
            final PrintWriter writer = new PrintWriter(file.openWriter());
            writer.print("package foo.bar;");
            writer.print("public class Baz {");
            writer.print("}");
            writer.close();
        } catch (final IOException ignore) {
        }
        return false;
    }

}
