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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.lang.model.element.TypeElement;

import org.seasar.aptina.beans.example.BarBeanState;
import org.seasar.aptina.beans.example.BoundAndConstrainedBeanState;
import org.seasar.aptina.beans.example.BoundBeanState;
import org.seasar.aptina.beans.example.ConstrainedBeanState;
import org.seasar.aptina.beans.example.FooBeanState;
import org.seasar.aptina.unit.AptinaTestCase;

/**
 * 
 * @author koichik
 */
public class BeanClassGeneratorTest extends AptinaTestCase {

    TypeElement typeElement;

    BeanInfoFactory beanInfoFactory;

    BeanInfo beanInfo;

    BeanClassGenerator generator;

    StringWriter stringWriter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setLocale(Locale.JAPANESE);
        setCharset(Charset.forName("UTF-8"));
        addSourcePath("aptina-beans/src/test/java", "src/test/java");
    }

    /**
     * @throws Exception
     */
    public void testPutClassHeaderFoo() throws Exception {
        createGenerator(FooBeanState.class);
        generator.putClassHeader(beanInfo);
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutClassHeaderBar() throws Exception {
        createGenerator(BarBeanState.class);
        generator.putClassHeader(beanInfo);
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutClassHeaderBound() throws Exception {
        createGenerator(BoundBeanState.class);
        generator.putClassHeader(beanInfo);
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutClassHeaderConstrained() throws Exception {
        createGenerator(ConstrainedBeanState.class);
        generator.putClassHeader(beanInfo);
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutClassHeaderBoundAndConstrained() throws Exception {
        createGenerator(BoundAndConstrainedBeanState.class);
        generator.putClassHeader(beanInfo);
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutClassFooter() throws Exception {
        createGenerator(FooBeanState.class);
        generator.putClassFooter(beanInfo);
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutConstructorFoo() throws Exception {
        createGenerator(FooBeanState.class);
        for (final ConstructorInfo constructorInfo : beanInfo.getConstructors()) {
            generator.putConstructor(beanInfo, constructorInfo);
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutConstructorBar() throws Exception {
        createGenerator(BarBeanState.class);
        for (final ConstructorInfo constructorInfo : beanInfo.getConstructors()) {
            generator.putConstructor(beanInfo, constructorInfo);
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutGetterFoo() throws Exception {
        createGenerator(FooBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isReadable()) {
                generator.putGetter(beanInfo, propertyInfo);
            }
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutGetterBar() throws Exception {
        createGenerator(BarBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isReadable()) {
                generator.putGetter(beanInfo, propertyInfo);
            }
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutSetterFoo() throws Exception {
        createGenerator(FooBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isWritable()) {
                generator.putSetter(beanInfo, propertyInfo);
            }
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutSetterBar() throws Exception {
        createGenerator(BarBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isWritable()) {
                generator.putSetter(beanInfo, propertyInfo);
            }
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutSetterBound() throws Exception {
        createGenerator(BoundBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isWritable()) {
                generator.putSetter(beanInfo, propertyInfo);
            }
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutSetterConstrained() throws Exception {
        createGenerator(ConstrainedBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isWritable()) {
                generator.putSetter(beanInfo, propertyInfo);
            }
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutSetterBoundAndConstrained() throws Exception {
        createGenerator(BoundAndConstrainedBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isWritable()) {
                generator.putSetter(beanInfo, propertyInfo);
            }
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutAddListenerBound() throws Exception {
        createGenerator(BoundBeanState.class);
        generator.putAddEventListener(beanInfo);
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutAddListenerConstrained() throws Exception {
        createGenerator(ConstrainedBeanState.class);
        generator.putAddEventListener(beanInfo);
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutAddListenerBoundAndConstrained() throws Exception {
        createGenerator(BoundAndConstrainedBeanState.class);
        generator.putAddEventListener(beanInfo);
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutAddSpecificListenerBound() throws Exception {
        createGenerator(BoundBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isWritable()) {
                generator.putAddEventListener(beanInfo, propertyInfo);
            }
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutAddSpecificListenerConstrained() throws Exception {
        createGenerator(ConstrainedBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isWritable()) {
                generator.putAddEventListener(beanInfo, propertyInfo);
            }
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testPutAddSpecificListenerBoundAndConstrained()
            throws Exception {
        createGenerator(BoundAndConstrainedBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                    .getPropertyInfo(propertyName);
            if (propertyInfo.isWritable()) {
                generator.putAddEventListener(beanInfo, propertyInfo);
            }
        }
        generator.writer.close();
        assertEqualsByLine(new StringReader(stringWriter.toString()));
    }

    /**
     * @throws Exception
     */
    public void testToQualifiedName() throws Exception {
        assertEquals(FooBeanState.class.getName(), BeanClassGenerator
                .toQualifiedName("org.seasar.aptina.beans.example",
                        "FooBeanState"));
        assertEquals("FooBeanState", BeanClassGenerator.toQualifiedName(null,
                "FooBeanState"));
    }

    /**
     * @param compilationUnit
     * @throws Exception
     */
    void createGenerator(final Class<?> compilationUnit) throws Exception {
        addCompilationUnit(compilationUnit);
        compile();

        typeElement = getTypeElement(compilationUnit);
        beanInfoFactory = new BeanInfoFactory(getProcessingEnvironment());
        beanInfo = beanInfoFactory.createBeanInfo(typeElement);

        generator = new BeanClassGenerator(getProcessingEnvironment());
        stringWriter = new StringWriter();
        generator.writer = new PrintWriter(stringWriter);
    }

    /**
     * @param reader
     * @throws Exception
     */
    void assertEqualsByLine(final Reader reader) throws Exception {
        final String expectedResourceName = getClass().getSimpleName() + "_"
                + getName() + ".txt";
        final BufferedReader expectedReader = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader()
                        .getResourceAsStream(expectedResourceName), Charset
                        .forName("UTF-8")));
        final BufferedReader actualReader = new BufferedReader(reader);
        try {
            String expected;
            String actual;
            while ((expected = expectedReader.readLine()) != null) {
                actual = actualReader.readLine();
                assertEquals(expected, actual);
            }
            assertEquals(null, actualReader.readLine());
        } finally {
            close(expectedReader);
            close(actualReader);
        }
    }

    /**
     * @param closeable
     */
    void close(final Closeable closeable) {
        try {
            closeable.close();
        } catch (final Exception ignore) {
        }
    }
}
