package net.petrikainulainen.spring.social.signinmvc;

/**
 * @author Petri Kainulainen
 */
public class TestUtil {

    private static final String CHARACTER = "a";

    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }
}