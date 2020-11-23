package com.example.easystock.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.easystock.R;
import com.example.easystock.utils.Constants;

public class MainActivity extends AppCompatActivity {

    private CardView btnUser;
    private CardView btnTwo;
    private CardView btnThird;
    private CardView btnFour;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        role = intent.getStringExtra("ROLE");

        init();
        btnUser.setOnClickListener(v -> {
            if (TextUtils.equals(role, Constants.ADMIN_ROLE)) {
                Intent intent1 = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(intent1);
            } else {
                Toast.makeText(MainActivity.this, "Función habilitada para administradores", Toast.LENGTH_SHORT).show();
            }
        });

        btnTwo.setOnClickListener(v -> {
            if (TextUtils.equals(role, Constants.ADMIN_ROLE)) {
                Intent intent1 = new Intent(MainActivity.this, StockActivity.class);
                startActivity(intent1);
            } else {
                //Fixme aca si es vendedor tendria que llevarlo a un fragment para modificar su perfil
                Toast.makeText(MainActivity.this, "Función habilitada para administradores", Toast.LENGTH_SHORT).show();
            }
        });

        btnThird.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, ShowStock.class);
            startActivity(intent1);
        });

/*        btnFour.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, test.class);
            startActivity(intent1);
        });*/
    }

    private void init() {
        btnUser = findViewById(R.id.firstIcon);
        btnTwo = findViewById(R.id.secondIcon);
        btnThird = findViewById(R.id.thirdIcon);
        btnFour = findViewById(R.id.fourIcon);
    }
}