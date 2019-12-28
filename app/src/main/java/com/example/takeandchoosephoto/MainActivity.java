package com.example.takeandchoosephoto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    public static int RC_TAKE = 1;
    public static int RC_CHOOSE = 2;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

    }


    public void take(View view) { // chụp
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // chuẩn bị mở intent chụp hình có sẵn của android cung cấp
        if (intent.resolveActivity(getPackageManager()) != null) { // nếu trong máy có 1 app nào đó có thể mở dc camera

            startActivityForResult(Intent.createChooser(intent,getString(R.string.chooseCamera)), RC_TAKE); // dùng hàm mở activy với trả về kết quả , bao phủ intent với chooser để user chọn app đã có trên máy để chụpảnh
            // gửi đi với mã yêu cầu (resquest code) RC_TAKE để nhận diện sau khi chụp xong (xử lý trong onActivityResult)
        }

    }

    public void choose(View view) { //  chọn ảnh
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // chuẩn bị mở intent chọn ảnh có sẵn android cung cấp
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager())!= null) { // nếu trong máy có 1 app nào có thể cho xem dc

            startActivityForResult(Intent.createChooser(intent,getString(R.string.chooseStr)), RC_CHOOSE); // tương tự trên
            // gửi đi với mã yêu cầu (resquest code) RC_CHOOSE để nhận diện sau khi chụp xong (xử lý trong onActivityResult)
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RC_TAKE && resultCode == RESULT_OK) { // nếu trở về activity này từ yêu cầu chụp RC_take, và user đồng ý chọn ảnh (RESULT_OK)
            Bundle extras = data.getExtras(); // lấy dữ liệu trả về từ activity chụp ảnh
            imageView.setImageBitmap((Bitmap) extras.get("data")); // chuyển dữ liệu sang ảnh bitmap rồi gán image view

        }

        if (requestCode == RC_CHOOSE && resultCode == RESULT_OK) { // nếu trở về activity này từ yêu cầu chọn RC_CHOOSE, và user đồng ý chọn ảnh (RESULT_OK)
            Uri imageUri = data.getData(); // lấy URI chỉ vị trí ảnh trả về từ activity chọn ảnh
            imageView.setImageURI(imageUri); // set ảnh với URI ảnh dc chọn cho imageview

        }

        if (resultCode == RESULT_CANCELED) { // nếu user huỷ activity dc mở

            Toast.makeText(getApplicationContext(), "Canceled",Toast.LENGTH_SHORT).show(); // hiện toast báo đã huỷ
        }



    }
}
