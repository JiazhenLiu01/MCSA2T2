package mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

import io.github.sceneview.sample.arcursorplacement.R;
import mobile.MainActivity;

public class RegisterActivity extends AppCompatActivity {
    String email;
    String password;
    String name;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private Button signUpButton;
    private ProgressBar progressBar;

    private String params = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_home);

        emailEditText = findViewById(R.id.register_email_text);
        passwordEditText = findViewById(R.id.register_password_text);
        nameEditText = findViewById(R.id.register_name_text);
        signUpButton = findViewById(R.id.bt_register);
        progressBar = findViewById(R.id.progressbar);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                name = nameEditText.getText().toString();

                if (isValid(email, password, name)) {
                    signUp(email, password, name);
                } else {
                    Toast.makeText(RegisterActivity.this, "Invalid details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValid(String email, String password, String name) {
        return isValidEmail(email) && isValidPassword(password) && isValidName(name);
    }

    private boolean isValidEmail(String email) {
        // Use a more restrictive regex pattern for email validation
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return !email.isEmpty() && email.matches(emailPattern);
    }

    private boolean isValidPassword(String password) {
        // Add your password validation logic here
        // For example, check if it has a minimum length or specific requirements
        return !password.isEmpty() && password.length() >= 6; // Minimum length of 8 characters
    }

    private boolean isValidName(String name) {
        // Add your name validation logic here
        // For example, check if it's not empty and doesn't contain special characters
        return !name.isEmpty() && name.matches("[a-zA-Z]+");
    }

    private void signUp(String email, String password, String name) {
        progressBar.setVisibility(View.VISIBLE);

        JSONObject paramsObject = new JSONObject();
        try {
            paramsObject.put("email", email);
            paramsObject.put("password", password);
            paramsObject.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params = paramsObject.toString();
        SignUpTask task = new SignUpTask();
        task.execute();
    }

    private class SignUpTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            requestRegistration(email,password,name);
            return null;
        }
    }

    private void requestRegistration(String email, String password, String name) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://mobiles-2a62216dada4.herokuapp.com/user/register"; // Replace with your actual backend URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("requestTest", "Registration Response: " + response);
//
//                        // Handle the registration response as needed
//                        progressBar.setVisibility(View.INVISIBLE);
//                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                        finish(); // Close the SignUpActivity after successful registration
//                    }
//                },
                    @Override
                    public void onResponse(String response) {
                        // response


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String login = jsonObject.getString("login");
                            String password= jsonObject.getString("password");
                            String username= jsonObject.getString("username");

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);

                        } catch (JSONException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("requestTest", "Registration Error: " + error.getMessage());
                        progressBar.setVisibility(View.INVISIBLE);
                        // Handle registration error
                        Toast.makeText(RegisterActivity.this, "Registration Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
//            @Override
//            public byte[] getBody() {
//                try {
//                    return params.getBytes("utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//        };
//
//        queue.add(stringRequest);
//    }
//}
        @Override
        public byte[] getBody() {

            JSONObject params = new JSONObject();
            try {
                params.put("email", email);
                params.put("password", password);
                params.put("name", name);
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

