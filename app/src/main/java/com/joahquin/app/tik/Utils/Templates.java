package com.joahquin.app.tik.Utils;

import java.util.concurrent.TimeUnit;

public class Templates {

    public static class WATER_MONITOR{

        public static int ASSIGNMENT_ID = 1;
        public static String ASSIGNMENT_DESC = "Remind me of taking water";
        public static boolean ASSIGNMENT_RECCURSIVE = true;
        public static long ASSIGNMENT_INTERVAL = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
    }
}
