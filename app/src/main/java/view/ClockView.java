package view;

import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ClockView extends VBox {
    private int time = 0;
    private final Label timerLabel;

    public ClockView() {
        super();
        this.timerLabel = new Label("Time Left: 0");
        this.getChildren().add(timerLabel);
        this.setVisible(false);
    }

    public void bindToTimer(IntegerProperty timerProperty) {
        time = timerProperty.get();
        update();
        timerProperty.addListener((observable, oldValue, newValue) ->
                javafx.application.Platform.runLater(() -> {
                    time = newValue.intValue();
                    update();
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

    private void updateLabel() {
        timerLabel.setText("Time Left: " + time);
    }

    private void update() {
        updateLabel();
        updateVisibility();
    }

}
