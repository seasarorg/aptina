/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.aptina.beans.internal;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

/**
 * 
 * @author koichik
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes("org.seasar.aptina.beans.BeanState")
public class BeansProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        final BeanInfoFactory beanMetaFactory = new BeanInfoFactory(
                processingEnv);
        final BeanClassGenerator beanClassGenerator = new BeanClassGenerator(
                processingEnv);
        for (final TypeElement annotation : annotations) {
            for (final TypeElement typeElement : ElementFilter.typesIn(roundEnv
                    .getElementsAnnotatedWith(annotation))) {
                final BeanInfo beanInfo = beanMetaFactory
                        .createBeanInfo(typeElement);
                if (beanInfo != null) {
                    try {
                        beanClassGenerator.generate(beanInfo, typeElement);
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return false;
    }

}
