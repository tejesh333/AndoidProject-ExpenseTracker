package com.example.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class AddExpense extends AppCompatActivity {

    private Spinner dropdown;
    private EditText et_expenseName,et_amount;
    private Button btn_addExpense,btn_cancel;
    private FirebaseFirestore db;
    private String selectedCategory;
    final String[] items = {"Select Category","Groceries", "Invoice", "Transportation", "Shopping", "Rent", "Trips", "Utilities and Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        db = FirebaseFirestore.getInstance();

        et_expenseName = findViewById(R.id.et_expenseName);
        et_amount = findViewById(R.id.et_amount);
        btn_addExpense = findViewById(R.id.btn_addExpense);
        btn_cancel = findViewById(R.id.btn_cancel);

        dropdown = findViewById(R.id.sp_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory=items[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCategory ="Select Category";
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_expenseName.getText().toString().equals("")&&!selectedCategory.equals("Select Category")&&Double.parseDouble(et_amount.getText().toString())>0){
                    Expense newExpense = new Expense(et_expenseName.getText().toString(),selectedCategory,Double.parseDouble(et_amount.getText().toString()),new Date());
                    HashMap<String, Object> toSave = newExpense.createHashMap();

                    db.collection("expenses")
                            .add(toSave)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(AddExpense.this, "Expense Added Successfully", Toast.LENGTH_SHORT).show();
                                    Intent goToMainActivity = new Intent(AddExpense.this,MainActivity.class);
                                    startActivity(goToMainActivity);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddExpense.this, "Expense adding failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    Toast.makeText(AddExpense.this, "Please fill all Fields", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
