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

import org.seasar.aptina.commons.message.MessageCodeEnum;

/**
 * メッセージコードです．
 * 
 * @author koichik
 */
public enum MessageCode implements MessageCodeEnum {

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
    JDOC0001(
            Kind.OTHER,
            " Return the nth %1$s.%n %n"
                    + " @param n the index of the %1$s to get.%n"
                    + " @return the n<sup>th</sup> %1$s.%n"
                    + " @throws ArrayIndexOutOfBoundsException an index is used that is outside the current array bounds%n",
            " %1$sのn番目の要素を返します。%n %n"
                    + " @param n 返される要素のインデックス%n"
                    + " @return n番目の%1$s%n"
                    + " @throws ArrayIndexOutOfBoundsException インデックスが配列のサイズを超えていた場合%n"),
    /** */
    JDOC0002(Kind.OTHER, " Set the %1$s.%n %n @param %2$n the %1$s.%n",
            " %1$sを設定します。%n %n @param %2$s %1$s%n"),
    /** */
    JDOC0003(
            Kind.OTHER,
            " Set the nth %1$s.%n %n"
                    + " @param n n<sup>th</sup> of the %1$s to set.%n"
                    + " @param %2$n %1$s%n"
                    + " @throws ArrayIndexOutOfBoundsException an index is used that is outside the current array bounds%n",
            " %1$sのn番目の要素を設定します。%n %n"
                    + " @param n 設定される要素のインデックス%n"
                    + " @param %2$s %1$s%n"
                    + " @throws ArrayIndexOutOfBoundsException インデックスが配列のサイズを超えていた場合%n"),
    /** */
    JDOC0004(
            Kind.OTHER,
            " Set the %1$s.%n %n @param %2$n the %1$s.%n"
                    + " @throws java.beans.PropertyVetoException if the recipient wishes the property change to be rolled back.%n",
            " %1$sを設定します。%n %n"
                    + " @param %2$s %1$s%n"
                    + " @throws java.beans.PropertyVetoException プロパティの変更が拒否された場合%n"),
    /** */
    JDOC0005(
            Kind.OTHER,
            " Set the nth %1$s.%n %n"
                    + " @param n n<sup>th</sup> of the %1$s to set.%n"
                    + " @param %2$n %1$s%n"
                    + " @throws ArrayIndexOutOfBoundsException an index is used that is outside the current array bounds.%n"
                    + " @throws java.beans.PropertyVetoException if the recipient wishes the property change to be rolled back.%n",
            " %1$sのn番目の要素を設定します。%n %n"
                    + " @param n 設定される要素のインデックス%n"
                    + " @param %2$s %1$s%n"
                    + " @throws ArrayIndexOutOfBoundsException インデックスが配列のサイズを超えていた場合%n"
                    + " @throws java.beans.PropertyVetoException プロパティの変更が拒否された場合%n"),
    /** */
    JDOC0006(Kind.OTHER, " Add a {@link %1$s} to the listener list.%n %n"
            + " @param listener The {@literal %1$s} to be added%n",
            " {@link %1$s} をリスナーリストに追加します。%n %n"
                    + " @param listener 追加する {@link %1$s}%n"),
    /** */
    JDOC0007(Kind.OTHER, " Add a {@link %1$s} for a specific property.%n %n"
            + " @param propertyName The name of the property to listen on.%n"
            + " @param listener The {@link %1$s} to be added%n",
            " 特定のプロパティーの {@link %1$s} をリスナーリストに追加します。%n %n"
                    + " @param propertyName 待機しているプロパティーの名前%n"
                    + " @param listener 追加する {@link %1$s}%n"),
    /** */
    JDOC0008(Kind.OTHER, " Remove a {@link %1$s} from the listener list.%n %n"
            + " @param listener The {@link %1$s} to be removed%n",
            " {@link %1$s} をリスナーリストから削除します。%n %n"
                    + " @param listener 削除する {@link %1$s}%n"),
    /** */
    JDOC0009(
            Kind.OTHER,
            " Remove a {@link %1$s} for a specific property.%n %n"
                    + " @param propertyName The name of the property that was listened on.%n"
                    + " @param listener The {@link %1$s} to be removed%n",
            " 特定のプロパティーの {@link %1$s} をリスナーリストから削除します。%n %n"
                    + " @param propertyName 待機していたプロパティーの名前%n"
                    + " @param listener 削除する {@link %1$s}%n"),
    /** */
    JDOC0010(Kind.OTHER, " Add a {@link %1$s} for the %2$s.%n %n"
            + " @param listener The {@link %1$s} to be added%n",
            " %2$sの {@link %1$s} をリスナーリストに追加します。%n %n"
                    + " @param listener 追加する {@link %1$s}%n"),
    /** */
    JDOC0011(Kind.OTHER, " Remove a {@link %1$s} for the %2$s.%n %n"
            + " @param listener The {@link %1$s} to be removed%n",
            " %2$sの {@link %1$s} をリスナーリストから削除します。%n %n"
                    + " @param listener 削除する {@link %1$s}%n"),
    /** */
    APT0000(Kind.ERROR, "", "注釈処理中に例外が発生しました．%1$s%n"),
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
