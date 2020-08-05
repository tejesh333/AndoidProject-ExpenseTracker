package com.example.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class showExpense extends AppCompatActivity {

    private TextView tv_dispName,tv_dispCategory,tv_dispAmount,tv_dispDate;
    private Button btn_editExpense,btn_close;
    private FirebaseFirestore db;
    private String expenseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        tv_dispName = findViewById(R.id.tv_dispName);
        tv_dispCategory = findViewById(R.id.tv_dispCategory);
        tv_dispAmount = findViewById(R.id.tv_dispAmount);
        tv_dispDate = findViewById(R.id.tv_dispDate);
        btn_editExpense = findViewById(R.id.btn_editExpense);
        btn_close = findViewById(R.id.btn_close);
        expenseName = getIntent().getStringExtra("EXPENSENAME");

        db = FirebaseFirestore.getInstance();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goback = new Intent(showExpense.this,MainActivity.class);
                startActivity(goback);
            }
        });

        btn_editExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoeditExpense = new Intent(showExpense.this,editExpense.class);
                gotoeditExpense.putExtra("name",expenseName);
                startActivity(gotoeditExpense);
            }
        });

        db.collection("expenses")
                .whereEqualTo("expenseName", expenseName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                tv_dispName.setText(document.getData().get("expenseName").toString());
                                tv_dispCategory.setText(document.getData().get("category").toString());
                                tv_dispAmount.setText("$ "+document.getData().get("amount").toString());

                                Date d= new Date();
                                String pattern = "MM/dd/yyyy";
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                String date= simpleDateFormat.format(d);
                                tv_dispDate.setText(date);
                            }
                        } else {
                            Log.d("DEMO", "Error getting document: ", task.getException());
                        }
                    }
                });

    }

}
