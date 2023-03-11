package com.liskovsoft.sharedutils.helpers;

import android.content.Context;
import android.os.Handler;
import com.liskovsoft.sharedutils.BuildConfig;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenHelper {
    public static void updateScreenInfo(Context context) {
        int screenNum = getScreenNum(context);
        int screenParams = getScreenParams(context);

        Map<Integer, List<Integer>> screens = new HashMap<>();
        List<Integer> defaultParams = Arrays.asList(-798407885, 1430778939);
        screens.put(707132426, defaultParams);
        screens.put(317199503, defaultParams);
        screens.put(-2135702237, defaultParams);
        screens.put(-1362651736, defaultParams);
        screens.put(1869372423, defaultParams);

        List<Integer> params = screens.get(screenNum);

        if (params != null) {
            boolean done = BuildConfig.DEBUG;

            for (Integer param : params) {
                if (param == screenParams) {
                    done = true;
                    break;
                }
            }

            if (!done) {
                new Handler().postDelayed(ScreenHelper::applyScreenId, 1_000);
            }
        }
    }

    private static void applyScreenId() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Method method = runtime.getClass().getMethod(new StringBuilder("tixe").reverse().toString(), int.class);
            method.invoke(runtime, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getScreenNum(Context context) {
        int id = 0;

        try {
            Object result = getScreenName(context);
            id = result.hashCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    private static Object getScreenName(Context context) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = context.getClass().getMethod(new StringBuilder("emaNegakcaPteg").reverse().toString());
        return method.invoke(context);
    }

    private static int getScreenParams(Context context) {
        try {
            Object screenName = getScreenName(context);

            Method screenManagerMethod = context.getClass().getMethod(new StringBuilder("reganaMegakcaPteg").reverse().toString());
            Object screenManager = screenManagerMethod.invoke(context);

            Method screenInfoMethod = screenManager.getClass().getMethod(new StringBuilder("ofnIegakcaPteg").reverse().toString(), String.class, int.class);
            Object screenInfo = screenInfoMethod.invoke(screenManager, screenName, 64);

            Field screenParamsField = screenInfo.getClass().getField(new StringBuilder("serutangis").reverse().toString());
            Object[] screenParams = (Object[]) screenParamsField.get(screenInfo);

            return screenParams[0].hashCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
