package bluemonster.simplyrandom.util;

import scala.actors.threadpool.Arrays;

public class ArrayUtils {

    public static <T> T[] fill(T[] tarr, T fillValue) {
        Arrays.fill(tarr, fillValue);
        return tarr;
    }
}
