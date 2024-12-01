package com.sangnv3.carmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;
import android.util.Log;

import com.sangnv3.carmanagement.adapter.CarAdapter;
import com.sangnv3.carmanagement.Api.ApiClient;
import com.sangnv3.carmanagement.Api.ApiService;
import com.sangnv3.carmanagement.Model.Car;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.SearchView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvCars;
    private CarAdapter carAdapter;
    private List<Car> carList;
    private ApiService apiService;
    private SearchView searchView;
    private FloatingActionButton fabAddCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các thành phần
        rvCars = findViewById(R.id.rvCars);
        searchView = findViewById(R.id.searchView);
        fabAddCar = findViewById(R.id.fabAddCar);

        carList = new ArrayList<>();
        carAdapter = new CarAdapter(this, carList);
        rvCars.setLayoutManager(new LinearLayoutManager(this));
        rvCars.setAdapter(carAdapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        // Lấy danh sách xe từ API
        fetchCars();

        // Tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query){
                // Không cần xử lý ở đây
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                filter(newText);
                return true;
            }
        });

        // Thêm xe mới
        fabAddCar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddCarActivity.class);
            startActivity(intent);
        });
    }

    private void fetchCars(){
        Call<List<Car>> call = apiService.getAllCars();
        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response){
                if(response.isSuccessful() && response.body() != null){
                    carList = response.body();
                    carAdapter.setCarList(carList);
                } else {
                    Toast.makeText(MainActivity.this, "Không thể lấy danh sách xe", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<List<Car>> call, Throwable t){
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_FAILURE", "Throwable: ", t);
            }
        });
    }

    private void filter(String text){
        List<Car> filteredList = new ArrayList<>();
        for(Car car : carList){
            if(car.getName().toLowerCase().contains(text.toLowerCase()) ||
                    car.getManufacturer().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(car);
            }
        }
        carAdapter.setCarList(filteredList);
    }

    @Override
    protected void onResume(){
        super.onResume();
        fetchCars(); // Cập nhật danh sách khi trở lại từ AddCarActivity hoặc DetailActivity
    }
}
