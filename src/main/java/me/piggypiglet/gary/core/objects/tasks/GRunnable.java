package me.piggypiglet.gary.core.objects.tasks;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class GRunnable implements Runnable {
    /**
     * Util method to sleep without needing a try/catch.<br/>
     * <i>WARNING: This method is unreliable, may not wake up.</i>
     * @param ms Milliseconds to sleep for before waking up.
     */
    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
