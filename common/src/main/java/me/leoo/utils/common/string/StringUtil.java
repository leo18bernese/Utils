package me.leoo.utils.common.string;

import lombok.experimental.UtilityClass;
import me.leoo.utils.common.compatibility.SoftwareManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@UtilityClass
public class StringUtil {

    // Date
    public String getCurrentDate(String format) {
        return getDate(format, Instant.now());
    }

    public String getDate(String format, long millis) {
        return getDate(format, Instant.ofEpochMilli(millis));
    }

    public String getDate(String format, Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(format));
    }

    public String getSimpleDate(String format, long millis) {
        return getSimpleDate(format, Instant.ofEpochMilli(millis));
    }

    public String getSimpleDate(String format, Instant instant) {
        return DateTimeFormatter.ofPattern(format).format(instant.atZone(ZoneId.systemDefault()));
    }

    // String manipulation
    public String lower(String string) {
        return string.toLowerCase().replace("_", "-");
    }

    public String getCenteredMessage(String message) {
        if (message == null || message.isEmpty()) return "";

        message = SoftwareManager.getUtils().color(message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            } else {
                FontInfo fontInfo = FontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? fontInfo.getBoldLength() : fontInfo.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = FontInfo.SPACE.getLength() + 1;
        int compensated = 0;

        StringBuilder builder = new StringBuilder();
        while (compensated < toCompensate) {
            builder.append(" ");
            compensated += spaceLength;
        }

        return builder + message;
    }

    /**
     * @param list  to edit
     * @param key   placeholder to replace
     * @param value string that replace the placeholder
     */
    public void replaceWithList(List<String> list, String key, List<String> value) {
        for (int i = 0; i < list.size(); i++) {
            String line = list.get(i);

            if (line.contains(key)) {
                if (value == null || value.isEmpty() || String.join("", value).isEmpty()) {
                    list.remove(line);
                    continue;
                }

                for (int n = 1; n < value.size(); n++) {
                    list.add(i + n, n == value.size() - 1 ? line.replace(key, value.get(n)) : value.get(n));
                }

                list.set(i, value.size() == 1 ? line.replace(key, value.get(0)) : value.get(0));
            }
        }
    }
}
