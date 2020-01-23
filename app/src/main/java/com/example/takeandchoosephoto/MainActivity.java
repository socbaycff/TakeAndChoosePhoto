package com.example.takeandchoosephoto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Uri imageUri;

    private File createImageFile() throws IOException { // tao file rong voi dinh dang, ten. thu muc
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg" ,        /* suffix */
                storageDir
        );


        return image;
    }

    public static int RC_TAKE = 1;
    public static int RC_CHOOSE = 2;
    GridView gridView;
    GridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridView);
        adapter = new GridViewAdapter();
        gridView.setAdapter(adapter);

    }


    private void galleryAddPic() { // add vao thu vien gallery
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void take(View view) { // chụp
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // chuẩn bị mở intent chụp hình có sẵn của android cung cấp
        if (intent.resolveActivity(getPackageManager()) != null) { // nếu trong máy có 1 app nào đó có thể mở dc camera
            File photoFile = null;
            try {
                photoFile = createImageFile(); // tao file
            } catch (IOException ex) {

            }
            // neu tao thanh cong, lay uri
            if (photoFile != null) {
                 imageUri = FileProvider.getUriForFile(this,
                        "com.example.takeandchoosephoto",
                        photoFile);


                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // luu vao uri nay sau khi xong
                startActivityForResult(Intent.createChooser(intent, getString(R.string.chooseCamera)), RC_TAKE); // dùng hàm mở activy với trả về kết quả , bao phủ intent với chooser để user chọn app đã có trên máy để chụpảnh
                // gửi đi với mã yêu cầu (resquest code) RC_TAKE để nhận diện sau khi chụp xong (xử lý trong onActivityResult)
            }

        }
    }

    public void choose(View view) { //  chọn ảnh
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // chuẩn bị mở intent chọn ảnh có sẵn android cung cấp
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        if (intent.resolveActivity(getPackageManager())!= null) { // nếu trong máy có 1 app nào có thể cho xem dc

            startActivityForResult(Intent.createChooser(intent,getString(R.string.chooseStr)), RC_CHOOSE); // tương tự trên
            // gửi đi với mã yêu cầu (resquest code) RC_CHOOSE để nhận diện sau khi chụp xong (xử lý trong onActivityResult)
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RC_TAKE && resultCode == RESULT_OK) { // nếu trở về activity này từ yêu cầu chụp RC_take, và user đồng ý chọn ảnh (RESULT_OK)
//            Bundle data1 = data.getExtras();
//            adapter.add((Bitmap) data1.get("data"));
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                adapter.add(bitmap);
                galleryAddPic();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        if (requestCode == RC_CHOOSE && resultCode == RESULT_OK) { // nếu trở về activity này từ yêu cầu chọn RC_CHOOSE, và user đồng ý chọn ảnh (RESULT_OK)
            ClipData clipData = data.getClipData();
            if (clipData == null) {
                Uri imageUri = data.getData(); // lấy URI chỉ vị trí ảnh trả về từ activity chọn ảnh
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    adapter.add(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                Bitmap bitmap = null;
                for (int i = 0; i< clipData.getItemCount();i++) {

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), clipData.getItemAt(i).getUri());
                        adapter.add(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }





        }

        if (resultCode == RESULT_CANCELED) { // nếu user huỷ activity dc mở

            Toast.makeText(getApplicationContext(), "Canceled",Toast.LENGTH_SHORT).show(); // hiện toast báo đã huỷ
        }



    }
}
