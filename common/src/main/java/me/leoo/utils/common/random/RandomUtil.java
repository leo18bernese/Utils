package me.leoo.utils.common.random;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class RandomUtil {

    public final Random RANDOM = new Random();

    public final ThreadLocalRandom LOCAL = ThreadLocalRandom.current();

    public Color getRandomRgb() {
        return new Color(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256));
    }

    public int randomInt(int bound) {
        return LOCAL.nextInt(bound);
    }

    public int randomInt(int origin, int bound) {
        return LOCAL.nextInt(origin, bound);
    }
}
