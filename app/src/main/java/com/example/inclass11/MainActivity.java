package com.example.inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ExpensesRVAdapter.InteractWithMainActivitry{

    private ImageView iv_addExpense;
    private FirebaseFirestore db;
    private TextView tv_noExpense;
    private String TAG = "DEMO", docIDToBeDeleted;
    private RecyclerView rv_recyclerview;
    private RecyclerView.Adapter rv_adapter;
    private RecyclerView.LayoutManager rv_layoutManager;
    private ArrayList<Expense> listOfExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_addExpense = findViewById(R.id.iv_addExpense);
        rv_recyclerview = findViewById(R.id.rv_listOfExpenses);
        tv_noExpense = findViewById(R.id.tv_noExpense);
        tv_noExpense.setVisibility(TextView.INVISIBLE);
        rv_recyclerview.setVisibility(RecyclerView.INVISIBLE);

        db = FirebaseFirestore.getInstance();
        getAllExpenses();

        iv_addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToAddExpense = new Intent(MainActivity.this,AddExpense.class);
                startActivity(goToAddExpense);
            }
        });
    }

    public void getDocID(String name){
        db.collection("expenses")
                .whereEqualTo("expenseName", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docIDToBeDeleted = document.getId();
                                deleteDocument(docIDToBeDeleted);
                            }
                        } else {
                            Log.d("DEMO", "Error getting document: ", task.getException());
                        }
                    }
                });
    }

    public void deleteDocument(String docIDToBeDeleted){
        db.collection("expenses")
                .document(docIDToBeDeleted)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void deleteItem(final int position) {

        getDocID(listOfExpenses.get(position).getExpenseName());
        listOfExpenses.remove(position);
        if(listOfExpenses.size()>0){
            rv_adapter.notifyDataSetChanged();
            tv_noExpense.setVisibility(TextView.INVISIBLE);
            rv_recyclerview.setVisibility(RecyclerView.VISIBLE);
        }else{
            tv_noExpense.setVisibility(TextView.VISIBLE);
            rv_recyclerview.setVisibility(RecyclerView.INVISIBLE);
        }
        Toast.makeText(MainActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void selecteditem(int position) {
        Intent goToShowExpense = new Intent(MainActivity.this,showExpense.class);
        goToShowExpense.putExtra("EXPENSENAME",listOfExpenses.get(position).getExpenseName());
        startActivity(goToShowExpense);
    }

    public void getAllExpenses(){
        db.collection("expenses")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        listOfExpenses = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            Expense expense = new Expense(documentSnapshot.getString("expenseName"),documentSnapshot.getString("category"),documentSnapshot.getDouble("amount"),documentSnapshot.getDate("date"));
                            listOfExpenses.add(expense);
                        }
                        if(listOfExpenses.size()>0){
                            tv_noExpense.setVisibility(TextView.INVISIBLE);
                            rv_recyclerview.setVisibility(RecyclerView.VISIBLE);

                            rv_recyclerview.setHasFixedSize(true);
                            rv_layoutManager = new LinearLayoutManager(MainActivity.this);
                            rv_recyclerview.setLayoutManager(rv_layoutManager);
                            rv_adapter = new ExpensesRVAdapter(listOfExpenses, MainActivity.this);
                            rv_recyclerview.setAdapter(rv_adapter);

                        }else{
                            tv_noExpense.setVisibility(TextView.VISIBLE);
                            rv_recyclerview.setVisibility(RecyclerView.INVISIBLE);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("DEMO", "Failed to load the document: "+e.getMessage() );
                    }
                });
    }
}
