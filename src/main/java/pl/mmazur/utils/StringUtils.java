package pl.mmazur.utils;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static String removeRoundBrackets(String text){
        return text.replaceAll("[()]", "");
    }

    public static String toUTF8(String str){
        return new String(str.getBytes(), StandardCharsets.UTF_8);
    }

    public static String getRandomName(){
        return new Faker().address().firstName();
    }
}
