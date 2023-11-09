package mobile.ui.notifications;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.github.sceneview.sample.arcursorplacement.R;
import io.github.sceneview.sample.arcursorplacement.databinding.FragmentNotificationsBinding;
import mobile.ui.SharedViewModel;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<mobile.ui.notifications.ListData> imageList = new ArrayList<>();
//        imageList.add(new ListData(R.drawable.bed, "Bed"));
//        imageList.add(new ListData(R.drawable.bed, "Bed"));
//        imageList.add(new ListData(R.drawable.bed, "Bed"));

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Observe the selected item
        sharedViewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                for (Pair<String, Bitmap> item : items) { // Iterate through all items
                    // Use the name and image as needed
                    String name = item.first;
                    Bitmap image = item.second;
                    imageList.add(new ListData(image, name));

                }
            }
        });


//         RecyclerView horizontalImageList = root.findViewById(R.id.horizontal_image_list);
//
//        List<Integer> imageResources = new ArrayList<>();
//        imageResources.add(R.drawable.bed);
//        imageResources.add(R.drawable.bed);
//        imageResources.add(R.drawable.bed);
//        imageResources.add(R.drawable.bed);
//        imageResources.add(R.drawable.bed);
//
//        ImageListAdapter adapter = new ImageListAdapter(imageResources);
//        horizontalImageList.setAdapter(adapter);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
//        horizontalImageList.setLayoutManager(layoutManager);






        RecyclerView horizontalImageList = root.findViewById(R.id.horizontal_image_list);



        ImageListAdapter adapter = new ImageListAdapter(imageList);
        horizontalImageList.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontalImageList.setLayoutManager(layoutManager);

//        ArrayList<mobile.ui.notifications.ListData> dataArrayList = new ArrayList<>();
//        int[] imageList = new int[]{R.drawable.bed, R.drawable.bed, R.drawable.bed};
//        String[] nameList = new String[]{"Bed", "Bed", "Bed"};
//
//        for (int i = 0; i < imageList.length; i++) {
//            ListData listData = new ListData(imageList[i], nameList[i]);
//            dataArrayList.add(listData);
//        }
//        ImageListAdapter listAdapter = new ImageListAdapter(requireContext(), dataArrayList);
//        horizontalImageList.setAdapter(listAdapter);
//        horizontalImageList.setClickable(true);

        TextView textNomeUser = root.findViewById(R.id.textNomeUser);
        TextView textEmailUser = root.findViewById(R.id.textEmailUser);
        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("USERNAME_KEY")) {
                String username = intent.getStringExtra("USERNAME_KEY");
                String email = intent.getStringExtra("EMAIL_KEY");
                textNomeUser.setText(username);
                textEmailUser.setText(email);
            }
        }

        final TextView textView = binding.textNotification;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}