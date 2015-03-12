package org.elasticsearch.index.analysis;

/**
 * Created by udiabon on 15/3/12.
 */
public class DTMFEncoder {
    private DTMFEncoder() {
    }

    public static String encode(CharSequence letters) {
        if (letters == null)
            return null;
        if (letters.length() == 0)
            return "";
        StringBuilder sb = new StringBuilder(letters.length());
        int len = letters.length();
        for (int i = 0; i < len; i++) {
            char ch = letters.charAt(i);
            if (ch >= 'A' && ch <= 'Z')
                ch += 32; // convert to lowercase
            if (ch >= 'a' && ch <= 'z') {
                if (ch <= 'o')
                    sb.append(2 + (ch - 'a') / 3);
                else if (ch <= 's')
                    sb.append('7');
                else if (ch <= 'v')
                    sb.append('8');
                else
                    sb.append('9');
            }
        }
        return sb.toString();
    }
}
