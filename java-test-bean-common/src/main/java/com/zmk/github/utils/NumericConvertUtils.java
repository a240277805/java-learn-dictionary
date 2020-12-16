package com.zmk.github.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zmk
 * @Date: 2020/09/11/ 14:17
 * @Description
 */
public class NumericConvertUtils {
    private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public NumericConvertUtils() {
    }

    public static String toOtherNumberSystem(long number, int seed) {
        if (number < 0L) {
            number = 4294967294L + number + 2L;
        }

        char[] buf = new char[32];

        int charPos;
        for (charPos = 32; number / (long) seed > 0L; number /= (long) seed) {
            --charPos;
            buf[charPos] = digits[(int) (number % (long) seed)];
        }

        --charPos;
        buf[charPos] = digits[(int) (number % (long) seed)];
        return new String(buf, charPos, 32 - charPos);
    }

    public static long toDecimalNumber(String number, int seed) {
        char[] charBuf = number.toCharArray();
        if (seed == 10) {
            return Long.parseLong(number);
        } else {
            long result = 0L;
            long base = 1L;

            for (int i = charBuf.length - 1; i >= 0; --i) {
                int index = 0;
                int j = 0;

                for (int length = digits.length; j < length; ++j) {
                    if (digits[j] == charBuf[i]) {
                        index = j;
                    }
                }

                result += (long) index * base;
                base *= (long) seed;
            }

            return result;
        }
    }

    public static List<Long> stringToListLong(String ids) {
        if (ids == null || ids.length() == 0) {
            return null;
        }
        List<Long> list = new ArrayList<>();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            list.add(Long.valueOf(id));
        }
        return list;
    }

    public static List<Integer> stringToListInteger(String ids) {
        if (ids == null || ids.length() == 0) {
            return null;
        }
        List<Integer> list = new ArrayList<>();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            list.add(Integer.valueOf(id));
        }
        return list;
    }
}

