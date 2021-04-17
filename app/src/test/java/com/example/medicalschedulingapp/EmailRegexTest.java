package com.example.medicalschedulingapp;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EmailRegexTest {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w\\p{Punct}]{1,64}@[a-z0-9_-]+.[a-z]{2,}$");
    @Test
    public void correct_email_1() {
        CharSequence  email = "yao.521@osu.edu";
        Matcher m = EMAIL_REGEX.matcher(email);
        boolean b = m.matches();
        assertEquals(true, b);
    }
    @Test
    public void correct_email_2() {
        Matcher m = EMAIL_REGEX.matcher("yaosam.521@gmail.com");
        boolean b = m.matches();
        assertEquals(true, b);
    }
    @Test
    public void correct_email_3() {
        Matcher m = EMAIL_REGEX.matcher("p@p.io");
        boolean b = m.matches();
        assertEquals(true, b);
    }
    @Test
    public void incorrect_email_1() {
        //This is the code for the laughing emoji I suppose
        Matcher m = EMAIL_REGEX.matcher("\uD83D\uDE02@\uD83D\uDE02.com");
        boolean b = m.matches();
        assertEquals(false, b);
    }
    @Test
    public void incorrect_email_2() {
        Matcher m = EMAIL_REGEX.matcher("@.com");
        boolean b = m.matches();
        assertEquals(false, b);
    }
    @Test
    public void incorrect_email_3() {
        Matcher m = EMAIL_REGEX.matcher("");
        boolean b = m.matches();
        assertEquals(false, b);
    }
}