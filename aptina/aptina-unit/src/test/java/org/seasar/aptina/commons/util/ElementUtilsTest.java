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
package org.seasar.aptina.commons.util;

import java.nio.charset.Charset;
import java.util.Locale;

import javax.lang.model.element.TypeElement;

import org.seasar.aptina.unit.AptinaTestCase;

import static org.seasar.aptina.commons.util.ElementUtils.*;

/**
 * 
 * @author koichik
 */
public class ElementUtilsTest extends AptinaTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setLocale(Locale.JAPANESE);
        setCharset(Charset.forName("UTF-8"));
        addSourcePath("aptina-unit/src/test/java", "src/test/java");
    }

    /**
     * @throws Exception
     */
    public void testToStringOfTypeParameterDecl() throws Exception {
        addCompilationUnit(Foo.class);
        addCompilationUnit(Bar.class);
        compile();

        TypeElement typeElement = getTypeElement(Foo.class);
        assertEquals("", toStringOfTypeParameterDecl(typeElement
                .getTypeParameters()));

        typeElement = getTypeElement(Bar.class);
        assertEquals("<E, T extends java.util.List<E> & java.io.Serializable>",
                toStringOfTypeParameterDecl(typeElement.getTypeParameters()));
    }

    /**
     * @throws Exception
     */
    public void testToStringOfTypeParameterNames() throws Exception {
        addCompilationUnit(Foo.class);
        addCompilationUnit(Bar.class);
        compile();

        TypeElement typeElement = getTypeElement(Foo.class);
        assertEquals("", toStringOfTypeParameterNames(typeElement
                .getTypeParameters()));

        typeElement = getTypeElement(Bar.class);
        assertEquals("<E, T>", toStringOfTypeParameterNames(typeElement
                .getTypeParameters()));
    }

}
