package com.eternitywall.btcclock;

import android.content.Context;
import com.eternitywall.btcclock.clocks.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import dalvik.system.DexFile;

public class Clocks {

    private static Clock[] clocks = {
            new BitcoinClock(),
            new BitcoinGithubClock(),
            new BitcoinPriceClock(),
            new BitcoinPriceEurClock(),
            new StandardClock(),
            new NasaClock(),
            new TimestampClock(),
            new SpaceXClock()
    };

    public static Clock[] get() {
        return clocks;
    }

    // parse from package
    public static String[] getClassesOfPackage(final Context context, final String packageName) {
        final ArrayList<String> classes = new ArrayList<String>();
        try {
            final String packageCodePath = context.getPackageCodePath();
            final DexFile df = new DexFile(packageCodePath);
            for (final Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
                final String className = iter.nextElement();
                if (className.contains(packageName) && !className.contains("$"))
                    classes.add(className);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return classes.toArray(new String[classes.size()]);
    }

    public static Clock get(final int id) {
        if (clocks == null)
            clocks = get();
        for (final Clock clock : clocks) {
            if (clock.id == id)
                return clock;
        }
        return null;
    }
}
