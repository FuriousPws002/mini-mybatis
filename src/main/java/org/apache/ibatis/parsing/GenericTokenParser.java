package org.apache.ibatis.parsing;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author furious 2024/4/10
 */
public class GenericTokenParser {

    private final String openToken;
    private final String closeToken;
    private final TokenHandler handler;

    public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.handler = handler;
    }

    public String parse(String text) {
        String[] tokens = StringUtils.substringsBetween(text, openToken, closeToken);
        if (ArrayUtils.isEmpty(tokens)) {
            return text;
        }
        for (String token : tokens) {
            text = text.replace(openToken + token + closeToken, handler.handleToken(token));
        }
        return text;
    }
}
