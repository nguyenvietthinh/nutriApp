package com.tma.techday.foodnutrientfact.viewholder;


import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.model.Order;
import com.tma.techday.foodnutrientfact.model.WeightUnit;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Create Adapter and Holder for cart
 */
class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView foodNameItemView, calAmountItemView;
    public EditText foodWeightText;
    public ImageView cartItemCount;
    Spinner spinner;





    public void setFoodNameItemView(TextView foodNameItemView) {
        this.foodNameItemView = foodNameItemView;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        foodNameItemView = itemView.findViewById(R.id.cart_item_food_name);
        calAmountItemView = itemView.findViewById(R.id.cart_item_cal_amount);
        spinner =  itemView.findViewById(R.id.planets_spinner);
        foodWeightText = itemView.findViewById(R.id.txtFoodWeight);


        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            contextMenu.add(0,0,getAdapterPosition(),"Delete");
    }
}
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private List<Order> orderList = new ArrayList<>();
    private Context mcontext;
    private ArrayAdapter<CharSequence> adapter;

    public CartAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.mcontext = context;
        adapter = ArrayAdapter.createFromResource(mcontext,
                R.array.weight_unit_array, android.R.layout.simple_spinner_item);//Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Specify the layout to use when the list of choices appears
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View itemView = layoutInflater.inflate(R.layout.cart_item_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String unit = holder.spinner.getSelectedItem().toString();
                WeightUnit weightUnit = WeightUnit.fromDescription(unit);
                //TODO: log weightUnit amount to file
                Log.i("WeightUnit", weightUnit.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        NumberFormat numberFormat = new DecimalFormat("0.00");
        double calAmount = (orderList.get(position).getFoodWeight())*(orderList.get(position).getCalorieAmount())/100.0;
        holder.calAmountItemView.setText(numberFormat.format(calAmount));
        holder.foodNameItemView.setText(orderList.get(position).getFoodName());
        holder.foodWeightText.setText(numberFormat.format(orderList.get(position).getFoodWeight()));
        holder.spinner.setAdapter(adapter); // Apply the adapter to the spinner

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

}
