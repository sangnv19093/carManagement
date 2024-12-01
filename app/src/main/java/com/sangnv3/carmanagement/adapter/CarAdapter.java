package com.sangnv3.carmanagement.adapter;// app/src/main/java/com/example/carmanagementapp/adapter/CarAdapter.java


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sangnv3.carmanagement.DetailActivity;
import com.sangnv3.carmanagement.R;
import com.sangnv3.carmanagement.Model.Car;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import android.widget.TextView;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private Context context;
    private List<Car> carList;

    public CarAdapter(Context context, List<Car> carList){
        this.context = context;
        this.carList = carList;
    }

    public void setCarList(List<Car> carList){
        this.carList = carList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position){
        Car car = carList.get(position);
        holder.tvName.setText(car.getName());
        holder.tvManufacturer.setText(car.getManufacturer());
        holder.tvYear.setText(String.valueOf(car.getYear()));
        // Định dạng giá bán thành tiền tệ
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText(formatter.format(car.getPrice()) + " VND");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("car_id", car.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        return carList.size();
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvManufacturer, tvYear, tvPrice;

        public CarViewHolder(@NonNull View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvManufacturer = itemView.findViewById(R.id.tvManufacturer);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
