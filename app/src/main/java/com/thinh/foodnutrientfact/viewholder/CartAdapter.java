package com.thinh.foodnutrientfact.viewholder;


import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.thinh.foodnutrientfact.R;
import com.thinh.foodnutrientfact.model.Order;
import com.thinh.foodnutrientfact.service.ItemClickListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Create Adapter and Holder for cart
 */
class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView foodNameItemView, calAmountItemView;
    public ImageView cartItemCount;
    private ItemClickListener itemClickListener;

    public void setFoodNameItemView(TextView foodNameItemView) {
        this.foodNameItemView = foodNameItemView;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        foodNameItemView = itemView.findViewById(R.id.cart_item_food_name);
        calAmountItemView = itemView.findViewById(R.id.cart_item_cal_amount);
        cartItemCount = itemView.findViewById(R.id.cart_item_count);
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
    private Context context;

    public CartAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.cart_item_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(""+orderList.get(position).getFoodWeight(), Color.RED);
        holder.cartItemCount.setImageDrawable(textDrawable);

        NumberFormat numberFormat = new DecimalFormat("0.00");
        double calAmount = (orderList.get(position).getFoodWeight())*(orderList.get(position).getCalorieAmount())/100.0;
        holder.calAmountItemView.setText(numberFormat.format(calAmount));
        holder.foodNameItemView.setText(orderList.get(position).getFoodName());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
