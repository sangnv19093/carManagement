package com.sangnv3.carmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.sangnv3.carmanagement.Api.ApiClient;
import com.sangnv3.carmanagement.Api.ApiService;
import com.sangnv3.carmanagement.Model.Car;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private TextView tvDetailName, tvDetailManufacturer, tvDetailYear, tvDetailPrice, tvDetailDescription;
    private ApiService apiService;
    private String carId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        // Khởi tạo các thành phần
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailManufacturer = findViewById(R.id.tvDetailManufacturer);
        tvDetailYear = findViewById(R.id.tvDetailYear);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);

        // Lấy ID xe từ Intent
        carId = getIntent().getStringExtra("car_id");

        if(carId == null || carId.isEmpty()){
            Toast.makeText(this, "Không tìm thấy thông tin xe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        apiService = ApiClient.getClient().create(ApiService.class);

        // Gọi API để lấy thông tin chi tiết xe
        fetchCarDetails();
    }

    private void fetchCarDetails(){
        Call<Car> call = apiService.getCarById(carId);
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response){
                if(response.isSuccessful() && response.body() != null){
                    Car car = response.body();
                    displayCarDetails(car);
                } else {
                    Toast.makeText(DetailActivity.this, "Không thể lấy thông tin xe", Toast.LENGTH_SHORT).show();
                    // Thêm logging để xem lỗi chi tiết
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API_ERROR", "Error response: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t){
                Toast.makeText(DetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "Throwable: ", t);
            }
        });
    }

    private void displayCarDetails(Car car){
        tvDetailName.setText(car.getName());
        tvDetailManufacturer.setText("Hãng sản xuất: " + car.getManufacturer());
        tvDetailYear.setText("Năm sản xuất: " + car.getYear());

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvDetailPrice.setText("Giá: " + formatter.format(car.getPrice()) + " VND");

        tvDetailDescription.setText("Mô tả: " + (car.getDescription() != null ? car.getDescription() : "Không có mô tả."));
    }
}
