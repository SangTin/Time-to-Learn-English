package gui;

import exception.progressBar.InvalidProgressException;
import exception.progressBar.ProgressBarException;
import exception.progressBar.ZeroProgressException;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

public class LoadingProgressBar {
   private static ProgressBar loadingProgressBar = new ProgressBar();
   private static Tooltip loadingTooltip;
   private static int totalProgress = 0;
   private static int currentProgress = 0;

   private static void setUpProgressBar() {
      loadingProgressBar.setPrefWidth(100.0D);
      loadingProgressBar.setPrefHeight(10.0D);
      AnchorPane.setBottomAnchor(loadingProgressBar, 5.0D);
      AnchorPane.setRightAnchor(loadingProgressBar, 5.0D);
      loadingProgressBar.progressProperty().addListener((observable, oldVal, newVal) -> {
         if (newVal.doubleValue() == 1.0D) {
            totalProgress = 0;
            currentProgress = 0;
            loadingProgressBar.setVisible(false);
         } else {
            loadingProgressBar.setVisible(true);
         }

      });
      loadingTooltip = new Tooltip("Loading...");
      loadingProgressBar.setTooltip(loadingTooltip);
   }

   private static void updateProgressBar() throws ZeroProgressException {
      if (totalProgress == 0) {
         throw new ZeroProgressException();
      } else {
         loadingProgressBar.setProgress((double)currentProgress / (double)totalProgress);
         loadingTooltip.setText("Loading... " + currentProgress + "/" + totalProgress);
      }
   }

   public static ProgressBar getLoadingProgressBar() {
      if (loadingProgressBar == null) {
         loadingProgressBar = new ProgressBar();
         setUpProgressBar();
      }

      return loadingProgressBar;
   }

   public static void addTotalProgress(int progress) throws ProgressBarException {
      if (totalProgress + progress < 0) {
         throw new InvalidProgressException("Total progress cannot be negative");
      } else {
         totalProgress += progress;
         updateProgressBar();
      }
   }

   public static void step(int progress) throws ProgressBarException {
      if (currentProgress + progress < 0) {
         throw new InvalidProgressException("Current progress cannot be negative");
      } else {
         currentProgress += progress;
         updateProgressBar();
      }
   }

   public static void step() throws ProgressBarException {
      step(1);
   }

   static {
      setUpProgressBar();
   }
}
