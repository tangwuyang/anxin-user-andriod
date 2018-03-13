package com.anxin.kitchen.event;

public class OnSaveBitmapEvent {
    private String ImavePath;
    private String SaveName;

    public OnSaveBitmapEvent(String ImavePath, String SaveName) {
        this.ImavePath = ImavePath;
        this.SaveName = SaveName;
    }

    public String getImavePath() {
        return ImavePath;
    }

    public void setImavePath(String imavePath) {
        ImavePath = imavePath;
    }

    public String getSaveName() {
        return SaveName;
    }

    public void setSaveName(String saveName) {
        SaveName = saveName;
    }
}
