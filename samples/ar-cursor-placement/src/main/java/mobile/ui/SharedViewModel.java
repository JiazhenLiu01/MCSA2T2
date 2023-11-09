package mobile.ui;

import android.graphics.Bitmap;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Pair<String, Bitmap>>> itemsLiveData = new MutableLiveData<>(new ArrayList<>());

    // Method to add an item
    public void addItem(String name, Bitmap image) {
        List<Pair<String, Bitmap>> currentItems = itemsLiveData.getValue();
        if (currentItems != null) {
            currentItems.add(new Pair<>(name, image));
            itemsLiveData.setValue(currentItems); // Trigger the observer
        }
    }

    // Method to get all items
    public LiveData<List<Pair<String, Bitmap>>> getItems() {
        return itemsLiveData;
    }
}
