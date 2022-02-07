package org.example.wordCounterTest;

import org.example.AbstractTest;
import org.example.WordCounter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordCounterTest extends AbstractTest {
    private static WordCounter wordCounter;


    @Test
    void count() throws IOException {
    String string = getResourceAsString("data.txt");
    WordCounter counter= new WordCounter(string.split(" +"));
       int actual = counter.count("a");
       int expected =5;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void top3() throws IOException {
        String string = getResourceAsString("data.txt");
        WordCounter counter= new WordCounter(string.split(" +"));

       List<String>actual =counter.top3();
        List<String> expected = Arrays.asList (getResourceAsString("expected.txt").split(" +"));

        Assertions.assertEquals(expected,actual);

    }
}