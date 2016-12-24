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

    @Test
    public void addition_isCorrect() throws Exception {

        System.out.println(removeChips("123, ", 0));
        System.out.println(removeChips("123, 456, 789, ", 0));
        System.out.println(removeChips("123, 456, 789, ", 1));
        System.out.println(removeChips("123, 456, 789, ", 2));
        assertEquals(true, true);
    }


    private String removeChips(String source, int position) {
        String[] split = source.split(SPAN_SEPARATOR);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i != position) {
                builder.append(split[i] + SPAN_SEPARATOR);
            }
        }
        return builder.toString();
    }
}