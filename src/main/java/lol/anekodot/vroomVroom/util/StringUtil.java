package lol.anekodot.vroomVroom.util;

import java.util.Random;

public class StringUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";


    public static String generateRandomString(int size) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);

            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder output = new StringBuilder();

        // I'm inefficientmaxxing over here, leave me alone.
        for (byte b : bytes) {
            output.append(String.format("%02X", b));
        }

        return output.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

//    public static void hexToBytes(String s, List<Byte> data) {
//        String[] hex = s.split("(?<=\\G.{2})");
//        byte[] ans = new byte[s.length()];
////
//        int i = 0;
//        for (String bytez : hex) {
//            // using left shift operator on every character
//            if (bytez.equals("0")) {
//                ans[i] = (byte) 0;
//                return;
//            }
//
//            ans[i] = (byte) ((Character.digit(bytez.charAt(0), 16) << 4)
//                    + Character.digit(bytez.charAt(1), 16));
//            i++;
//        }
//
//        for (Byte b : ans) {
//            data.add(b);
//        }
//    }
}
