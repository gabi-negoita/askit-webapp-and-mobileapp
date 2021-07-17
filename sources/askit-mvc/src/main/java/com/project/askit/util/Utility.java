package com.project.askit.util;

import javax.servlet.http.HttpServletResponse;

public class Utility {

    public static String capitalizeAndSeparate(String text) {
        /*
         * This function capitalizez the text and adds spaces before uppercase letter
         * Example: abcDef -> Abc def
         */
        return text.substring(0, 1).toUpperCase() + text.substring(1).replaceAll("([A-Z])", " $1").toLowerCase();
    }

    public static String formatUrl(String url) {
        /*
         * This function replaces non-alpha-numeric characters with "+"
         * Example: Abc Def Ghi -> abc+def+ghi
         */
        return url
                // Replace all non-alphanumeric characters with space
                .replaceAll("[^a-zA-Z\\d]", " ")
                // Remove head and tail spaces
                .trim()
                // Replace all spaces with separator
                .replaceAll("\\s+", "+").toLowerCase();
    }

    public static String encodeUrl(String url){
        HttpServletResponse response = null;
        return response.encodeURL(url);
    }
}
