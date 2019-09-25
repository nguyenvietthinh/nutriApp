package com.tma.techday.foodnutrientfact.viewholder;



import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tma.techday.foodnutrientfact.R;
import com.tma.techday.foodnutrientfact.activity.AddToCartActivity;
import com.tma.techday.foodnutrientfact.gui.event.CaloriesChangeEvent;
import com.tma.techday.foodnutrientfact.model.Order;
import com.tma.techday.foodnutrientfact.model.WeightUnit;
import org.greenrobot.eventbus.EventBus;
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
    private AddToCartActivity cart;
    private ArrayAdapter<CharSequence> adapter;
    Double calAmount;
    int pos = -1;
    public CartAdapter(List<Order> orderList, AddToCartActivity cart) {
        this.orderList = orderList;
        this.cart = cart;

        adapter = ArrayAdapter.createFromResource(cart,
                R.array.weight_unit_array, android.R.layout.simple_spinner_item);//Create an ArrayAdapter using the string array and a default spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Specify the layout to use when the list of choices appears
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(cart);
        View itemView = layoutInflater.inflate(R.layout.cart_item_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        NumberFormat numberFormat = new DecimalFormat("0.00");
        calAmount = orderList.get(position).getCalorieAmount();
        holder.calAmountItemView.setText(numberFormat.format(calAmount));
        holder.foodNameItemView.setText(orderList.get(position).getFoodName());
        holder.foodWeightText.setText(numberFormat.format(orderList.get(position).getFoodWeight()));
        holder.spinner.setAdapter(adapter); // Apply the adapter to the spinner
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

        holder.foodWeightText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.foodWeightText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            Double newWeightfood = Double.parseDouble(holder.foodWeightText.getText().toString());
                            Double newCalAmount = newWeightfood*calAmount/(orderList.get(position).getFoodWeight());
                            calAmount = orderList.get(position).getCalorieAmount();
                            pos = (int) holder.getAdapterPosition();
                            calAmount =  newCalAmount;
                            Double calChange = newCalAmount - calAmount;
                            holder.calAmountItemView.setText(numberFormat.format(newCalAmount));
                            EventBus.getDefault().post(new CaloriesChangeEvent(calChange));
                        }
                    });// code to execute when EditText loses focus
                }
            }
        });





    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

}
