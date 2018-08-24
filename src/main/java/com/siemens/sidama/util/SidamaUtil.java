package com.siemens.sidama.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author FBG
 *
 */
public final class SidamaUtil {

    /**
     * 
     * @param emailAddress
     * @return
     */
    public static boolean validateEmail(String emailAddress) {
        Pattern regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
        Matcher regMatcher = regexPattern.matcher(emailAddress);
        return regMatcher.matches();
    }
}
