package org.seasar.aptina.beans.internal;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * {@link MessageCode} に定義されたメッセージフォーマットを使用してメッセージを作成するクラスです．
 * 
 * @author koichik
 */
public class MessageFormatter {

    /** リソースバンドル */
    protected ResourceBundle bundle;

    /**
     * デフォルトのロケールでインスタンスを構築します．
     */
    public MessageFormatter() {
        bundle = ResourceBundle.getBundle(MessageResource.class.getName());
    }

    /**
     * 指定されたロケールでインスタンスを構築します．
     * 
     * @param locale
     *            ロケール
     */
    public MessageFormatter(final Locale locale) {
        bundle = ResourceBundle.getBundle(MessageResource.class.getName(),
                locale);
    }

    /**
     * メッセージを作成して返します．
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            引数
     * @return メッセージ
     */
    public String getMessage(final MessageCode messageCode,
            final Object... args) {
        final String pattern = bundle.getString(messageCode.name());
        return String.format(pattern, args);
    }

}
