package com.example.inclass11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpensesRVAdapter extends RecyclerView.Adapter<ExpensesRVAdapter.ViewHolder> {

    public InteractWithMainActivitry interact;
    Context ctx;

    ArrayList<Expense> expenses = new ArrayList<Expense>();

    public ExpensesRVAdapter(ArrayList<Expense> expenses, Context mainActivity) {
        this.expenses = expenses;
        this.ctx = mainActivity;
    }

    @NonNull
    @Override
    public ExpensesRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout rv_layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.rvitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesRVAdapter.ViewHolder holder, int position) {
        holder.selectedposition = position;
        holder.tv_expenseitemName.setText(expenses.get(position).getExpenseName());
        holder.tv_expenseItemAmount.setText("$ "+String.valueOf(expenses.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_expenseitemName, tv_expenseItemAmount;
        int selectedposition;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_expenseitemName = itemView.findViewById(R.id.tv_rvItemExpenseName);
            tv_expenseItemAmount = itemView.findViewById(R.id.tv_rvItemExpenseAmount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interact = (InteractWithMainActivitry) ctx;
                    interact.selecteditem(selectedposition);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    interact = (InteractWithMainActivitry) ctx;
                    interact.deleteItem(selectedposition);
                    return true;
                }
            });
        }


    }

    public interface InteractWithMainActivitry{
        void deleteItem(int position);
        void selecteditem(int position);
    }
}
