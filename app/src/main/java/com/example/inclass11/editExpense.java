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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class editExpense extends AppCompatActivity {
    private Spinner sp_editCategory;
    private EditText et_editExpenseName,et_editAmount;
    private Button btn_save,btn_editCancel;
    private FirebaseFirestore db;
    private String name,selectedCategory,docID;
    final String[] items = {"Select Category","Groceries", "Invoice", "Transportation", "Shopping", "Rent", "Trips", "Utilities and Other"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        db = FirebaseFirestore.getInstance();

        et_editExpenseName = findViewById(R.id.et_editexpenseName);
        et_editAmount = findViewById(R.id.et_editamount);
        btn_save = findViewById(R.id.btn_save);
        btn_editCancel = findViewById(R.id.btn_editcancel);

        sp_editCategory = findViewById(R.id.sp_editcategory);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sp_editCategory.setAdapter(adapter);

        name = getIntent().getStringExtra("name");
        preLoadValues(name);

        sp_editCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory=items[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCategory ="Select Category";
            }
        });

        btn_editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_editExpenseName.getText().toString().equals("")&&!selectedCategory.equals("Select Category")&&Double.parseDouble(et_editAmount.getText().toString())>0) {
                    db.collection("expenses")
                            .document(docID)
                            .update("expenseName",et_editExpenseName.getText().toString(),"category", selectedCategory,"amount",Double.parseDouble(et_editAmount.getText().toString()),"date",new Date())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(editExpense.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    Intent goToMainActivity = new Intent(editExpense.this,MainActivity.class);
                                    startActivity(goToMainActivity);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editExpense.this, "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(editExpense.this, "Please fill all Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void preLoadValues(String name){
        db.collection("expenses")
                .whereEqualTo("expenseName", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docID = document.getId();
                                et_editExpenseName.setText(document.getData().get("expenseName").toString());
                                int spinnerPosition =0;
                                Log.d("DEMO", "onComplete: "+document.getData().get("category").toString());
                                for(int i=0;i<items.length;i++){
                                    if(items[i].equals(document.getData().get("category").toString())) spinnerPosition=i;
                                }
                                sp_editCategory.setSelection(spinnerPosition);
                                et_editAmount.setText(document.getData().get("amount").toString());
                            }
                        } else {
                            Log.d("DEMO", "Error getting document: ", task.getException());
                        }
                    }
                });
    }

}
