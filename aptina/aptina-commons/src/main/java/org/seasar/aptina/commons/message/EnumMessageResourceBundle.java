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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.seasar.aptina.commons.message.EnumMessageCode.*;

/**
 * メッセージコードを定義した列挙を使用するリソースバンドルです．
 * 
 * @author koichik
 * @param <T>
 *            メッセージコードを定義した列挙の型
 */
public class EnumMessageResourceBundle<T extends Enum<T> & EnumMessageCode>
        extends ResourceBundle {

    /** メッセージコードを定義した列挙の型 */
    protected final Class<T> enumClass;

    /** ロケール */
    protected final int locale;

    /**
     * メッセージコードを定義した列挙を使用するリソースバンドルを返します．
     * 
     * @param <T>
     *            メッセージコードを定義した列挙の型
     * @param enumClass
     *            メッセージコードを定義した列挙の型
     * @return リソースバンドル
     */
    public static <T extends Enum<T> & EnumMessageCode> ResourceBundle getBundle(
            final Class<T> enumClass) {
        return ResourceBundle.getBundle(enumClass.getName(),
                new EnumMessageResourceBundleControl<T>(enumClass));
    }

    /**
     * メッセージコードを定義した列挙を使用するリソースバンドルを返します．
     * 
     * @param <T>
     *            メッセージコードを定義した列挙の型
     * @param enumClass
     *            メッセージコードを定義した列挙の型
     * @param locale
     *            ロケール
     * @return リソースバンドル
     */
    public static <T extends Enum<T> & EnumMessageCode> ResourceBundle getBundle(
            final Class<T> enumClass, final Locale locale) {
        return ResourceBundle.getBundle(enumClass.getName(), locale,
                new EnumMessageResourceBundleControl<T>(enumClass));
    }

    /**
     * インスタンスを構築します．
     * 
     * @param enumClass
     *            メッセージコードを定義した列挙の型
     * @param locale
     *            ロケール
     */
    public EnumMessageResourceBundle(final Class<T> enumClass, final int locale) {
        this.enumClass = enumClass;
        this.locale = locale;
    }

    @Override
    public Enumeration<String> getKeys() {
        final EnumMessageCode[] codes = enumClass.getEnumConstants();
        final List<String> keys = new ArrayList<String>(codes.length);
        for (final EnumMessageCode code : codes) {
            keys.add(code.getMessageFormat(locale));
        }
        return Collections.enumeration(keys);
    }

    @Override
    protected Object handleGetObject(final String key) {
        try {
            final EnumMessageCode code = Enum.valueOf(enumClass, key);
            return code.getMessageFormat(locale);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * {@link EnumMessageResourceBundle} を作成する {@link Control} です．
     * 
     * @author koichik
     * @param <T>
     *            メッセージコードを定義した列挙の型
     */
    public static class EnumMessageResourceBundleControl<T extends Enum<T> & EnumMessageCode>
            extends Control {

        /** メッセージコードを定義した列挙の型 */
        protected final Class<T> enumClass;

        /**
         * インスタンスを構築します．
         * 
         * @param enumClass
         *            メッセージコードを定義した列挙の型
         */
        public EnumMessageResourceBundleControl(final Class<T> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public List<String> getFormats(final String baseName) {
            return FORMAT_CLASS;
        }

        @Override
        public ResourceBundle newBundle(final String baseName,
                final Locale locale, final String format,
                final ClassLoader loader, final boolean reload)
                throws IllegalAccessException, InstantiationException,
                IOException {
            for (int i = 0; i < SUPORTED_LOCALE.length; ++i) {
                if (SUPORTED_LOCALE[i].equals(locale)) {
                    return new EnumMessageResourceBundle<T>(enumClass, i);
                }
            }
            return null;
        }

    }

}
