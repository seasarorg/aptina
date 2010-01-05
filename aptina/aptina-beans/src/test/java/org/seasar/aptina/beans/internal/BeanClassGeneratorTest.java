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
package org.seasar.aptina.beans.internal;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.lang.model.element.TypeElement;

import org.seasar.aptina.beans.example.BarBeanState;
import org.seasar.aptina.beans.example.BoundAndConstrainedBeanState;
import org.seasar.aptina.beans.example.BoundBeanState;
import org.seasar.aptina.beans.example.ConstrainedBeanState;
import org.seasar.aptina.beans.example.FooBeanState;
import org.seasar.aptina.unit.AptinaTestCase;

import static org.seasar.aptina.commons.util.IOUtils.*;

/**
 * 
 * @author koichik
 */
public class BeanClassGeneratorTest extends AptinaTestCase {

    TypeElement typeElement;

    BeanInfoFactory beanInfoFactory;

    BeanInfo beanInfo;

    BeanClassGenerator generator;

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
        generator.putFields(beanInfo);
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutClassHeaderBar() throws Exception {
        createGenerator(BarBeanState.class);
        generator.putClassHeader(beanInfo);
        generator.putFields(beanInfo);
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutClassHeaderBound() throws Exception {
        createGenerator(BoundBeanState.class);
        generator.putClassHeader(beanInfo);
        generator.putFields(beanInfo);
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutClassHeaderConstrained() throws Exception {
        createGenerator(ConstrainedBeanState.class);
        generator.putClassHeader(beanInfo);
        generator.putFields(beanInfo);
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutClassHeaderBoundAndConstrained() throws Exception {
        createGenerator(BoundAndConstrainedBeanState.class);
        generator.putClassHeader(beanInfo);
        generator.putFields(beanInfo);
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutClassFooter() throws Exception {
        createGenerator(FooBeanState.class);
        generator.putClassFooter(beanInfo);
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutConstructorFoo() throws Exception {
        createGenerator(FooBeanState.class);
        for (final ConstructorInfo constructorInfo : beanInfo.getConstructors()) {
            generator.putConstructor(beanInfo, constructorInfo);
        }
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutConstructorBar() throws Exception {
        createGenerator(BarBeanState.class);
        for (final ConstructorInfo constructorInfo : beanInfo.getConstructors()) {
            generator.putConstructor(beanInfo, constructorInfo);
        }
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutGetterFoo() throws Exception {
        createGenerator(FooBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                .getPropertyInfo(propertyName);
            generator.putGetter(beanInfo, propertyInfo);
        }
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutGetterBar() throws Exception {
        createGenerator(BarBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                .getPropertyInfo(propertyName);
            generator.putGetter(beanInfo, propertyInfo);
        }
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutSetterFoo() throws Exception {
        createGenerator(FooBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                .getPropertyInfo(propertyName);
            generator.putSetter(beanInfo, propertyInfo);
        }
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutSetterBar() throws Exception {
        createGenerator(BarBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                .getPropertyInfo(propertyName);
            generator.putSetter(beanInfo, propertyInfo);
        }
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutSetterBound() throws Exception {
        createGenerator(BoundBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                .getPropertyInfo(propertyName);
            generator.putSetter(beanInfo, propertyInfo);
        }
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutSetterConstrained() throws Exception {
        createGenerator(ConstrainedBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                .getPropertyInfo(propertyName);
            generator.putSetter(beanInfo, propertyInfo);
        }
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutSetterBoundAndConstrained() throws Exception {
        createGenerator(BoundAndConstrainedBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                .getPropertyInfo(propertyName);
            generator.putSetter(beanInfo, propertyInfo);
        }
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutListenerBound() throws Exception {
        createGenerator(BoundBeanState.class);
        generator.putEventListener(beanInfo);
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutListenerConstrained() throws Exception {
        createGenerator(ConstrainedBeanState.class);
        generator.putEventListener(beanInfo);
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutListenerBoundAndConstrained() throws Exception {
        createGenerator(BoundAndConstrainedBeanState.class);
        generator.putEventListener(beanInfo);
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutSpecificListenerBound() throws Exception {
        createGenerator(BoundBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                .getPropertyInfo(propertyName);
            generator.putSpecificEventListener(beanInfo, propertyInfo);
        }
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutSpecificListenerConstrained() throws Exception {
        createGenerator(ConstrainedBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                .getPropertyInfo(propertyName);
            generator.putSpecificEventListener(beanInfo, propertyInfo);
        }
        assertEqualsByLine();
    }

    /**
     * @throws Exception
     */
    public void testPutSpecificListenerBoundAndConstrained() throws Exception {
        createGenerator(BoundAndConstrainedBeanState.class);
        for (final String propertyName : beanInfo.getPropertyNames()) {
            final PropertyInfo propertyInfo = beanInfo
                .getPropertyInfo(propertyName);
            generator.putSpecificEventListener(beanInfo, propertyInfo);
        }
        assertEqualsByLine();
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
    }

    /**
     * @param actualReader
     * @throws Exception
     */
    void assertEqualsByLine() throws Exception {
        final String expectedResourceName = getClass().getSimpleName() + "/"
                + getName() + ".txt";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(
            expectedResourceName);
        try {
            assertEqualsByLine(readString(is, "UTF-8"), generator.toString());
        } finally {
            closeSilently(is);
        }
    }

}
