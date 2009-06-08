package org.seasar.aptina.commons.message;

import javax.tools.Diagnostic.Kind;

/**
 * 
 * @author koichik
 */
public enum TestMessageCode implements EnumMessageCode {
    /** */
    FOO(Kind.WARNING, "aaa", "あああ"),
    /** */
    BAR(Kind.ERROR, "%1$s of %2$s", "%2$sの%1$s");

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
    private TestMessageCode(final Kind kind, final String... messageFormats) {
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
