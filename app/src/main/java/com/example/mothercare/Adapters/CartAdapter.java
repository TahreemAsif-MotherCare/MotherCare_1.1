package com.example.mothercare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Medicine;
import com.example.mothercare.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context mContext;
    LayoutInflater inflater;
    public List<Medicine> MedicineList = null;
    String currentFlow;
    CartUpdate cartUpdate;

    public CartAdapter(Context context, List<Medicine> MedicineList, CartUpdate cartUpdate) {
        mContext = context;
        this.MedicineList = MedicineList;
        inflater = LayoutInflater.from(mContext);
        this.cartUpdate = cartUpdate;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return MedicineList.size();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_row, parent, false);

        return new CartAdapter.MyViewHolder(itemView);
    }

    public interface CartUpdate {
        void cartUpdate(ArrayList<Medicine> cartArrayList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.medicineName.setText(MedicineList.get(position).medicineName);
        holder.medicinePower.setText(MedicineList.get(position).medicineDose);
        holder.medicineStockQuantity.setText("Quantity: " + String.valueOf(MedicineList.get(position).cartQuantity));
        holder.medicinePrice.setText("Rs. " + String.valueOf(MedicineList.get(position).price));
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineList.remove(position);
                notifyItemChanged(position);
                cartUpdate.cartUpdate((ArrayList<Medicine>) MedicineList);
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView medicineName, medicinePower, medicineStockQuantity, medicinePrice;
        public ImageView remove;

        public MyViewHolder(View view) {
            super(view);
            medicineName = view.findViewById(R.id.medicineName);
            medicinePower = view.findViewById(R.id.medicineDose);
            medicineStockQuantity = view.findViewById(R.id.quantity);
            medicinePrice = view.findViewById(R.id.medicinePrice);
            remove = view.findViewById(R.id.removeItemFromCart);
        }
    }
}
