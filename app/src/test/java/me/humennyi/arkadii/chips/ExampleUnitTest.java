package me.humennyi.arkadii.chips;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final java.lang.String SPAN_SEPARATOR = ", ";
    String SEPARATOR = ", ";

    @Test
    public void addition_isCorrect() throws Exception {

        System.out.println(replaceChips("123, ", 0,"asd"));
        System.out.println(replaceChips("123, 456, 789, aasd", 0, "asd"));
        System.out.println(replaceChips("123, 456, 789, fdsfsdf", 1, "asd"));
        System.out.println(replaceChips("123, 456, 789, asdas", 2, "asd"));
        assertEquals(true, true);
    }

    private String replaceChips(String source, int position, String what) {
        String firstPart = "";
        String lastPart = "";
        int separatorCount = 0;
        int i;
        for (i = 0; i < source.length()-1; i++) {
            if (source.substring(i).startsWith(SEPARATOR)) {
                separatorCount++;
            }
            if (separatorCount == position) {
                firstPart = source.substring(0, i);
                if (!firstPart.isEmpty()){
                    firstPart += SEPARATOR;
                }
                break;
            }
        }
        for (int j = i + SEPARATOR.length(); j < source.length()-1; j++) {
            if (source.substring(j).startsWith(SEPARATOR)) {
                separatorCount++;
            }
            if (separatorCount == position + 1) {
                lastPart = source.substring(j + SEPARATOR.length());
                break;
            }
        }
        return firstPart + what + SEPARATOR + lastPart;
    }
}