package com.example.mothercare.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final int[] count = {MedicineList.get(position).getCount()};
        holder.medicineName.setText(MedicineList.get(position).medicineName);
        holder.medicinePower.setText(MedicineList.get(position).medicineDose);
        holder.quantityET.setText(Integer.toString(MedicineList.get(position).getCartQuantity()));
        holder.medicinePrice.setText("Rs. " + String.valueOf(MedicineList.get(position).price));
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineList.remove(position);
                notifyItemChanged(position);
                cartUpdate.cartUpdate((ArrayList<Medicine>) MedicineList);
            }
        });
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]--;
                if (count[0] != 0) {
                    MedicineList.get(position).setCount(count[0]);
                    holder.quantityET.setText(String.valueOf(count[0]));
                    cartUpdate.cartUpdate((ArrayList<Medicine>) MedicineList);
                } else count[0]++;
            }
        });
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]++;
                if (count[0] > MedicineList.get(position).stockQuantity) {
                    Toast.makeText(mContext, "Your ordered amount is greater than the available stock", Toast.LENGTH_SHORT).show();
                    holder.quantityET.setText("1");
                } else {
                    MedicineList.get(position).setCount(count[0]);
                    holder.quantityET.setText(String.valueOf(count[0]));
                    cartUpdate.cartUpdate((ArrayList<Medicine>) MedicineList);
                }
            }
        });

        holder.quantityET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    if (Integer.parseInt(s.toString()) > MedicineList.get(position).stockQuantity) {
                        Toast.makeText(mContext, "Your ordered amount is greater than the available stock", Toast.LENGTH_SHORT).show();
                        holder.quantityET.setText("1");
                    } else {
                        MedicineList.get(position).setCount(Integer.parseInt(s.toString()));
                        count[0] = MedicineList.get(position).getCount();
                        cartUpdate.cartUpdate((ArrayList<Medicine>) MedicineList);
                    }
                }
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView medicineName, medicinePower, medicineStockQuantity, medicinePrice;
        public ImageView remove, decrease, increase;
        public EditText quantityET;

        public MyViewHolder(View view) {
            super(view);
            medicineName = view.findViewById(R.id.medicineName);
            medicinePower = view.findViewById(R.id.medicineDose);
//            medicineStockQuantity = view.findViewById(R.id.quantity);
            medicinePrice = view.findViewById(R.id.medicinePrice);
            remove = view.findViewById(R.id.removeItemFromCart);
            decrease = view.findViewById(R.id.decraseQuantityBtn);
            increase = view.findViewById(R.id.increaseQuantityBtn);
            quantityET = view.findViewById(R.id.et_quantity);
        }
    }
}
