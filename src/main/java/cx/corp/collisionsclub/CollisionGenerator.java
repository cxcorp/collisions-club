package cx.corp.collisionsclub;

import java.util.Objects;
import java.util.Random;

/***
 * Generates Java strings whose hashcode is the same.&nbsp;Currently supports four-character strings.
 */
public class CollisionGenerator {

    private static final int MAX_CHARS = 4;
    private static final int STRING_HASHCODE_MULTIPLIER = 31;
    private static final int UTF16_CODE_UNIT_MAX_VALUE = 0xFFFF;
    private static final int COLLISION_MAX_MULTIPLIER = UTF16_CODE_UNIT_MAX_VALUE / STRING_HASHCODE_MULTIPLIER;

    private final char[] buffer;
    private final Random random;

    private int coefficientA, coefficientB, coefficientC;

    /**
     * Constructs a {@code CollisionGenerator} with the specified {@code Random}
     * used to produce distinct strings.
     * @param random the random number generator used to produce distinct strings.
     */
    public CollisionGenerator(Random random) {
        Objects.requireNonNull(random, "random cannot be null!");
        this.random = random;
        this.buffer = new char[MAX_CHARS];
    }

    public String generate() {
        do {
            randomizeCoefficients();
            calculateBuffer();
        } while (bufferHasIllegalCodeUnits());
        return bufferAsString();
    }

    private void randomizeCoefficients() {
        // { 2114-a, 31*a-b, 31*b-c, 31*c }
        // 0 <= c <= b <= a <= 2114
        coefficientC = nextIntBetween(0, COLLISION_MAX_MULTIPLIER);
        coefficientB = nextIntBetween(coefficientC, COLLISION_MAX_MULTIPLIER);
        coefficientA = nextIntBetween(coefficientB, COLLISION_MAX_MULTIPLIER);
    }

    private int nextIntBetween(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    private void calculateBuffer() {
        // { 2114-a, 31*a-b, 31*b-c, 31*c }
        // 0 <= c <= b <= a <= 2114
        buffer[0] = (char) (COLLISION_MAX_MULTIPLIER - coefficientA);
        buffer[1] = (char) (STRING_HASHCODE_MULTIPLIER * coefficientA - coefficientB);
        buffer[2] = (char) (STRING_HASHCODE_MULTIPLIER * coefficientB - coefficientC);
        buffer[3] = (char) (STRING_HASHCODE_MULTIPLIER * coefficientC);
    }

    private String bufferAsString() {
        return new String(buffer);
    }

    private boolean bufferHasIllegalCodeUnits() {
        for (int i = 0; i < buffer.length; i++) {
            if (isUtf16Surrogate(buffer[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean isUtf16Surrogate(int codeUnit) {
        return codeUnit >= 0xD800 && codeUnit <= 0xDFFF;
    }
}
