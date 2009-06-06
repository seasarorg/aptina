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

import java.util.Iterator;

/**
 * 文字列操作用のユーティリティです．
 * 
 * @author koichik
 */
public class Strings {

    /**
     * シーケンスの要素をセパレータで区切って連結した文字列を返します．
     * <p>
     * <code>join(asList("a", "b", "c"), ", ")</code> は <code>"a, b, c"</code>
     * を返します．
     * </p>
     * 
     * @param elements
     *            文字列のシーケンス
     * @param separator
     *            文字列を区切るセパレータ
     * @return シーケンスの要素をセパレータで区切って連結した文字列
     */
    public static String join(final Iterable<? extends CharSequence> elements,
            final String separator) {
        final StringBuilder buf = new StringBuilder();
        for (final CharSequence element : elements) {
            buf.append(element).append(separator);
        }
        if (buf.length() > 0) {
            buf.setLength(buf.length() - separator.length());
        }
        return new String(buf);
    }

    /**
     * 同じ長さの二つのシーケンスのそれぞれの対応する要素をセパレータ1で区切って連結し， それらをセパレータ2で区切って連結した文字列を返します．
     * <p>
     * <code>join(asList("a", "b", "c"), ":", asList("1", "2", "3"), ", ")</code>
     * は <code>"a:1, b:2, c:3"</code> を返します．
     * </p>
     * 
     * @param elements1
     *            シーケンス1
     * @param separator1
     *            二つのシーケンスのそれぞれの対応する要素を区切るセパレータ
     * @param elements2
     *            シーケンス2
     * @param separator2
     *            二つのシーケンスのそれぞれの対応する要素を連結した文字列を区切るセパレータ
     * @return 同じ長さの二つのシーケンスのそれぞれの対応する要素をセパレータ1で区切って連結し， それらをセパレータ2で区切って連結した文字列
     */
    public static String join(final Iterable<? extends CharSequence> elements1,
            final String separator1,
            final Iterable<? extends CharSequence> elements2,
            final String separator2) {
        final StringBuilder buf = new StringBuilder();
        final Iterator<? extends CharSequence> it = elements2.iterator();
        for (final CharSequence element1 : elements1) {
            buf.append(element1).append(separator1).append(it.next()).append(
                    separator2);
        }
        if (buf.length() > 0) {
            buf.setLength(buf.length() - separator2.length());
        }
        return new String(buf);
    }

    /**
     * 文字列の先頭文字を大文字化した文字列を返します．
     * 
     * @param s
     *            文字列
     * @return 文字列の先頭文字を大文字化した文字列
     */
    public static String capitalize(final String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * 文字列の先頭文字を小文字化した文字列を返します．
     * <p>
     * JavaBeans の仕様に従い， 先頭の2文字がともに大文字の場合はそのまま返します．
     * </p>
     * 
     * @param s
     *            文字列
     * @return 文字列の先頭文字を小文字化した文字列
     */
    public static String decapitalize(final String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        if (s.length() == 1) {
            return s.toLowerCase();
        }
        if (Character.isUpperCase(s.charAt(0))
                && Character.isUpperCase(s.charAt(1))) {
            return s;
        }
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

}
