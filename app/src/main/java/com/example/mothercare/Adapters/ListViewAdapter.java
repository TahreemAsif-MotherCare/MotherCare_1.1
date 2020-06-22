package com.example.mothercare.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mothercare.Models.Medicine;
import com.example.mothercare.R;
import com.example.mothercare.Views.Activities.CartActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.MyViewHolder> {

    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<Medicine> MedicineList = null;
    private ArrayList<Medicine> arraylist;
    String currentFlow;

    public ListViewAdapter(Context context, List<Medicine> MedicineList, String currentFlow) {
        mContext = context;
        this.MedicineList = MedicineList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Medicine>();
        this.arraylist.addAll(MedicineList);
        this.currentFlow = currentFlow;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return MedicineList.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        MedicineList.clear();
        if (charText.length() == 0) {
            MedicineList.addAll(arraylist);
        } else {
            for (Medicine wp : arraylist) {
                if (wp.medicineName.toLowerCase(Locale.getDefault()).contains(charText)) {
                    MedicineList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_medicine_listview, parent, false);

        return new ListViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final int[] count = {MedicineList.get(position).getCount()};
        final int stockAmount = MedicineList.get(position).stockQuantity;
        final Medicine medicine = MedicineList.get(position);
        holder.medicineName.setText(medicine.medicineName);
        holder.medicinePower.setText("Dose: " + medicine.medicineDose);
        if (medicine.stockQuantity != 0 || medicine.stockQuantity < 0) {
            holder.medicineStockQuantity.setText(Integer.toString(medicine.stockQuantity) + " available.");
        } else {
            holder.medicineStockQuantity.setText("Out of stock ");
            holder.addToCartIV.setVisibility(View.GONE);
            holder.increase.setVisibility(View.GONE);
            holder.decrease.setVisibility(View.GONE);
            holder.quantityET.setVisibility(View.GONE);
        }
        holder.medicinePrice.setText("Price: " + medicine.price + " Rs.");
        holder.pharmacyName.setText(medicine.pharmacyName);
        if (currentFlow.equals("Patient")) {
            holder.addToCart.setVisibility(View.VISIBLE);
            holder.manageLayout.setVisibility(View.GONE);
        } else {
            holder.addToCart.setVisibility(View.GONE);
            holder.manageLayout.setVisibility(View.VISIBLE);
        }

        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("Medicine").child(medicine.medicineID);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.medicinePicture.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });

        holder.addToCartIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.quantityET.getText().toString().equals("0")) {
                    if (count[0] > medicine.stockQuantity) {
                        Toast.makeText(mContext, "Your ordered amount is greater than the available stock", Toast.LENGTH_SHORT).show();
                        holder.quantityET.setText("1");
                    } else {
                        Medicine medicineToUpload = new Medicine(MedicineList.get(position).medicineID, MedicineList.get(position).medicineName, MedicineList.get(position).medicineDose,
                                MedicineList.get(position).pharmacyName, MedicineList.get(position).price, MedicineList.get(position).stockQuantity,
                                MedicineList.get(position).pharmacyID);
                        medicineToUpload.setCartQuantity(Integer.parseInt(holder.quantityET.getText().toString()));
                        medicineToUpload.setCount(count[0]);
                        CartActivity.cartArrayList.add(medicineToUpload);
                        holder.quantityET.setText("1");
                        Toast.makeText(mContext, "Item Added to the cart", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Please select medicine quantity!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.deleteMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Medicines")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(MedicineList.get(position).medicineID);
                reference.removeValue();
                notifyItemChanged(position);
                Toast.makeText(mContext, "Medicine Removed", Toast.LENGTH_SHORT).show();
            }
        });

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count[0] >= medicine.stockQuantity) {
                    Toast.makeText(mContext, "Your ordered amount is greater than the available stock", Toast.LENGTH_SHORT).show();
                } else {
                    count[0]++;
                    medicine.setCount(count[0]);
                    holder.quantityET.setText(String.valueOf(count[0]));
                }
            }
        });

        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[0]--;
                if (count[0] != 0) {
                    MedicineList.get(position).setCount(count[0]);
                    holder.quantityET.setText(String.valueOf(count[0]));
                } else count[0]++;
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
                /*if (!s.toString().equals("")) {
                    try {
                        if (Integer.parseInt(s.toString()) > stockAmount) {
                            Toast.makeText(mContext, "Your ordered amount is greater than the available stock", Toast.LENGTH_SHORT).show();
                        } else {
                            MedicineList.get(position).setCount(Integer.parseInt(s.toString()));
                            count[0] = MedicineList.get(position).getCount();
                        }
                    } catch (IndexOutOfBoundsException e) {
                        Toast.makeText(mContext, "Your ordered amount is greater than the available stock", Toast.LENGTH_SHORT).show();
                    }
                }*/
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView medicineName, medicinePower, medicineStockQuantity, medicinePrice, pharmacyName;
        LinearLayout manageLayout, addToCart;
        ImageView addToCartIV, medicinePicture, decrease, increase;
        EditText quantityET;
        ImageView deleteMedicine;

        public MyViewHolder(View view) {
            super(view);
            medicineName = view.findViewById(R.id.medicineName);
            medicinePower = view.findViewById(R.id.power);
            medicineStockQuantity = view.findViewById(R.id.stockQuantity);
            pharmacyName = view.findViewById(R.id.pharmacyName);
            medicinePrice = view.findViewById(R.id.price);
            manageLayout = view.findViewById(R.id.manageLayout);
            addToCart = view.findViewById(R.id.addToCart);
            addToCartIV = view.findViewById(R.id.addToCartButton);
            deleteMedicine = view.findViewById(R.id.deleteMedicine);
            medicinePicture = view.findViewById(R.id.medicinePicture);
            decrease = view.findViewById(R.id.decraseQuantityBtn);
            increase = view.findViewById(R.id.increaseQuantityBtn);
            quantityET = view.findViewById(R.id.et_quantity);
        }
    }
}
