package com.tma.techday.foodnutrientfact.gui.event;

public class ResultModeChangeEvent {
    String resultMode;

    public ResultModeChangeEvent(String resultMode) {
        this.resultMode = resultMode;
    }

    public String getResultMode() {
        return resultMode;
    }

    public void setResultMode(String resultMode) {
        this.resultMode = resultMode;
    }
}
