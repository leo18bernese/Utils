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

    public String getCurrentDate(String format) {
        return getDate(format, Instant.now());
    }

    public String getDate(String format, Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(formatter);
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
    public List<String> replaceWithList(List<String> list, String key, List<String> value) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains(key)) {
                for (int n = 1; n < value.size(); n++) {
                    list.add(i + n, n == value.size() - 1 ? list.get(i).replace(key, value.get(n)) : value.get(n));
                }

                list.set(i, value.size() == 1 ? list.get(i).replace(key, value.get(0)) : value.get(0));
            }
        }

        return list;
    }
}
