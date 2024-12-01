package com.sangnv3.carmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sangnv3.carmanagement.Api.ApiClient;
import com.sangnv3.carmanagement.Api.ApiService;
import com.sangnv3.carmanagement.Model.Car;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

import java.io.IOException;

public class AddCarActivity extends AppCompatActivity {

    private EditText etName, etManufacturer, etYear, etPrice, etDescription;
    private Button btnSubmit;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        // Khởi tạo các thành phần
        etName = findViewById(R.id.etName);
        etManufacturer = findViewById(R.id.etManufacturer);
        etYear = findViewById(R.id.etYear);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        btnSubmit = findViewById(R.id.btnSubmit);

        apiService = ApiClient.getClient().create(ApiService.class);

        btnSubmit.setOnClickListener(v -> {
            addCar();
        });
    }

    private void addCar(){
        String name = etName.getText().toString().trim();
        String manufacturer = etManufacturer.getText().toString().trim();
        String yearStr = etYear.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Kiểm tra dữ liệu nhập
        if(name.isEmpty() || manufacturer.isEmpty() || yearStr.isEmpty() || priceStr.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập đầy đủ các trường bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        int year;
        double price;

        try {
            year = Integer.parseInt(yearStr);
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e){
            Toast.makeText(this, "Năm và Giá phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng Car
        Car car = new Car();
        car.setName(name);
        car.setManufacturer(manufacturer);
        car.setYear(year);
        car.setPrice(price);
        car.setDescription(description);

        // Gửi yêu cầu đến API
        Call<Car> call = apiService.addCar(car);
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response){
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(AddCarActivity.this, "Thêm xe thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại MainActivity
                } else {
                    Toast.makeText(AddCarActivity.this, "Thêm xe thất bại", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddCarActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "Throwable: ", t);
            }
        });
    }
}
