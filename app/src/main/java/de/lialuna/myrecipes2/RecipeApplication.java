package de.lialuna.myrecipes2;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeApplication extends Application {
    public static ExecutorService parseRecipeExecutorService = Executors.newSingleThreadExecutor();
    public static Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());


}
