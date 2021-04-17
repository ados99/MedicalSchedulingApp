package com.example.medicalschedulingapp;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PasswordRegexTest {
    private static final Pattern PASS_REGEX = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    @Test
    public void correct_password_1() {
        CharSequence  password = "Password1!";
        Matcher m = PASS_REGEX.matcher(password);
        boolean b = m.matches();
        assertEquals(true, b);
    }
    @Test
    public void correct_password_2() {
        CharSequence  password = "Passwo1!";
        Matcher m = PASS_REGEX.matcher(password);
        boolean b = m.matches();
        assertEquals(true, b);
    }
    @Test
    public void correct_password_3() {
        CharSequence  password = "Pa1!@$%&*";
        Matcher m = PASS_REGEX.matcher(password);
        boolean b = m.matches();
        assertEquals(true, b);
    }
    @Test
    public void incorrect_password_1() {
        CharSequence  password = "";
        Matcher m = PASS_REGEX.matcher(password);
        boolean b = m.matches();
        assertEquals(false, b);
    }
    @Test
    public void incorrect_password_2() {
        CharSequence  password = "password";
        Matcher m = PASS_REGEX.matcher(password);
        boolean b = m.matches();
        assertEquals(false, b);
    }
    @Test
    public void incorrect_password_3() {
        CharSequence  password = "PASSWORD";
        Matcher m = PASS_REGEX.matcher(password);
        boolean b = m.matches();
        assertEquals(false, b);
    }
    @Test
    public void incorrect_password_4() {
        CharSequence  password = "Password";
        Matcher m = PASS_REGEX.matcher(password);
        boolean b = m.matches();
        assertEquals(false, b);
    }
}