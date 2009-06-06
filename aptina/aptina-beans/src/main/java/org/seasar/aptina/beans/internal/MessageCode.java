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

import javax.tools.Diagnostic.Kind;

/**
 * メッセージコードです．
 * 
 * @author koichik
 */
public enum MessageCode {

    /** */
    CLS0000(Kind.ERROR, "", "@BeanState アノテーションをインタフェースに付けることはできません"),
    /** */
    CLS0001(Kind.ERROR, "", "@BeanState アノテーションを列挙に付けることはできません"),
    /** */
    CLS0002(Kind.ERROR, "", "@BeanState アノテーションをアノテーションに付けることはできません"),
    /** */
    CLS0003(Kind.ERROR, "", "@BeanState アノテーションをローカルクラスに付けることはできません"),
    /** */
    CLS0004(Kind.ERROR, "", "@BeanState アノテーションをネストしたクラスに付けることはできません"),
    /** */
    CLS0005(Kind.ERROR, "", "@BeanState アノテーションを final クラスに付けることはできません"),
    /** */
    CLS0006(Kind.ERROR, "", "@BeanState アノテーションを非 public クラスに付けることはできません"),
    /** */
    FLD0000(Kind.ERROR, "", "@Property アノテーションを private フィールドに付けることはできません"),
    /** */
    FLD0001(Kind.ERROR, "", "@Property アノテーションを public フィールドに付けることはできません"),
    /** */
    FLD0002(Kind.ERROR, "", "@Property アノテーションを static フィールドに付けることはできません"),
    /** */
    FLD0003(Kind.ERROR, "", "final フィールドを WRITE_ONLY にすることはできません"),
    /** */
    CTOR0000(Kind.WARNING, "", "JavaBeans には public のデフォルトコンストラクタが必要です"),
    /** */
    CTOR0001(Kind.ERROR, "", "サブクラスから可視のコンストラクタがありません"),
    /** */
    JDOC0000(Kind.OTHER, " Return the %1$s.%n %n @return the %1$s.%n",
            " %1$sを返します。%n %n @return %1$s%n"),
    /** */
    JDOC0001(Kind.OTHER, " Set the %1$s.%n %n @param %2$n the %1$s.%n",
            " %1$sを設定します。%n %n @param %2$s %1$s%n"),
    /** */
    JDOC0002(
            Kind.OTHER,
            " Return the nth %1$s.%n %n @param n the index of the %1$s to get.%n @return the n<sup>th</sup> %1$s.%n"
                    + " @throws ArrayIndexOutOfBoundsException an index is used that is outside the current array bounds",
            " %1$sのn番目の要素を返します。%n %n @param n 返される要素のインデックス%n @return n番目の%1$s%n"
                    + " @throws ArrayIndexOutOfBoundsException インデックスが配列のサイズを超えていた場合"),
    /** */
    JDOC0003(
            Kind.OTHER,
            " Set the nth %1$s.%n %n @param n n<sup>th</sup> of the %1$s to set.%n @param %2$n %1$s%n"
                    + " @throws ArrayIndexOutOfBoundsException an index is used that is outside the current array bounds",
            " %1$sのn番目の要素を設定します。%n %n @param n 設定される要素のインデックス%n @param %2$s %1$s%n"
                    + " @throws ArrayIndexOutOfBoundsException インデックスが配列のサイズを超えていた場合"),
    //
    ;

    /** デフォルトロケール */
    protected static final int LOCALE_DEFAULT = 0;

    /** 日本語ロケール */
    protected static final int LOCALE_JA = 1;

    /** 診断の種類 */
    private final Kind kind;

    /** メッセージフォーマット */
    private final String[] messageFormats;

    /**
     * インスタンスを構築します．
     * <p>
     * 配列の要素はロケールごとのメッセージフォーマットです．
     * </p>
     * 
     * @param messageFormats
     *            メッセージフォーマットの配列
     */
    private MessageCode(final Kind kind, final String... messageFormats) {
        this.kind = kind;
        this.messageFormats = messageFormats;
    }

    /**
     * 診断の種類を返します．
     * 
     * @return 診断の種類
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * 指定されたロケールのメッセージフォーマットを返します．
     * 
     * @param locale
     *            ロケール
     * @return 指定されたロケールのメッセージフォーマット
     */
    public String getMessageFormat(final int locale) {
        return messageFormats[locale];
    }

}
