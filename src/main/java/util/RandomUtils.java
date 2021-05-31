package util;

import java.util.Random;

public class RandomUtils {
    Random random = new Random();

    /**
     * 产生激活码
     * @return
     */
    public int getRandom() {

        int number = random.nextInt(900000) + 100000;
        return number;
    }

    /**
     * 测试
     * @param args
     */
//    public static void main(String[] args) {
//        RandomUtils randomUtils = new RandomUtils();
//        int random = randomUtils.getRandom();
//        System.out.println(random);
//    }
}
