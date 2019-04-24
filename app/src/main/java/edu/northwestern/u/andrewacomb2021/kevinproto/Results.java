package edu.northwestern.u.andrewacomb2021.kevinproto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Results extends AppCompatActivity {

    TextView txt_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        txt_name = (TextView) findViewById(R.id.txt_name);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        txt_name.setText(name);



        Button btnReturn = (Button)findViewById(R.id.btnReturn);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Results.this, Preview.class);
                startActivity(intent);
            }
        });

    }
}
