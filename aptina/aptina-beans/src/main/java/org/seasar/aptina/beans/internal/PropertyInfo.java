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

/**
 * 生成するプロパティの情報を保持するクラスです．
 * 
 * @author koichik
 */
public class PropertyInfo {

    /** Javadoc コメント */
    protected String comment;

    /** プロパティ名 */
    protected String name;

    /** プロパティの型 */
    protected String type;

    /** プロパティが参照可能なら {@literal true} */
    protected boolean readable = true;

    /** プロパティが変更可能なら {@literal true} */
    protected boolean writable = true;

    /**
     * インスタンスを構築します．
     */
    public PropertyInfo() {
    }

    /**
     * Javadoc コメントを返します．
     * 
     * @return Javadoc コメント
     */
    public String getComment() {
        return comment;
    }

    /**
     * Javadoc コメントを設定します．
     * 
     * @param comment
     *            Javadoc コメント
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * プロパティ名を返します．
     * 
     * @return プロパティ名
     */
    public String getName() {
        return name;
    }

    /**
     * プロパティ名を設定します．
     * 
     * @param name
     *            プロパティ名
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * プロパティの型を返します．
     * 
     * @return プロパティの型
     */
    public String getType() {
        return type;
    }

    /**
     * プロパティの型を設定します．
     * 
     * @param type
     *            プロパティの型 The type to set.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * プロパティが参照可能なら {@literal true} を返します．
     * 
     * @return プロパティが参照可能なら {@literal true}
     */
    public boolean isReadable() {
        return readable;
    }

    /**
     * プロパティが参照可能なら {@literal true} を設定します．
     * 
     * @param readable
     *            プロパティが参照可能なら {@literal true}
     */
    public void setReadable(final boolean readable) {
        this.readable = readable;
    }

    /**
     * プロパティが変更可能なら {@literal true} を返します．
     * 
     * @return プロパティが変更可能なら {@literal true}
     */
    public boolean isWritable() {
        return writable;
    }

    /**
     * プロパティが変更可能なら {@literal true} を設定します．
     * 
     * @param writable
     *            プロパティが変更可能なら {@literal true}
     */
    public void setWritable(final boolean writable) {
        this.writable = writable;
    }

}
