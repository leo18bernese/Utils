package me.leoo.utils.common.random;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.util.Random;

@UtilityClass
public class RandomUtil {

    private static final Random random = new Random();

    public Color getRandomRgb() {
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
