package com.orange.orangeetmoipro.utilities;

public class PasswordStrength {

    PasswordStrength() {

    }

    public static int calculate(String password) {
        int score = 0;
        boolean upper = false;
        boolean lower = false;
        boolean digit = false;
        boolean specialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (!specialChar && !Character.isLetterOrDigit(c)) {
                score++;
                specialChar = true;
            } else {
                if (!digit && Character.isDigit(c)) {
                    score++;
                    digit = true;
                } else if (Character.isUpperCase(c) && !upper) {
                    score++;
                    upper = true;
                } else if (Character.isLowerCase(c) && !lower) {
                    score++;
                    lower = true;
                }
                
            }
        }

        return score;

    }
}
