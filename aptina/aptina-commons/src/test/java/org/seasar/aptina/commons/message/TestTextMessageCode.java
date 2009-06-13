package org.seasar.aptina.commons.message;

import java.util.Locale;

import javax.tools.Diagnostic.Kind;

/**
 * 
 * @author koichik
 */
public enum TestTextMessageCode implements EnumMessageCode {
    /** */
    FOO(Kind.WARNING, "aaa", "あああ"),
    /** */
    BAR(Kind.ERROR, "{0} of {1}", "{1}の{0}");

    /** サポートするロケールの配列 */
    public static final Locale[] SUPPORTED_LOCALES = new Locale[] {
            Locale.ROOT, Locale.JAPANESE };

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
    private TestTextMessageCode(final Kind kind, final String... messageFormats) {
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
