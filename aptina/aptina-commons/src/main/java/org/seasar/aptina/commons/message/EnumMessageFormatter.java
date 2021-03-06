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
package org.seasar.aptina.commons.message;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Formatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 列挙に定義された {@link Formatter} のパターンを使用してメッセージを組み立てるクラスです．
 * <p>
 * {@link MessageFormat} のパターンを使用する場合は {@link EnumMessageTextFormatter}
 * を使用してください．
 * </p>
 * <p>
 * パターンを定義した列挙は {@link EnumMessageCode} を実装していなければなりません．
 * </p>
 * <p>
 * コンストラクタ引数で {@link Appendable} を渡した場合は {@link #format(Enum, Object...)}
 * を呼び出すことで組み立てられたメッセージが出力先に追加されます． 追加された文字列は {@link #toString()}
 * を呼び出すことで取得することができます．
 * </p>
 * 
 * @author koichik
 * @param <T>
 *            パターンを定義した列挙の型
 */
public class EnumMessageFormatter<T extends Enum<T> & EnumMessageCode> {

    /** リソースバンドル */
    protected final ResourceBundle bundle;

    /** フォーマッタ */
    protected final Formatter formatter;

    /**
     * デフォルトロケールでインスタンスを構築します．
     * 
     * @param enumClass
     *            パターンを定義した列挙の型
     */
    public EnumMessageFormatter(final Class<T> enumClass) {
        this(enumClass, new NullWriter());
    }

    /**
     * デフォルトロケールでインスタンスを構築します．
     * 
     * @param enumClass
     *            パターンを定義した列挙の型
     * @param out
     *            出力先
     */
    public EnumMessageFormatter(final Class<T> enumClass, final Appendable out) {
        bundle = EnumMessageResourceBundle.getBundle(enumClass);
        formatter = new Formatter(out);
    }

    /**
     * ロケールを指定してインスタンスを構築します．
     * 
     * @param enumClass
     *            パターンを定義した列挙の型
     * @param locale
     *            ロケール
     */
    public EnumMessageFormatter(final Class<T> enumClass, final Locale locale) {
        this(enumClass, new NullWriter(), locale);
    }

    /**
     * ロケールを指定してインスタンスを構築します．
     * 
     * @param enumClass
     *            パターンを定義した列挙の型
     * @param out
     *            出力先
     * @param locale
     *            ロケール
     */
    public EnumMessageFormatter(final Class<T> enumClass, final Appendable out,
            final Locale locale) {
        bundle = EnumMessageResourceBundle.getBundle(enumClass, locale);
        formatter = new Formatter(out, locale);
    }

    /**
     * 列挙に定義されたパターンを返します．
     * 
     * @param messageCode
     *            列挙
     * @return 列挙に定義されたパターン
     */
    public String getPattern(final T messageCode) {
        return bundle.getString(messageCode.name());
    }

    /**
     * 列挙に定義されたパターンを使用してメッセージを作成して返します．
     * 
     * @param messageCode
     *            列挙
     * @param args
     *            引数
     * @return 列挙に定義されたパターンを使用して作成したメッセージ
     */
    public String getMessage(final T messageCode, final Object... args) {
        return String.format(getPattern(messageCode), args);
    }

    /**
     * 列挙に定義されたパターンを使用して作成したメッセージを出力先に追加します．
     * 
     * @param messageCode
     *            列挙
     * @param args
     *            引数
     * @return このインスタンス自身
     */
    public EnumMessageFormatter<T> format(final T messageCode,
            final Object... args) {
        formatter.format(getPattern(messageCode), args);
        return this;
    }

    /**
     * 出力先に追加された文字列を返します．
     * 
     * @return 追加された文字列
     */
    @Override
    public String toString() {
        return formatter.toString();
    }

    /**
     * 何もしない {@link Appendable} です．
     * 
     * @author koichik
     */
    static class NullWriter implements Appendable {

        @Override
        public Appendable append(final char c) throws IOException {
            return this;
        }

        @Override
        public Appendable append(final CharSequence csq, final int start,
                final int end) throws IOException {
            return this;
        }

        @Override
        public Appendable append(final CharSequence csq) throws IOException {
            return this;
        }

        @Override
        public String toString() {
            return "";
        }

    }

}
