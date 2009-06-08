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
package org.seasar.aptina.commons.message;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import static org.seasar.aptina.commons.message.MessageCodeEnum.*;

/**
 * {@link EnumMessageResourceBundle} を作成する {@link Control} です．
 * 
 * @author koichik
 * @param <T>
 *            メッセージコードを定義した列挙の型
 */
public class EnumMessageResourceBundleControl<T extends Enum<T> & MessageCodeEnum>
        extends Control {

    /** メッセージコードを定義した列挙の型 */
    protected Class<T> messageCodeEnumClass;

    /**
     * インスタンスを構築します．
     * 
     * @param messageCodeEnumClass
     *            メッセージコードを定義した列挙の型
     */
    public EnumMessageResourceBundleControl(final Class<T> messageCodeEnumClass) {
        this.messageCodeEnumClass = messageCodeEnumClass;
    }

    @Override
    public ResourceBundle newBundle(final String baseName, final Locale locale,
            final String format, final ClassLoader loader, final boolean reload)
            throws IllegalAccessException, InstantiationException, IOException {
        if ("java.class".equals(format)) {
            for (int i = 0; i < SUPORTED_LOCALE.length; ++i) {
                if (SUPORTED_LOCALE[i].equals(locale)) {
                    return new EnumMessageResourceBundle<T>(
                            messageCodeEnumClass, i);
                }
            }
        }
        return null;
    }

}
