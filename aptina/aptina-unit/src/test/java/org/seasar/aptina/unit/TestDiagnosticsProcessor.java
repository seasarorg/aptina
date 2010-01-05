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

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

/**
 * 
 * @author koichik
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.seasar.aptina.unit.Hoge")
public class TestDiagnosticsProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }
        final Messager messager = processingEnv.getMessager();
        messager.printMessage(Kind.NOTE, "hoge");
        for (final TypeElement annotation : annotations) {
            for (final TypeElement typeElement : ElementFilter.typesIn(roundEnv
                .getElementsAnnotatedWith(annotation))) {
                messager.printMessage(Kind.ERROR, "foo", typeElement);
                messager.printMessage(Kind.WARNING, "bar", typeElement);
            }
        }
        return false;
    }

}
