package mobile.ui.notifications;

import android.graphics.Bitmap;

public class ListData {
    private int imageResId;
    private Bitmap imagebit;
    private String name;

    public ListData(int imageResId, String name) {
        this.imageResId = imageResId;
        this.name = name;
    }

    public ListData(Bitmap iamge, String name) {
        this.imagebit = iamge;
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }
    public Bitmap getImagebit() {
        return imagebit;
    }

    public String getName() {
        return name;
    }
}
