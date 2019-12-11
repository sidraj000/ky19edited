package org.kashiyatra.ky19;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    private final int RC_SIGN_IN = 1234;

    private Button reg_later_button, reg_now_button, ky_login_button, submit_button;
    private EditText ky_no_input;
    private com.google.android.gms.common.SignInButton g_login_button;
    private CallbackManager callbackManager;
    private ProgressBar pb;
    private int index = 0;
    private String ky_no_text, contact_no;
    private GoogleSignInClient mGoogleSignInClient;

    static void animateElement(View element, int duration, int start_pos, int final_pos) {
        ObjectAnimator translateElement = ObjectAnimator.ofFloat(element, "translationX", start_pos, final_pos);
        translateElement.setInterpolator(new DecelerateInterpolator());
        translateElement.setDuration(duration);
        translateElement.start();
    }

    static void animateElement(View element, int duration, int pos) {
        ObjectAnimator translateElement = ObjectAnimator.ofFloat(element, "translationX", pos);
        translateElement.setInterpolator(new DecelerateInterpolator());
        translateElement.setDuration(duration);
        translateElement.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Result Code", String.valueOf(resultCode));
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();///
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ky_login_button = findViewById(R.id.ky_login_button);
        g_login_button = findViewById(R.id.google_login_button);
        reg_later_button = findViewById(R.id.register_later);
        reg_now_button = findViewById(R.id.register_now);
        submit_button = findViewById(R.id.login_submit);
        ky_no_input = findViewById(R.id.ky_number_input);
        pb = findViewById(R.id.progress);
        pb.setVisibility(View.GONE);

        g_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        ky_login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                animateElement(ky_login_button, 300, -1000);
                animateElement(g_login_button, 300, -1000);
                ky_login_button.setVisibility(View.GONE);
                g_login_button.setVisibility(View.GONE);

                ky_no_input.setVisibility(View.VISIBLE);
                animateElement(ky_no_input, 300, 1000, 0);
                submit_button.setVisibility(View.VISIBLE);
                animateElement(submit_button, 300, 1000, 0);

                ky_no_input.setHint("KY0001 or KYCA0001");
                index++;
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (index == 1) {

                    ky_no_text = ky_no_input.getText().toString().toUpperCase();
                    Pattern pattern = Pattern.compile("KY[0-9]{4}");
                    Pattern patternCa = Pattern.compile("KYCA[0-9]{4}");

                    if (!pattern.matcher(ky_no_text).matches() && !patternCa.matcher(ky_no_text).matches()) {
                        Toast.makeText(LoginActivity.this, "Invalid KY Number!", Toast.LENGTH_SHORT).show();
                    } else {
                        animateElement(ky_no_input, 300, -1000);
                        ky_no_input.setText(null);
                        ky_no_input.setHint("Contact No.");
                        ky_no_input.setInputType(InputType.TYPE_CLASS_PHONE);
                        animateElement(ky_no_input, 300, 1000, 0);
                        index = index + 1;
                    }
                } else {

                    contact_no = ky_no_input.getText().toString();
                    Pattern pattern = Pattern.compile("[0-9]{10}");

                    if (!pattern.matcher(contact_no).matches()) {
                        Toast.makeText(LoginActivity.this, "Not a Valid Contact Number!", Toast.LENGTH_SHORT).show();
                    } else {

                        new getDetails(ky_no_text, contact_no).execute();
                    }
                }
            }
        });

        reg_later_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startHomeActivity();
            }
        });
        reg_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            String email = account.getEmail();
            new getDetailsUsingEmail(email).execute();
            mGoogleSignInClient.signOut();
            mGoogleSignInClient.revokeAccess();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            if (e.getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_FAILED) {
                Toast.makeText(getApplicationContext(), "Please check internet connectivity", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error code " + String.valueOf(e.getStatusCode()), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        LoginActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.pull_right, R.anim.push_left);
        LoginActivity.this.finish();
    }


    @Override
    public void onBackPressed() {
        if (index == 2) {
            index--;
            animateElement(ky_no_input, 300, 0, 1000);
            ky_no_input.setText(ky_no_text);
            ky_no_input.setHint("MI No. (mi-abc-123)");
            ky_no_input.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            animateElement(ky_no_input, 300, -2000, 0);
        } else if (index == 1) {
            index--;
            animateElement(ky_no_input, 300, 2000);
            animateElement(submit_button, 300, 2000);
            ky_no_input.setVisibility(View.GONE);
            submit_button.setVisibility(View.GONE);

            ky_login_button.setVisibility(View.VISIBLE);
            g_login_button.setVisibility(View.VISIBLE);

            animateElement(ky_login_button, 300, 0);
            animateElement(g_login_button, 300, 0);
        } else {
            super.onBackPressed();
        }
    }

    private class getDetails extends AsyncTask<Void, Void, JSONObject> {

        private String mKyId, mMobileNumber;

        getDetails(String kyId, String mobileNumber) {
            mKyId = kyId;
            mMobileNumber = mobileNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ky_id", mKyId);//changed to above secret
                jsonObject.put("secret", getResources().getString(R.string.app_secret));
                jsonObject.put("mobile_number", mMobileNumber);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://kashiyatra.herokuapp.com/api/mobile/login/")
                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString()))
                        .build();
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                Log.e("json", jsonData);
                Log.e("status_code", String.valueOf(response.code()));
                JSONObject jsonResponse = new JSONObject();

                try {
                    jsonResponse = new JSONObject(jsonData);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                jsonResponse.put("status_code", response.code());

                return jsonResponse;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }


        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            pb.setVisibility(View.GONE);
            int statusCode;
            try {
                statusCode = result.getInt("status_code");
            } catch (JSONException je) {
                statusCode = -1;
            }
            if (statusCode == 200) {
                try {
                    String name = result.getString("full_name");
                    String email = result.getString("email");
                    String college = result.getString("college");
                    String proPicUrl = result.getString("profile_picture");
                    String kyId;
                    try {
                        kyId = result.getString("ky_id");
                    } catch (JSONException je) {
                        kyId = result.getString("ca_id");
                    }

                    SharedPreferences prefs = getSharedPreferences(SplashActivity.storeUserDetails, MODE_PRIVATE);
                    final SharedPreferences.Editor prefEditor = prefs.edit();
                    prefEditor.putString("fullName", name);
                    prefEditor.putString("email", email);
                    prefEditor.putString("ky_id", kyId);
                    prefEditor.putString("college", college);
                    prefEditor.putString("profilePic", proPicUrl);
                    prefEditor.putBoolean("isLoggedIn", true);
                    prefEditor.commit();
                    startHomeActivity();
                } catch (JSONException je) {
                    je.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Invalid Server Response", Toast.LENGTH_SHORT).show();
                }

            } else if (statusCode == 403) {
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class getDetailsUsingEmail extends AsyncTask<Void, Void, JSONObject> {

        private String mEmail;

        getDetailsUsingEmail(String email) {
            mEmail = email;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("secret", getResources().getString(R.string.app_secret));
                jsonObject.put("email", mEmail);

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://kashiyatra.herokuapp.com/api/mobile/login/social/")
                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString()))
                        .build();
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                Log.e("json", jsonData);
                Log.e("status_code", String.valueOf(response.code()));
                JSONObject jsonResponse = new JSONObject();

                try {
                    jsonResponse = new JSONObject(jsonData);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("response_error", e.toString());
                }

                jsonResponse.put("status_code", response.code());

                return jsonResponse;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ResponseError", e.toString());

            }
            return new JSONObject();
        }


        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            pb.setVisibility(View.GONE);
            int statusCode;
            try {
                statusCode = result.getInt("status_code");
            } catch (JSONException je) {
                statusCode = -1;
            }
            if (statusCode == 200) {
                try {
                    String name = result.getString("full_name");
                    String email = result.getString("email");
                    String college = result.getString("college");
                    String proPicUrl = result.getString("profile_picture");
                    String kyId;
                    try {
                        kyId = result.getString("ky_id");
                    } catch (JSONException je) {
                        kyId = result.getString("ca_id");
                    }


                    SharedPreferences prefs = getSharedPreferences(SplashActivity.storeUserDetails, MODE_PRIVATE);
                    final SharedPreferences.Editor prefEditor = prefs.edit();
                    prefEditor.putString("fullName", name);
                    prefEditor.putString("email", email);
                    prefEditor.putString("ky_id", kyId);
                    prefEditor.putString("college", college);
                    prefEditor.putString("profilePic", proPicUrl);
                    prefEditor.putBoolean("isLoggedIn", true);
                    prefEditor.commit();
                    startHomeActivity();
                } catch (JSONException je) {
                    je.printStackTrace();
                }

            } else if (statusCode == 403) {
                Toast.makeText(LoginActivity.this, "Looks like you have not registered for KY. Please register first.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.putExtra("email", mEmail);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}