package view;

import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import util.Side;

public class ClockView extends VBox {
    private int time = 0;
    private final Label label;

    public ClockView(Side side) {
        super();
        this.getStyleClass().add("clock");
        // Label for the timer
        this.label = new Label("00:00");
        this.label.setFont(new Font(24));
        this.label.setTextAlignment(TextAlignment.CENTER);
        this.setAlignment(Pos.CENTER);

        // Add labels to the layout
        this.getChildren().add(label);
        this.setVisible(false);

        scaleFont();
        this.widthProperty().addListener((obs, oldVal, newVal) -> scaleFont());
    }

    public void bindToTimer(IntegerProperty timerProperty) {
        time = timerProperty.get();
        updateVisibility();
        updateTimeLabel();
        timerProperty.addListener((observable, oldValue, newValue) ->
                javafx.application.Platform.runLater(() -> {
                    time = newValue.intValue();
                    updateVisibility();
                    updateTimeLabel();
                })
        );
    }

    public void initialize(Integer seconds) {
        time = seconds;
        updateVisibility();
        updateTimeLabel();
    }

    public void setTime(Integer seconds) {
        javafx.application.Platform.runLater(() -> {
            time = seconds;
            updateVisibility();
            updateTimeLabel();
        });
    }

    private void updateVisibility() {
        setVisible(time > 0);
    }

    private void updateTimeLabel() {
        int minutes = time / 60;
        int seconds = time % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        label.setText(timeString);
    }

    public void setActive(boolean active) {
        if (active) {
            if (!this.getStyleClass().contains("active")) {
                this.getStyleClass().add("active");
            }
        } else {
            this.getStyleClass().remove("active");
        }
    }

    private void scaleFont() {
        double width = this.getWidth();
        if (width > 0) {
            double fontSize = width / 3.5;
            label.setFont(new Font(fontSize));
        }
    }
}
