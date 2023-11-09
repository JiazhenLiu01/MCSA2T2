package mobile.ui.home;

import android.graphics.Bitmap;

import io.github.sceneview.sample.arcursorplacement.R;

public class ListData {
    String name, size;
    int image;
    Bitmap img;

    int star;

    public ListData(String name, String size, int image,boolean star) {
        this.name = name;
        this.size = size;
        this.image = image;
        if(star)
            this.star = star ? R.drawable.star : 0; // 设置为固定图片资源（假设 star 图片资源的 ID 是 R.drawable.star）
    }

    public ListData(String name, Bitmap image,String size){
        this.name =name;
        this.img=image;
        this.size=size;
    }


    public ListData(String name, Bitmap image,String size,boolean star){
        this.name =name;
        this.img=image;
        this.size=size;
        if(star)
            this.star = star ? R.drawable.star : 0; // 设置为固定图片资源（假设 star 图片资源的 ID 是 R.drawable.star）

    }


    public void setStar(boolean star) {
        if(star)
            this.star = star ? R.drawable.star : 0; // 设置为固定图片资源（假设 star 图片资源的 ID 是 R.drawable.star）
    }
}
