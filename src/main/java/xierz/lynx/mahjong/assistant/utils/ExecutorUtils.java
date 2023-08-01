package xierz.lynx.mahjong.assistant.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ExecutorUtils {
    public static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors(), 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(128),
            r -> {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            });

    private ExecutorUtils() {
    }
}
