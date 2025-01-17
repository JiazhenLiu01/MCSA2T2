package mobile.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import io.github.sceneview.sample.arcursorplacement.Activity;
import io.github.sceneview.sample.arcursorplacement.R;
import io.github.sceneview.sample.arcursorplacement.databinding.FragmentHomeBinding;
import mobile.ui.SharedViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    ListAdapter listAdapter;

    ListData listData;


    ArrayList<ListData> dataItems = new ArrayList<>();

    SearchView searchView;

    ArrayList<ListData> nameStar = new ArrayList<>();

    ImageView filterImageView;

    ArrayList<ListData> dataItem2;



    boolean firstGet = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        searchView = root.findViewById(R.id.search_view);

        filterImageView = root.findViewById(R.id.filter);


//        ArrayList<ListData> dataArrayList = new ArrayList<>();
//        int[] imageList = new int[]{R.drawable.bed, R.drawable.bed, R.drawable.bed};
//        String[] nameList = new String[]{"Bed", "Chair", "Bed"};
//        String[] sizeList = new String[]{"30*40*50", "30*40*50", "30*40*50"};
//        Boolean[] starList = new Boolean[]{false, false, true};
//
//        for (int i = 0; i < imageList.length; i++) {
//            listData = new ListData(nameList[i], sizeList[i], imageList[i],starList[i]);
//            dataArrayList.add(listData);
//        }

//        dataItems = dataArrayList;
//        listAdapter = new ListAdapter(requireContext(), dataArrayList);
//        binding.listView.setAdapter(listAdapter);





        makeRequest();
        binding.listView.setClickable(true);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                  downloadFurnitureRequest(dataItem2.get(i).name);
                  dataItem2.get(i).setStar(true);
                for(ListData item3 : nameStar){
                    if(dataItem2.get(i).name.equals(item3.name))
                        item3.setStar(true);
                }

                for(ListData item3 : dataItems){
                    if(dataItem2.get(i).name.equals(item3.name))
                        item3.setStar(true);
                }
//                    nameStar.get(i).setStar(true);
                  ListAdapter adapter = new ListAdapter(requireContext(), dataItem2);
                    // assign the adapter to the recycler view
                    binding.listView.setAdapter(adapter);

                    String name =dataItem2.get(i).name;
                    Bitmap imge =dataItem2.get(i).img;

                    SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                    // Set the selected item
                    sharedViewModel.addItem(name, imge);



            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return true;
            }
        });

        // Inside your onCreateView method in HomeFragment
//        binding.arscenebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Start the MainFragment.kt
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.container, new MainFragment()); // Assuming `R.id.container` is the ID of your fragment container. Adjust as necessary.
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        binding.arscenebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity.class);
                startActivity(intent);
            }
        });


        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void makeRequest() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://mobiles-2a62216dada4.herokuapp.com/model";
        Log.e("requestTest", "url-set");


        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.e("requestTest", response.toString());
                                try {
                                    dataItems = new ArrayList<>();
                                    dataItem2 = new ArrayList<>();
                                    Log.e("requestTest", "before-pass");
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject itemObj = response.getJSONObject(i);
                                        String name = itemObj.getString("name");
                                        String imgB64 = itemObj.getString("image");
//                                        String glbB64 = itemObj.getString("data");
                                        String size = itemObj.getString("size");
                                        byte[] decodeImage = Base64.decode(imgB64, Base64.DEFAULT);
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length);
//                                        byte[] glbData = Base64.decode(glbB64, Base64.DEFAULT);

                                         //saveByteArrayToInternalStorage(glbData, name+".glb",bitmap, name+".png");

//                                        String stringdata =  readStorage();
//                                        String first100Characters = stringdata.substring(stringdata.length() - 100);
//                                        Log.e("requestTest", "stringdata"+first100Characters);

                                        ListData modelItem = new ListData(name, bitmap,size,false);
