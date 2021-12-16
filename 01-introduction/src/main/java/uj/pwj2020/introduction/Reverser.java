package uj.pwj2020.introduction;

public class Reverser {

    public String reverse(String input) {
        if (input != null) {
            StringBuilder sB = new StringBuilder(input.trim());
            return sB.reverse().toString();
        } else {
            return null;
        }
    }

    public String reverseWords(String input) {
        StringBuilder res = new StringBuilder();
        StringBuilder tmp = new StringBuilder();
        String trimmedInput = input.trim();

        for (int i = trimmedInput.length() - 1; i >= 0; i--) {
            if( trimmedInput.charAt(i) != ' ') {
                tmp.insert(0, trimmedInput.charAt(i));
            } else {
                res.append(tmp).append(" ");
                tmp = new StringBuilder();
            }
        }
        res.append(tmp);
        return res.toString();
    }
}