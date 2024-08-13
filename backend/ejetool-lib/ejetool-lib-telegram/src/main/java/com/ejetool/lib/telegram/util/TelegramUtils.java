package com.ejetool.lib.telegram.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TelegramUtils {

    private static final Pattern SPECIAL_CHARACTERS_PATTERN_V1 = Pattern.compile("[*_\\[`]");
    
    public static String toSafeText(String format, Object... args){
        return toSafeText(String.format(format, args));
    }

    public static String toSafeText(String input){
        Matcher matcher = SPECIAL_CHARACTERS_PATTERN_V1.matcher(input);
        StringBuffer escapedString = new StringBuffer();

        // Process each match
        while (matcher.find()) {
            // Append the escaped version of the matched character
            matcher.appendReplacement(escapedString, "\\\\" + matcher.group());
        }
        matcher.appendTail(escapedString);

        return escapedString.toString();
    }
}