//                                        ListData modelItem = new ListData(name,"2",0);
                                        dataItems.add(modelItem);
                                        dataItem2.add(modelItem);
                                        if (firstGet) {
                                            nameStar.add(modelItem);
                                        }else{
                                            dataItems.get(i).star=nameStar.get(i).star;
                                            dataItem2.get(i).star=nameStar.get(i).star;
                                        }

                                        Log.e("requestTest", "pass");
                                    }
                                    firstGet=false;
                                    Log.e("requestTest", String.valueOf(response.length()));
                                    // Now: all the data items are in the array list, send it to the recycler adapter to create views.
                                    ListAdapter adapter = new ListAdapter(requireContext(), dataItem2);
                                    // assign the adapter to the recycler view
                                    binding.listView.setAdapter(adapter);
                                } catch (Exception e) {
                                    Log.d("stock", e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.getMessage() != null) {
                            Log.d("stock", error.getMessage());
                        } else {
                            Log.d("stock", "Error occurred, but error message is null");
                        }
                    }
                });
        // due to long response time, we need to add a long delay time
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static String getDataType(byte[] byteArray) {
        // Check for common data type signatures or patterns
        if (byteArray.length > 4) {
            if (byteArray[0] == 0x47 && byteArray[1] == 0x4C && byteArray[2] == 0x54) {
                return "GLB (glTF Binary)";
            }
        }

        return "Unknown"; // Default to "Unknown" if the data type is not recognized
    }

    public void saveByteArrayToFile(byte[] byteArray, String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(byteArray);
            fos.close();
            Log.d("File", "File saved at " + filePath);
        } catch (IOException e) {
            Log.e("File", "Error saving file: " + e.getMessage());
        }
    }

//    private void saveByteArrayToInternalStorage(byte[] data, String filename, Bitmap bitmap, String s) {
//        try {
//
//            File file = new File(requireContext().getFilesDir(), filename);
////            File file = new File("app/src/main/res/raw", filename);
//            Log.e("requestTest", "GLB data saved to internal storage: " + file.getAbsolutePath());
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(data);
//            fos.close();
//            Log.e("requestTest", "GLB data saved to internal storage: " + file.getAbsolutePath());
//        } catch (IOException e) {
//            Log.e("requestTest", "Error saving GLB data to internal storage: " + e.getMessage());
//        }
//    }
    private void saveByteArrayToInternalStorage(byte[] data, String modelName, Bitmap bitmap, String imageName) {
        try {
            // Save the GLB file
            File modelsDir = new File(getContext().getFilesDir(), "models");
            if (!modelsDir.exists()) modelsDir.mkdir(); // Create the directory if it doesn't exist
            File modelFile = new File(modelsDir, modelName);
            FileOutputStream fos = new FileOutputStream(modelFile);
            fos.write(data);
            fos.close();
            Log.e("requestTest", "GLB data saved to internal storage: " + modelFile.getAbsolutePath());

            // Save the PNG image
            File imagesDir = new File(getContext().getFilesDir(), "images");
            if (!imagesDir.exists()) imagesDir.mkdir(); // Create the directory if it doesn't exist
            File imageFile = new File(imagesDir, imageName);
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Compress and write the bitmap to file
            fos.close();
            Log.e("requestTest", "Image saved to internal storage: " + imageFile.getAbsolutePath());

        } catch (IOException e) {
            Log.e("requestTest", "Error saving to internal storage: " + e.getMessage());
        }
    }

    private void downloadFurnitureRequest(String name) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = "https://mobiles-2a62216dada4.herokuapp.com/model/model";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.e("requestTest", "Model: " + response.toString().substring(0, Math.min(response.toString().length(), 50)));

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String model = jsonObject.getString("model");
                            String image = jsonObject.getString("image");

                            byte[] decodeImage = Base64.decode(image, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length);
                            byte[] glbData = Base64.decode(model, Base64.DEFAULT);

                            saveByteArrayToInternalStorage(glbData,name+".glb",bitmap,name+".png");


                            String model1 = model.substring(100);
                            Log.e("requestTest", "stringdata"+model1);

                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("requestTest", "Error: " + error.getMessage());
                        // deal with error response
                    }
                })  {
            @Override
            public byte[] getBody() {

                JSONObject params = new JSONObject();
                try {
                    params.put("name", name);
                    Log.e("requestTest", "message: " + params.toString().getBytes());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        // add to queue
        queue.add(stringRequest);
    }

    private String readStorage() {
        String base64Data=null;
        try {
            File file = new File(requireContext().getFilesDir(), "creeper.glb");
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            base64Data= Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Data;
    }
    private void filterData(String searchText) {
        dataItem2 = new ArrayList<>();
        if (searchText!=null)
            filterImageView.setVisibility(View.VISIBLE);
        for (ListData item : dataItems) {
            if (item.name.toLowerCase().contains(searchText.toLowerCase())) {
                dataItem2.add(item);
            }

            for (ListData item2 : dataItem2) {
                for(ListData item3 : nameStar){
                    if(item2.name.equals(item3.name))
                        item2.star=item3.star;
                }
            }
            ListAdapter adapter = new ListAdapter(requireContext(), dataItem2);
            binding.listView.setAdapter(adapter);
        }
    }

}
