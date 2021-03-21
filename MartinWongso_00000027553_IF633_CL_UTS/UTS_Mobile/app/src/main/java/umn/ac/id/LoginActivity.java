package umn.ac.id;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText username, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Halaman Login");
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){

                    //Toast.makeText(v.getContext(), username.getText().toString() + password.getText().toString(), Toast.LENGTH_LONG).show();
                    //String user = username.getText().toString();
                    //String pass = password.getText().toString();

                    if(username.getText().toString().equals("uasmobile") && password.getText().toString().equals("uasmobilegenap")){
                        Intent musicListIntent = new Intent(LoginActivity.this, ViewMusicActivity.class);
                        startActivity(musicListIntent);
                    }else {
                        Toast.makeText(v.getContext(), "Login Gagal", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
