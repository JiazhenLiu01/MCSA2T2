package mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import android.app.ProgressDialog; // Add this import statement

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.github.sceneview.sample.arcursorplacement.Activity;
import io.github.sceneview.sample.arcursorplacement.R;
import mobile.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;

    String email;

    String login;

    String username;

    TextView loginStatusText;

    String password;
    private TextView forgotPasswordTextView;
    private ProgressDialog mDialog; // Add this line for ProgressDialog

    private String params = ""; //定义参数字符串

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_home);

        emailEditText = findViewById(R.id.edit_email);
        passwordEditText = findViewById(R.id.edit_senha);
        loginButton = findViewById(R.id.bt_entrar);
        signUpTextView = findViewById(R.id.bt_signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                // Check if email and password are valid
                if (isValid(email, password)) {
                    login(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    private boolean isValid(String email, String password) {
        // Add your authentication logic here
        // For example, you can check if email and password meet certain criteria
        // For simplicity, this example checks if both fields are non-empty
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);
    }

    private void login(String username, String password) {
        mDialog = new ProgressDialog(LoginActivity.this);
        mDialog.setTitle("Login...");
        mDialog.setMessage("Waiting...");
        mDialog.setCancelable(false);
        mDialog.show();

        JSONObject paramsObject = new JSONObject();
        try {
            paramsObject.put("account", username); // Assuming username is the email
            paramsObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        params = paramsObject.toString();

        LoginTask task = new LoginTask();
        task.execute();
    }

    public class LoginTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            requestModel(email,password);
            return null;
        }
    }

//    private void requestModel(String address) {
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//        String url = "https://mobiles-2a62216dada4.herokuapp.com/user/login";
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.e("requestTest", "Model: " + response.substring(0, Math.min(response.length(), 50)));
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
////                            String model = jsonObject.getString("model");
//                            Log.e("requestTest", "Model: " + jsonObject.toString());
//                            // Handle the model response as needed
//                        } catch (JSONException ex) {
//                            throw new RuntimeException(ex);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("requestTest", "Error: " + error.getMessage());
//                        // deal with error response
//                    }
//                }) {
//            @Override
//            public byte[] getBody() {
//                return params.getBytes();
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//        };
//
//        // add to queue
//        queue.add(stringRequest);
//    }

    private void requestModel(String email, String password) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        String url = "https://mobiles-2a62216dada4.herokuapp.com/user/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        loginStatusText = findViewById(R.id.login_status_text);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            login = jsonObject.getString("login");
                            username= jsonObject.getString("username");
                            Log.e("requestTest", "Error: " + response);
                            if(login.equals("true")){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                intent.putExtra("USERNAME_KEY", username);
                                intent.putExtra("EMAIL_KEY", email);
                                startActivity(intent);
                            }else{
                                loginStatusText.setText(login);
                            }

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
                    params.put("email", email);
                    params.put("password", password);
                    Log.e("requestTest", "message: " + params);
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
}



