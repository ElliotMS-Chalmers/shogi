package view;

import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import util.Side;

public class ClockView extends VBox {
    private int time = 0;
    private final Label playerLabel;
    private final Label timeLabel;
    private String playerName;

    public ClockView(Side side) {
        super();
        switch (side) {
            case SENTE -> playerName = "Black";
            case GOTE -> playerName = "White";
        }

        // Label for player name (e.g., "White" or "Black")
        this.playerLabel = new Label(playerName);
        this.playerLabel.setFont(new Font(24)); // Set font size for player name
        this.playerLabel.setTextAlignment(TextAlignment.CENTER);

        // Label for the timer
        this.timeLabel = new Label("00:00");
        this.timeLabel.setFont(new Font(48)); // Set font size for digital clock
        this.timeLabel.setTextAlignment(TextAlignment.CENTER);

        // Add labels to the layout
        this.getChildren().addAll(playerLabel, timeLabel);
        this.setVisible(false);
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

    private void updateVisibility() {
        if (time > 0) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    private void updateTimeLabel() {
        int minutes = time / 60;
        int seconds = time % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        timeLabel.setText(timeString);
    }
}
