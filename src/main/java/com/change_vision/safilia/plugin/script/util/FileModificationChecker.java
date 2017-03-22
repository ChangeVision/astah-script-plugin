package com.change_vision.safilia.plugin.script.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class FileModificationChecker {

    private File targetFile;
    private Runnable onModifiedRunner;

    private static final int DURATION = 2000; // msec
    private long lastModified = 0;
    private boolean wasStopped = false;

    public FileModificationChecker(File targetFile, Runnable onModifiedRunner) {
        this.targetFile = targetFile;
        this.onModifiedRunner = onModifiedRunner;
    }

    public void start() {
        lastModified = targetFile.lastModified();
        wasStopped = false;
        Thread thread = new Thread(new Checker());
        thread.start();
    }

    public void stop() {
        wasStopped = true;
    }

    protected void check() {
        long currentLastModified = targetFile.lastModified();
        if (lastModified < currentLastModified) {
            lastModified = currentLastModified;
            onModified();
        }
    }

    protected void onModified() {
        try {
            SwingUtilities.invokeAndWait(onModifiedRunner);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected class Checker implements Runnable {
        public void run() {
            while (!wasStopped) {
                check();

                try {
                    Thread.sleep(DURATION);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}