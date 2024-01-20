package me.leoo.utils.common.random;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class RandomUtil {

    public final Random random = new Random();

    public final ThreadLocalRandom localRandom = ThreadLocalRandom.current();

    public Color getRandomRgb() {
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
