package com.example.brandcast.mobilapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Login extends AppCompatActivity implements View.OnClickListener{

    //Declaración de argumentos para el uso campos en el formulario
    private EditText emailText;
    private EditText passText;
    private Button loginBtn;
    private ProgressDialog progressDialog;
    public static final String usuario = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Referencia a los views
        emailText = findViewById(R.id.usuarioText);
        passText = findViewById(R.id.passText);
        loginBtn = findViewById(R.id.loginBtn);

        progressDialog = new ProgressDialog(this);
        loginBtn.setOnClickListener(this);
    }

    private void login() {

        //Se obtienen el email y la contraseña

        /*
         * trim() -- Se usa para eliminar espacios en blancos que el usuario agregue
         * por error al inicio o al final de la cadena de texto*/

        final String email = emailText.getText().toString().trim();
        final String pass = passText.getText().toString().trim();

        //Se verifica que los campos de texto no esten vacíos
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Debes escribir un correo electrónico", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Desbes escribir una contraseña", Toast.LENGTH_LONG).show();
        }

        Thread tr = new Thread(){
            @Override
            public void run() {
                final String request = sendPost(email, pass);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int row = objJSON(request);

                        if(row > 0){
                            Intent i = new Intent(getApplicationContext(), Default.class);
                            i.putExtra(usuario, request);
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrectos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
        tr.start();

    }

    @Override
    public void onClick(View view){
        login();
    }

    public String sendPost(String usuario, String password){

        //Se asignan parametros que serán enviados por método POST
        String parametros = "usuario="+usuario+"&password="+password;

        //Creaacion del objeto HTTPConexion
        HttpURLConnection conection = null;

        //Variable que almacena la respuesta
        String request = "";

        try{

            //Se asigna el action donde se envian los datos ingresados en el form de login
            URL url = new URL("http://mobilapp.branddcast.x10.mx/login.php");

            conection = (HttpURLConnection) url.openConnection();
            conection.setRequestMethod("POST");
            conection.setRequestProperty("Content-length",""+Integer.toString(parametros.getBytes().length));

            conection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();

            //Recibe los datos que son extraidos de la bd
            Scanner inStream = new Scanner(conection.getInputStream());
            while(inStream.hasNextLine()){
                request += (inStream.nextLine());
            }

        }catch (Exception e){}

        return request.toString();
    }

    public int objJSON(String request) {
        int flag = 0;
        try {
            JSONArray json = new JSONArray(request);
            if(json.length()>0){
                flag = 1;
            }
        } catch (Exception e) {}

        return flag;
    }
}
