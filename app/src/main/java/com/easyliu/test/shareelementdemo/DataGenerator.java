package com.easyliu.test.shareelementdemo;

import java.util.ArrayList;


/**
 * @author easyliu
 */
public class DataGenerator {

    private static final String URL_ONE = "http://puui.qpic.cn/vpic/0/l0604xawgkg.png/0";
    private static final String URL_TWO = "http://puui.qpic.cn/vpic/0/q0619jmzfav.png/0";
    private static final String URL_THREE = "http://puui.qpic.cn/vpic/0/v0617g8ny0x.png/0";
    private static final String URL_FOUR = "http://puui.qpic.cn/vpic/0/i0618m9ry5a.png/0";
    private static final String URL_FIVE = "http://puui.qpic.cn/vpic/0/r06188lq7gx.png/0";

    private static final String[] IMG_ARRAY = {URL_ONE, URL_TWO, URL_THREE, URL_FOUR, URL_FIVE};

    public static final ArrayList<String> ITEMS = new ArrayList<>();

    private static final int COUNT = 25;

    static {
        for (int i = 1; i <= COUNT; i++) {
            ITEMS.add(IMG_ARRAY[i % 5]);
        }
    }
}
