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
package org.seasar.aptina.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * プロパティとしてアクセス可能なフィールドを注釈します．
 * <p>
 * デフォルトでは， 注釈されなかったフィールドは {@link AccessType#READ_WRITE} ({@literal final}
 * フィールドは {@link AccessType#READ_ONLY}) のプロパティとして扱われます．
 * 特定のフィールドをプロパティとしてアクセスしたくない場合やアクセス型を変更したい場合は， そのフィールドにこの注釈を付与します．
 * </p>
 * 
 * @author koichik
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    /**
     * プロパティのアクセス型を指定します．
     * <p>
     * デフォルトは {@link AccessType#READ_WRITE} です． ただし，{@literal final}
     * フィールドに注釈された場合は {@link AccessType#READ_ONLY} として扱われます．
     * </p>
     * 
     * @return プロパティのアクセス型
     */
    AccessType access() default AccessType.READ_WRITE;

}
