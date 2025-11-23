package utils;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static int getRandomInt(int min, int max){
        return ThreadLocalRandom.current().nextInt(min,max);
    }

    public String getRandomPhone(){
        return String.format("%s"+"%s"+"%s",getRandomInt(901,999),getRandomInt(901,999),getRandomInt(1111,9999));
    }
}
