package uj.pwj2020.introduction;

import java.util.stream.IntStream;

public class Banner {

    public String[] toBanner(String input) {
        if (input == null) {
            return new String[]{};
        } else {
            final int height = 7;
            String tmp = input.toUpperCase();
            String[] result = new String[height];

            for(int i = 0; i < height; i++){
                result[i] = "";
            }

            String[] alphabet = new String[height];

            alphabet[0] = "   #    ######   #####  ######  ####### #######  #####  #     # ###       # #    # #       #     # #     # ####### ######   #####  ######   #####  ####### #     # #     # #     # #     # #     # ####### ";
            alphabet[1] = "  # #   #     # #     # #     # #       #       #     # #     #  #        # #   #  #       ##   ## ##    # #     # #     # #     # #     # #     #    #    #     # #     # #  #  #  #   #   #   #       #  ";
            alphabet[2] = " #   #  #     # #       #     # #       #       #       #     #  #        # #  #   #       # # # # # #   # #     # #     # #     # #     # #          #    #     # #     # #  #  #   # #     # #       #   ";
            alphabet[3] = "#     # ######  #       #     # #####   #####   #  #### #######  #        # ###    #       #  #  # #  #  # #     # ######  #     # ######   #####     #    #     # #     # #  #  #    #       #       #    ";
            alphabet[4] = "####### #     # #       #     # #       #       #     # #     #  #  #     # #  #   #       #     # #   # # #     # #       #   # # #   #         #    #    #     #  #   #  #  #  #   # #      #      #     ";
            alphabet[5] = "#     # #     # #     # #     # #       #       #     # #     #  #  #     # #   #  #       #     # #    ## #     # #       #    #  #    #  #     #    #    #     #   # #   #  #  #  #   #     #     #      ";
            alphabet[6] = "#     # ######   #####  ######  ####### #        #####  #     # ###  #####  #    # ####### #     # #     # ####### #        #### # #     #  #####     #     #####     #     ## ##  #     #    #    ####### ";

            for (int i = 0; i < tmp.length(); i++) {
                char letter = tmp.charAt(i);

                int lengthOfLetter = 8; //with space
                int allowShorterLengths = 11; //I and K are both 11 shorter in total by we have to take the earlier characters

                switch (letter) {
                    case ' ' -> IntStream.range(0, height).forEach(j -> result[j] += "   ");
                    case 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' -> IntStream.range(0, height).forEach(j -> result[j] += alphabet[j].substring(((int) letter - 'A') * lengthOfLetter, ((int) letter - 'A' + 1) * lengthOfLetter));
                    case 'I' -> IntStream.range(0, height).forEach(j -> result[j] += alphabet[j].substring(64, 68)); //64 - starting index of I in array, 68 - starting index of J in array
                    case 'J' -> IntStream.range(0, height).forEach(j -> result[j] += alphabet[j].substring(68, 76)); //68 - starting index of J in array, 77 - ending index of K in array
                    case 'K' -> IntStream.range(0, height).forEach(j -> result[j] += alphabet[j].substring(76, 83)); //76 - starting index of K in array, 83 - ending index of L in array
                    default -> IntStream.range(0, height).forEach(j -> result[j] += alphabet[j].substring(83 + ((int) letter - 'A' - allowShorterLengths) * lengthOfLetter, 83 + ((int) letter - 'A' - allowShorterLengths + 1) * lengthOfLetter));
                }
            }
            return result;
        }
    }
}