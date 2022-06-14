package com.github.tgnthump.cfnnagintellijplugin;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public class MyBundle extends DynamicBundle {
    @NonNls
    private static final String BUNDLE = "messages.MyBundle";

    public MyBundle() {
        super(BUNDLE);
    }

    public static String message(@PropertyKey(resourceBundle = BUNDLE) String key, Object... params){
        return INSTANCE.getMessage(key, params);
    }

    public static Supplier<String> messagePointer(@PropertyKey(resourceBundle = BUNDLE) String key, Object... params){
        return INSTANCE.getLazyMessage(key, params);
    }

}
