package mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import mobile.RegisterActivity;

public class RegisterActivity extends AppCompatActivity {
    String email;
    String password;
    String name;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private Button signUpButton;
    private ProgressBar progressBar;

    TextView registrationStatusText;

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
                    Log.e("requestTest", "message: " + email +" "+ password+" " + name);
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
        Log.e("requestTest", "message: " + email.matches(emailPattern));
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

    private void requestRegistration(String email, String password,String username) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        String url = "https://mobiles-2a62216dada4.herokuapp.com/user/register";
        registrationStatusText = findViewById(R.id.registration_status_text);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String successful = jsonObject.getString("successful");

                            if(successful.equals("true")) {
                                registrationStatusText.setText("Registration successful!");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish(); // Optional, depending on your navigation flow
                                    }
                                }, 1000); // 3000 milliseconds = 3 seconds

                            }else{
                                registrationStatusText.setText(successful);
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
                    params.put("username", username);
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

