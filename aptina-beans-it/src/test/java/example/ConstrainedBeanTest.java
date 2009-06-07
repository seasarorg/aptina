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
package example;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import junit.framework.TestCase;

/**
 * 
 * @author koichik
 */
public class ConstrainedBeanTest extends TestCase {

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        final ConstrainedBean bean = new ConstrainedBean();
        bean.addVetoableChangeListener(new VetoableChangeListener() {

            @Override
            public void vetoableChange(final PropertyChangeEvent evt)
                    throws PropertyVetoException {
                if (evt.getNewValue() == null) {
                    throw new PropertyVetoException("null", evt);
                }
            }

        });
        bean.setBbb("hoge");
        try {
            bean.setBbb(null);
            fail();
        } catch (final PropertyVetoException expected) {
        }
    }

}
