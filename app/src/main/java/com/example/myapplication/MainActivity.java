package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    ProgressBar progressBar;
    String id_user, username_db, nama, alamat,email;
    AnimationDrawable animationDrawable;
    Button btnlogin,btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        btnlogin = findViewById(R.id.bt_signin);
        btnRegister = findViewById(R.id.bt_sign_signup);
        Log.d("LOGIN","LOGIN");
        /*
        ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.cl);
        animationDrawable =(AnimationDrawable) cl.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);
        */
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Drawer.class));
        }

        editTextUsername = (EditText) findViewById(R.id.username_editTxt_signup);
        editTextPassword = (EditText) findViewById(R.id.pass_editTxt_signup);

        //if user presses on login
        //calling the method login
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("LOGIN","LOGIN");
                userLogin();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("kode_item_rcv", "Register");
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

    }

    private void userLogin() {
        //first getting the values
        final String[] username = {editTextUsername.getText().toString()};
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username[0])) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        //HttpsTrustManager.allowAllSSL();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerUrl.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        Log.d("login_respose",""+response);
                        try {
                            //converting response to json object
                            //JSONObject obj = new JSONObject(response);
                            //if(!obj.toString().equals("[]") || !obj.toString().equals("Gagal") )
                            if(!response.equals("[]")){

                                JSONArray obj = new JSONArray(response);
                                Log.d("login_arrya",""+obj.toString());
                                Log.d("login_arrya",""+response);

                                for(int i=0; i < obj.length(); i++) {
                                    JSONObject jsonobject = obj.getJSONObject(i);
                                    id_user       = jsonobject.getString("id");
                                    username_db = jsonobject.getString("username");
                                    nama = jsonobject.getString("nama");
                                    alamat = jsonobject.getString("alamat");
                                    email = jsonobject.getString("email");
                                    //Log.d("Error WH ID","Error WHID"+wh_id);
                                }
                                User user =new User(id_user, username_db,nama,alamat,email);
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), Drawer.class));
                            }else{
                                Log.d("error_obj_array", "ERROR ARRAY NULL");
                                alert("Gagal Login","Invalid username / password");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("error_login", "error Login" + response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        alert("Gagal Login"," error "+error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username[0]);
                params.put("password", password);
                Log.d("login_response", "onResponse: "+ServerUrl.URL_LOGIN);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void alert(String Judul, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(Judul)
                .setMessage(message)
                .show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}