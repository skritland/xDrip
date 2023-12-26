package com.eveningoutpost.dexdrip.utils;

import android.content.Context;

import android.media.AudioAttributes;
import android.os.Build;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.eveningoutpost.dexdrip.models.BgReading;
import com.eveningoutpost.dexdrip.models.JoH;
import com.eveningoutpost.dexdrip.xdrip;

import static android.content.Context.VIBRATOR_SERVICE;


/**
 * Created by jamorham on 06/01/16.
 * <p>
 * lazy helper class for utilities
 */
public class VibrateBloodGlucose {
    private final static String TAG = "VibrateBloodGlucose";

    // vibrations
    private static final long SHORT_VIBRATION_MS = 100;
    private static final long LONG_VIBRATION_MS = 500;
    private static final long ERROR_VIBRATION_MS = 1000;

    // gaps
    private static final long SHORT_GAP_MS = 250;
    private static final long MEDIUM_GAP_MS = 750;
    private static final long LONG_GAP_MS = 1250;

    private static final int DO_NOT_REPEAT = -1;

    private static final AudioAttributes bgVibAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build();

    public static final int VIBRATIONS_TYPE_NONE = 0;
    public static final int VIBRATIONS_TYPE_VALUE_STD_TREND_NO = 1;
    public static final int VIBRATIONS_TYPE_VALUE_STD_TREND_STD = 2;

    
    public static void vibrateBg(final BgReading bg, int vibrationsType) {
        if (vibrationsType == VIBRATIONS_TYPE_NONE) {
            return;
        }

        final Vibrator vibrator = (Vibrator) xdrip.getAppContext().getSystemService(VIBRATOR_SERVICE);

        long[] pattern = new long[]{SHORT_GAP_MS};
        pattern = concat(pattern, getVibrationPatternForBgMgDl(bg));

        VibrationEffect vibe = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibe = VibrationEffect.createWaveform(pattern, DO_NOT_REPEAT);
            // start vibration
            vibrator.vibrate(vibe, bgVibAttributes);
        }
        else {
            vibrator.vibrate(pattern, DO_NOT_REPEAT);
        }

    }

    private static long[] getVibrationPatternForBgMgDl(final BgReading bg) {
        long[] pattern = new long[]{};
        final int bgMgDl = (int)bg.getDg_mgdl();

        if ((bgMgDl < 0) || (bgMgDl > 999)) {
            return new long[]{ERROR_VIBRATION_MS};
        }

        final int digit1 = bgMgDl % 10;
        final int digit2 = (bgMgDl / 10) % 10;
        final int digit3 = (bgMgDl / 100) % 10;
        Log.d(TAG, "BG to vibrate: " + String.format("%d %d %d", digit3, digit2, digit1));
        // third number of BG mg/dl
        pattern = concat(pattern, getVibrationPatternForDigit(digit3));
        // gap between numbers
        pattern = concat(pattern, new long[]{MEDIUM_GAP_MS});
        // second number of BG mg/dl
        pattern = concat(pattern, getVibrationPatternForDigit(digit2));
        // gap between numbers
        pattern = concat(pattern, new long[]{MEDIUM_GAP_MS});
        // first number of BG mg/dl
        pattern = concat(pattern, getVibrationPatternForDigit(digit1));
        return pattern;
    }

    private static long[] getVibrationPatternForDigit(int digit) {
        switch (digit) {
            case 0:
                return new long[]{LONG_VIBRATION_MS, SHORT_GAP_MS, LONG_VIBRATION_MS};
            case 1:
                return new long[]{SHORT_VIBRATION_MS};
            case 2:
                return new long[]{SHORT_VIBRATION_MS, SHORT_GAP_MS, SHORT_VIBRATION_MS};
            case 3:
                return new long[]{SHORT_VIBRATION_MS, SHORT_GAP_MS, SHORT_VIBRATION_MS,
                        SHORT_GAP_MS, SHORT_VIBRATION_MS};
            case 4:
                return new long[]{SHORT_VIBRATION_MS, SHORT_GAP_MS, SHORT_VIBRATION_MS,
                        SHORT_GAP_MS, SHORT_VIBRATION_MS, SHORT_GAP_MS, SHORT_VIBRATION_MS};
            case 5:
                return new long[]{LONG_VIBRATION_MS};
            case 6:
                return new long[]{LONG_VIBRATION_MS, SHORT_GAP_MS, SHORT_VIBRATION_MS};
            case 7:
                return new long[]{LONG_VIBRATION_MS, SHORT_GAP_MS, SHORT_VIBRATION_MS,
                        SHORT_GAP_MS, SHORT_VIBRATION_MS};
            case 8:
                return new long[]{LONG_VIBRATION_MS, SHORT_GAP_MS, SHORT_VIBRATION_MS,
                        SHORT_GAP_MS, SHORT_VIBRATION_MS, SHORT_GAP_MS, SHORT_VIBRATION_MS};
            case 9:
                return new long[]{LONG_VIBRATION_MS, SHORT_GAP_MS, SHORT_VIBRATION_MS,
                        SHORT_GAP_MS, SHORT_VIBRATION_MS, SHORT_GAP_MS, SHORT_VIBRATION_MS,
                        SHORT_GAP_MS, SHORT_VIBRATION_MS};
            default:
                return new long[]{};
        }
    }

    private static long[] concat(long[] array1, long[] array2) {
        int array1Len = array1.length;
        int array2Len = array2.length;
        long[] arrayResult = new long[array1Len+array2Len];
        System.arraycopy(array1, 0, arrayResult, 0, array1Len);
        System.arraycopy(array2, 0, arrayResult, array1Len, array2Len);
        return arrayResult;
    }

}
