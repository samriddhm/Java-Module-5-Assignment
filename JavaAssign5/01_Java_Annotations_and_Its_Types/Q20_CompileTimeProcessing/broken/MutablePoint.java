/**
 * Q20: FAILS the @Immutable check on purpose - field 'y' is not final.
 * Compiling this with the processor stops the build with an error.
 */
@Immutable
public class MutablePoint {
    private final int x;
    private int y;          // <-- not final: the processor reports an ERROR here

    public MutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
