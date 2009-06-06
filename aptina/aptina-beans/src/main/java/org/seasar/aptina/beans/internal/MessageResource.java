package org.seasar.aptina.beans.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

/**
 * {@link MessageCode} のメッセージフォーマットを対象とするリソースバンドルです．
 * 
 * @author koichik
 */
public class MessageResource extends ResourceBundle {

    /** ロケール */
    protected int locale;

    /**
     * デフォルトロケールのインスタンスを構築します．
     */
    public MessageResource() {
        this(MessageCode.LOCALE_DEFAULT);
    }

    /**
     * 指定されたロケールのインスタンスを構築します．
     * 
     * @param locale
     */
    protected MessageResource(final int locale) {
        this.locale = locale;
    }

    @Override
    public Enumeration<String> getKeys() {
        final MessageCode[] codes = MessageCode.class.getEnumConstants();
        final List<String> keys = new ArrayList<String>(codes.length);
        for (final MessageCode messageCode : codes) {
            keys.add(messageCode.getMessageFormat(locale));
        }
        return Collections.enumeration(keys);
    }

    @Override
    protected Object handleGetObject(final String key) {
        final MessageCode m = Enum.valueOf(MessageCode.class, key);
        return m.getMessageFormat(locale);
    }

}
