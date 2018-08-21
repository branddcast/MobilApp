package com.example.brandcast.mobilapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Default extends AppCompatActivity {
    private TextView usuarioTxt;
    String usuario_data;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        usuarioTxt = (TextView) findViewById(R.id.usuario_txt);

        Intent intent = getIntent();
        usuario_data = intent.getStringExtra(Login.usuario);
        try {
            //JSONArray json = new JSONArray(usuario_data);
            JSONObject json_ = new JSONObject(usuario_data);
            String nombre = json_.getString("Nombre");
            String apellido_p = json_.getString("Apellido_paterno");
            usuarioTxt.setText(" "+nombre+" "+apellido_p);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
