package gui;

import exception.progressBar.InvalidProgressException;
import exception.progressBar.ProgressBarException;
import exception.progressBar.ZeroProgressException;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class LoadingProgressBar {
    private static ProgressBar loadingProgressBar = new ProgressBar();
    private static int totalProgress = 0;
    private static int currentProgress = 0;

    static {
        loadingProgressBar.setPrefWidth(100);
        loadingProgressBar.setPrefHeight(10);
        AnchorPane.setBottomAnchor(loadingProgressBar, 5.0);
        AnchorPane.setRightAnchor(loadingProgressBar, 5.0);

        loadingProgressBar.progressProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal.doubleValue() == 1.0) {
                totalProgress = 0;
                currentProgress = 0;
                loadingProgressBar.setVisible(false);
            } else {
                loadingProgressBar.setVisible(true);
            }
        });
    }

    private static void updateProgressBar() throws ZeroProgressException {
        if (totalProgress == 0) {
            throw new ZeroProgressException();
        }

        loadingProgressBar.setProgress((double) currentProgress / totalProgress);
    }

    public static ProgressBar getLoadingProgressBar() {
        return loadingProgressBar;
    }

    public static void addTotalProgress(int progress) throws ProgressBarException {
        if (totalProgress + progress < 0) {
            throw new InvalidProgressException("Total progress cannot be negative");
        }

        totalProgress += progress;
        updateProgressBar();
    }

    public static void addCurrentProgress(int progress) throws ProgressBarException {
        if (currentProgress + progress < 0) {
            throw new InvalidProgressException("Current progress cannot be negative");
        }

        currentProgress += progress;
        updateProgressBar();
    }
}
