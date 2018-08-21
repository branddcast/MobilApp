package com.example.brandcast.mobilapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity implements View.OnClickListener{

    //Declaración de argumentos para el uso campos en el formulario
    private EditText emailText;
    private EditText passText;
    private Button registrarBtn;
    private ProgressDialog progressDialog;
    private TextView iniciarSesion;

    //firebase object
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Inicialización del objeto
        firebaseAuth = FirebaseAuth.getInstance();

        //Referencia a los views
        emailText = findViewById(R.id.emailText);
        passText = findViewById(R.id.passText);
        registrarBtn = findViewById(R.id.registrarBtn);
        iniciarSesion = findViewById(R.id.iniciarSesion_link);

        progressDialog = new ProgressDialog(this);
        registrarBtn.setOnClickListener(this);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    private void registrar(){
        //Se obtienen el email y la contraseña

        /*
         * trim() -- Se usa para eliminar espacios en blancos que el usuario agregue
         * por error al inicio o al final de la cadena de texto*/

        String email = emailText.getText().toString().trim();
        String pass = passText.getText().toString().trim();

        //Se verifica que los campos de texto no esten vacíos
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Debes escribir un correo electrónico", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Desbes escribir una contraseña", Toast.LENGTH_LONG).show();
        }

        progressDialog.setMessage("Procesando registro...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(Tag, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);
                            Toast.makeText(signup.this, "Usuario registrado correctamente.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(signup.this, "Falló registro de usuario.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View view){
        registrar();
    }

    private void login(){
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }

}
