package com.aryan.stumps11.More;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aryan.stumps11.Activity.BankAccountActivity;
import com.aryan.stumps11.ApiModel.profile.documents.DocumentsData;
import com.aryan.stumps11.ApiModel.profile.documents.UserDocumentsResponse;
import com.aryan.stumps11.ApiModel.profile.kyc.KycRequest;
import com.aryan.stumps11.ApiModel.profile.kyc.KycResponse;
import com.aryan.stumps11.ApiModel.profile.profilegetProfile.ProfileResponse;
import com.aryan.stumps11.Home.HomePage;
import com.aryan.stumps11.MainActivity;
import com.aryan.stumps11.R;
import com.aryan.stumps11.api_integration.CheckConnection;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KYC extends AppCompatActivity {
    private TextView t57,t92;
    private TextInputEditText tt;
    private TextInputLayout tt1;
    private LinearLayout layout;
    private ImageView i20,i9;
    private CardView cc;
    private MaterialButton save,upload;
    private Uri uri;
    private Bitmap photo;

    private String imageString,encodedString,UPLOADPANCARD;
    private MultipartBody.Part profileFilePart;

    private ImageView imgPanCard;
    private CardView cardViewPanCard;

    public static final int RESULT_GALLERY = 0;
    public static final int CAMERA_REQUEST = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    public ImageLoader loader;
    Uri photoURI, finalUri;
    File photoFile;
    Bitmap resized;

    private TextView tvPhoneNumber;

    private String verify="",verifyStatus;
private SharedPreferences sharedPreferences;
private SharedPreferences.Editor editor;
    private boolean isDocuments=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_y_c);


        sharedPreferences=getSharedPreferences("MY_APP",MODE_PRIVATE);
        editor=sharedPreferences.edit();



        cc=findViewById(R.id.cc);
        t57=findViewById(R.id.textView57);
        t92=findViewById(R.id.textView92);
        i20=findViewById(R.id.imageView20);
        tt=findViewById(R.id.Pan);
        tt1=findViewById(R.id.Pan1);
        save=findViewById(R.id.Save);
        upload=findViewById(R.id.upload);
        i9=findViewById(R.id.imageView9);
        layout=findViewById(R.id.Layout);
        imgPanCard=findViewById(R.id.pancardImage);
        cardViewPanCard=findViewById(R.id.kyc_pancadView);
        tvPhoneNumber=findViewById(R.id.verified_kyc_number);

        //getUserDocuments();
        DisplayProfile();

        if (sharedPreferences.contains("UploadPanCard")){
            UPLOADPANCARD=sharedPreferences.getString("UploadPanCard","");

          /*  if (UPLOADPANCARD.equalsIgnoreCase("True")){

            }*/

            getUserDocuments();
            cc.setClickable(false);

        }else{
            cc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(layout.getVisibility()==View.GONE){
                        layout.setVisibility(View.VISIBLE);
                        i20.setImageResource(R.drawable.ic_baseline_expand_less_24);

                    }
                    else {
                        layout.setVisibility(View.GONE);
                        i20.setImageResource(R.drawable.ic_baseline_expand_more_24);

                    }
                }
            });
        }


/*
         if(!verify.equalsIgnoreCase("")){
            getUserDocuments();


        }else {
          //  getUserDocuments();
            cc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(layout.getVisibility()==View.GONE){
                        layout.setVisibility(View.VISIBLE);
                        i20.setImageResource(R.drawable.ic_baseline_expand_less_24);

                    }
                    else {
                        layout.setVisibility(View.GONE);
                        i20.setImageResource(R.drawable.ic_baseline_expand_more_24);

                    }
                }
            });

        }*/




        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ImagePicker.Companion.with(KYC.this).start();

                selectImage();



            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPancardDetails();
                Dialog();
                 Notify();
            }
        });

        i9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout.getVisibility()==View.GONE){
                    layout.setVisibility(View.VISIBLE);
                    i20.setImageResource(R.drawable.ic_baseline_expand_less_24);
                }
                else {
                    layout.setVisibility(View.GONE);
                    i20.setImageResource(R.drawable.ic_baseline_expand_more_24);
                }
            }
        });

//        Condition();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MoreActivity.class));
        overridePendingTransition(0,0);
        finish();
    }

//    private void Condition(){
//        if(t57.getText().toString().equals("Not Verified")){
//            cc.setEnabled(true);
//            i20.setVisibility(View.VISIBLE);
//            t57.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Color1));
//            t92.setText("Please Verify Your Pan Card.");
//        }
//        else if(t57.getText().toString().matches("Verified")){
//            cc.setEnabled(false);
//            i20.setVisibility(View.GONE);
//            t57.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Color5));
//            t92.setText(tt.getText().toString());
////            getUserDocuments();
//        }
//        else {
//            cc.setEnabled(false);
//            i20.setVisibility(View.GONE);
//            t57.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Color6));
//            t92.setText("Please Wait We Check Your Information.");
//        }
//    }

    private void Dialog(){
        AlertDialog.Builder alert=new AlertDialog.Builder(KYC.this);
        alert.setMessage("Your Pan Card Verify Within 48 Hours.");
        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                t57.setText("Verified");
                layout.setVisibility(View.GONE);
//                Condition();

            }
        });
        AlertDialog alertDialog=alert.create();
        alertDialog.show();
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        uri=data.getData();
//    }

    public void Notify() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel ch=new NotificationChannel("Hello","Hello",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(ch);
        }

        Intent notificationIntent = new Intent(KYC.this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(KYC.this, 0, notificationIntent, 0);


        NotificationCompat.Builder builder=new NotificationCompat.Builder(KYC.this,"Hello");
        builder.setContentText("Your Pan Card Verify Successfully!");
        builder.setContentTitle("Pan Verification!");
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(contentIntent);
        NotificationManagerCompat man= NotificationManagerCompat.from(KYC.this);
        man.notify(1,builder.build());
    }


    private void addPancardDetails(){
        SharedPreferences preferences = KYC.this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);//second parameter default value.
        String tokenName="Bearer "+retrivedToken;


        String pancardNumber=tt.getText().toString();

        imgPanCard.buildDrawingCache();
        Bitmap bmap = imgPanCard.getDrawingCache();
        String encodedImageData = "data:image/jpeg;base64,"+encodeImage(bmap);

        encodedImageData = encodedImageData.replaceAll("\n","");
        Log.d("myString",encodedImageData);

      //  Log.e("bmnkjmkl",encodedImageData+"");



//String image="data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkJCggKCAsLCQsKCwsLDhAMCgsNExcVEBQPFhISDhYSDxQPDxQSFBgTFhQZIBoeGRgrIRwkExwdMiIzKjclIjABBgsKCw0OCwwMDg4MDRAOHRQNDCIUFRcOHggXDBAWEBEXCxATFAsRGREeCRkMCCIYHRQPHRANDA8WEAsUFSMWGP/CABEIAakBqQMBIgACEQEDEQH/xAA2AAEAAQUBAQAAAAAAAAAAAAAABQECAwQGBwgBAQACAwEBAQAAAAAAAAAAAAABBQIDBAYHCP/aAAwDAQACEAMQAAAA9xAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMRlYhla42GsNlrXGdrjYYLjKx1L1BUAAAAAAAAAAAAABTTNrUt0U7GPDbE5661xsVxXJvx240ZKYrTNTFROa3FQz1wVM1cFTNXDVGZiuResoZb9e1O9mi6k5m53LOM7XU2ioAAAAAAAAMGhK1TF3SNCPs3MJrUzIYWcauLeEdZKJmKtl6EPbNVhC1ncJFVlBG3SI0K7xGnXcS1G5kNK6R0025NW2G1j1bjoBOIAAAAAAAFt1l4ABQidGUj4z2Hh1H631WS8as22v0tk8S6bX5rv9HyPnu2y+gNn5v8ApvfXad847aCEvmBFZJFLUzZSAAEbJRqdFRGVuTHnROCcQAAAAAAALL8eQAY7+OqujLp3bfyb0EH419A+A+qvrcEjKXNpE2+n8vw6uJz0us5t+mPmf6X7fOzClbnxYAAAACNko1OjSqMl9CJ5q1nHZR1iZRF4EzaFuJhF7SNpSqAAAMWXFlBHasoiPxyfxz02SUsy31Rb8/fQlLPu8P8AXZDFXbcOLYxUHN4pB+zeO+o9zh928q9Pq6ztb+fn/ovg6ju1AAAAI2SjE6KtIyX4rzJhw4YbVuvgN2mHelr5b9IkMvP7kJaVgdiYmGDPOIAGLLiylOP6TkPD22xNae15rp2MmK+5r8ttHTrtsvs49vI876RzPL2Q3Sam3VdOts48FX1bHb8ZKeurejUr9MogAAAEZJxidApE2ZMaGLHk14zr1t92WsJgCnMdQTxm9tw+OUzK8/uTEwpWcQMWXHkOdidjH8i9JI5tbjOnm9Cv5Hrd+i+tlerVWy6zRlzsNJR1NZ6mXz/fuOaZn/O/XuDdbWmGj7e9W3fdPJBlAAACMk4xOhStImy2tYaHVZsswEwAAAgZ4cjvx2eM5iRhJtiExjvtuOFzau18O9dzstqdDd8EjXmJrkwkK43fovtpTXMFwHoPhW/bx07bF/VvP/VulG7nzu6vsyY/A3nRy8FOfafK1FlzlBVRE1EkZJxidChDHtau6TQmAAAAAI2E63kIy35SE3iZE42WX3RPBZ9fP8H9hmhpblLLRJSHCTNlwdxu8h01Vs2VldmuA8F9uwfQqztfjv0Cb9vS19O8k9c8xZ2WR0b8l9v0nMcdb9EvJDFp27LPp+98Z6SaDtMEzr/PqDqN3hO7+m0KMk4u55dApDHIx0kmWE4gAAAAICf0omC3NLbjLoGLLlhZdSpwelK4fiXq4SFjNL09fy8X3Mh9GoOZ9n+a/WPJ2Xrt+PL8/wC+zLdg9XxeT8X9L8t9j8vxPWScV859XH81K6nN7+N1ZHT23GG2+2N1k3CY9fL7RKwz5/8APHWch1fqObejJOL+i0uhStInHJxcqSgmAAAAABQ5Pa1c+Oc1uRslljQqjlo7qeX+U+jriyWUHbOwHT+Efof55yHqHmPtGjr67PC3fF/Vz+bTkr6rhNPotK24ebh5WDpfZY9fZs9NZRenI6PPdaFLqarKy2+zZr2PTvJsdXSem+ofLH1Df+NzxcpF3fn9ChE4piHmUyAnEAAAAADlb60xz35iEm5xpW26Ypw3dQfmLCCrR8p9DlyRvJfR6zi92y/0Ptl66rvs3XclvZVXsMLxk753yFsR0XO7+1StPTdGjob2houNK263RbW2XW54WYc2HLk1vcfD/R+/yfsMXKRdt4jQtrSJxzMLOp3ROIAAAAAHN477Mc9ycg5ycbL7bphZehxWv13F/KfScpx+HL6n2ea/Hfvtslbbsc829HbuXPJyMPu7aLpuekY6o4a4r9S86cGln0+e8wWmrvssutzwtwZsGXFg6rlJ/s899FxUrFXHz/QtutZY+g57oTaE4gAAAAActfizY57c1Ey0423W3TAFPJfW/nDkt4X2vxSf4PVertvx7pp5/lMve8HqOCu9z1+mp8i3JvmeW4mLI6+Mc+tbhjqpp5MOmzWXWMraVtz12YMuDOvwdRyvo3Z5z2iKlIu38NoW1pGeLoud6NGwJxAAAAAGiQWey7HOTktfYywtutuALfk36q+S+L0EnIRPsvF6DoPJfRKdFZ5X6b5nN8PqJrUisU6PT/LdmOx6d2um0Wmzhx2xsrRSM6WXW5a6W3YstNmDJg21et7x4T9UWPktmJlomw8zoUUjLH0nN9IxziYAAAAAQM7zCclt10ZdDUnCy+y8A1Pl/wCgL/F3/wA7bHoXnHV6Tbz6ubfc57sd2Hbfdux6clcebDOixGy+1aXW0plipfgy1TOp635X2+VgNbNh19Xo3tMdI3PgERLxGzl0KVpGWPo+b3kTylZxAAAAFDRiV8ZWScT1AE42X2XgI4Dd0534r6jU8Q9r8buuiJzaex7K/wBj2vxD37fVdD4zh5+cfRuzsje3z3mc877kvOU4nd1dFp1Ozu+d76rotzzL3qNuHwHsOMx2Y/X+B+juirqO/wAyh5iHTpWXWxlZnx7ENiYg5KY2hOIAACK24VNaXYMct6bpXLEEUrSoBx1Op435xe73kfU+UY21NrTyev7t3seF+jNnNxu7kb6zreTk+y31vlcpw/oPJeePSkPv1/re0879Y8h6ai73LicO6s8+YPcsN/WSxbeICcUPMQ6dGi+Mra0xw2r4+QJq+E28sZBbVFVmqndjNS2FtzAypMac4gJxApWlQBSo8/8AA/rvw3kvfOFK8l/f6N5tXHD3bzTmc0pbrvPrtfZs9dxLHdluwW475+Bx9hsr/Xfm/wBg8k66PtPd8Gfsow2coCHmIdOhfZbGTVyxmM7e1E0xy6XLCbmWG6w1lmsx2GfFrYDPu7sjMUqTiABStKgAClR455P9dwXLc/MT0vheW7jrsVdfTnu10Z7TWYzs2YUxfjqzwu988h+meqhqO2gAAQ8xCp0sd9kZY+r5XrWNRMReh0aJ5LH2KJ5LZ6RMR0iTAAAAFK48ZsAAAUqKVDS5zsGO3zCE9qa+n535j6vt19XyLd9J+RaLTiaUpqsfcPTYqVs/FBnzgAIOcgk6mO+yMq9XyvVMQmAAAAAAAAKQHQEwW9TQiZ5BCeRWyjcW1mKgAAAoqPMPFvrjxbjvfZ7qOyiqoKtTVTK05/BE9HA47UsVZcpLk4AAAAAAAAAAAW6m6iYjXn0OcdEmedyTuGEbftYSy/HjNmmoTs2YRfo7QwY93IRtZO8iaTm0ctsdLWYht/aTFKiAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP/8QAMxAAAgECBAUDAwMEAgMAAAAAAQIDABEEEBIgEyEwMTIFIkEzQEMUQlEVIyQ0cHE1UGH/2gAIAQEAAQgC/wCKLitS1rFaxXEriVxDXENcQ1xDXENazWs1xDXEriVxK4grWtalrUtXH3t61Vc9G+V6vV8r9K5rU1azXErUPtWNqvR/9AKv9m/fbYVYVarVarGudc+tY1Y1Y1pNaa0irLler/Z/ky0itFaWqx6nKvZXKuXRsa0Gm5G2wfZ/k32FEDnX9QkvzGPX5TGQMwU8qs1H2i78WGtcdc651Zqs9aXrQ9cNq4ZrhitC1YbJPLYO/wBn+Te0qryErycJyRl3qPEsBaUYxF8cRiXxFhkagN8PGetJ5bF7j7P8m29hcvIX7VN9CTPTQ7Ue1as8L/qxdaTy2DvuvV+p+TYTTtrOQUmpl/x5ABlBA85r9DDU2EmQXTkcjWHP+PGB1ZPLaG5VrrVVzXPZc0G6P5NkrajpFKv818Wp4ZY3K1HhppTzRFjQImWJj4c5tQF2ApSYzcDn26knltNXq9Xq9XrVWqr5q/8AO78ucraV5Uo+ehap4uLHaiCps2FTVNfKBrNoPUk89rd8yaCSt24D/PAejHKKvQNA5I3wdv5c3bU96AuehKmKkPL9Nil5qs8qHTiXSOQe5ERBZK/6RgVBHTk8tpy5sbKkSrtZVbu8TJzANA5I3wdn5cpG0oTkvboEgC54rE8uKLWlUKq+xjYE1H9NcoTzK9STz2muZNgiBB0JYre5BQOQNxs/LlMeajpTc2C1J7Ar1PyYVG4RVvPLpZ6w4YQjVSmzqepJ57SefKKPTzPRlTSdQoUpsdn5cn5yGhk+MsbRRzTHz2yXu5AIlwz6uOGji1QupmXVEn6qfU2XxSG6A9OTz2oipz6ZFxY20mxr4pDcZ/ly+TQrFuQqxrBAsQud37mriDDuXo1AsbSDjSIiwrw84fDpyeexPIdWdf3ZCo/K2f5absaHYUKVdWIaRiwAueI9xQb+dh+oaxzasQUV8NiEj4jobNWBk4kDwsvjnB+7pyeeyPy6pFxau3LL5Bz/AC0/g1DtnM3MU5ACNUzaFBpT8HOc6PfUHOdZJMS0f6WQyAE9sC2nExV2ZxRyE0UOrU+NlPgZpz34swpcZiF7x4yFxTGR+ZVpEPJHDi4yk89kXn1pRZ8vil8Rl+Wn8TQ7DOe4kBovfDyREzCfAo1RklBQNxfPHf6klNhbemqoa8sYFenOmHeTjw88ahVyAzk8y12llt7U24RQZblrfPekPDkvnJ5bIvLrTj2g5Couxy/LkKfUPevHHK2IMg96yS9tKyuqMghxksZ92HkWRLrkQGFmiP7TjIDC+tL3rARkycU9yXMjWFhuw7BZOaDV7ie1OOVRm6A5SeeyLy60ngchUXkcvyZNydq+LGbDT/iRPUb6Vl9Pkhh4kkXpxmi1h1ZHKt6crBeIuZB8hqBFHC4S96fxEaMaOZ2o4KC3N+7HlUX01yk89kXl9innl+QZTeed9LK1YpdeFkFYbEwrhhxJScTi2KYOOWDjRSbGUg6k1LV7lmpv4yNGjtV3XkA90GkMjTLGcpPPZF36575L5DI/UGU49l8zzFQtqj5zJwpXiePiQTq6YeU4ieWRuLEBz4+HpWRvHI27kduZ7nM77kdrlSGCMHRWFSeeyHv128jkvkMj9QZGrWJGaHS9YrDjEJy0ns3YWGQqLESJ3V1dbrI9BnK3OZ6Br059WGtlJ57Ivnrv5nJfIZN5jPELz1jLVHU2IYjhpsFClJB5X18g3gdh6Ppbe+VcpPPZD89dvI5L5DJvMZnnyJBVtJNrc8TJb+2m0UKFKbGn8DmaNHoenm2LGUnnsh+eue+S+QybzGx01inbQjM1yTc7xkpuhXaehgv9yPKTz2Q9j1j2zj8xk3mNvqbC6IKgwmHeBGb9Fha/RYWsLCkk0iP2Nq9xBIDUDkp9wo9+p6eL4xcpPPZD2PWk8M4vI5N5jbi314uQ5JPOukDGMVwxK8WegzKbrgoo5IZQ0bQHVFHqw0LLEMYgSYMMr9M16UP7kjZSeeyHsetMfihlD2OTeY2fBNXuxOQSXkRjFd8NZODicvTfy1gOWMYVjP8AcNY/6CnrGvTE0YbVlJ57Iex60hu2aCyjJvMbJPpOaGUOOgjgRD/UYa/qEdfJNQTvATpSV0mMqyO0r63eaaQWk6oBdwgRQiKoqTz2Rdj1TyGYFyBm3mNkn0npIZ2F15g2P2MEXHl0VNG8L6Hy9Lh1SGU5SeeyHserKfjOIe4nNvJdkzXbQB2rGqpTVQ2f/AYsQBc5EMps+6zG9qwMXDh1HFS8admyAZ2CLDGIolQZSeexW0nqHkK7nNBZc28l2LzY1zbtoS1ToI5iBnhIkhi1uCD2xwUYj24CEW4zTxLNGVOGUfqlSTEYcnE6MP8A072+7DqrYlVklwbNP/amwUKQkr6fKqsYmkwP99SmMl4WHNqNemwWHHfOTz2odJ6cjX5ZxjU2xvJdg5O1LRYk+zF6/wBR7svkVioePCAqwY+E3jld3kYyakhwykw4uOabQsg0+qLWIl4MRahjMWDesOf8tDWNneKyR/qMTpKmsJOZobtjpeJiLCsJAcRNarfxnJ57o2+D0JH+Bmi6V2N5LsmGmW9d1tV7Dljm1SqNkWNxEVlP9Sf4xJjxGC4wZBNhtNYGB4yzyT/+Sir1L6SZYf8A2Yq9S+qhy5kgByuEwXKgrO4RIIVgiCLsk89oOSPfvuZ9kSfuOxvJdjKHFiVkj7zYhYuVEliS2YjhngTW3pqfjmjGG9PZKwMuvDhcmk14/XXqX0Fyh+vHXqY/txtl6dFqkMp9Sk1SiME1gMPwk4j7ZPPYchkHIrXWqtVFzVzsjTVzO1vJd3qGH4sfEXZh8cYkCOfUcPblPPJO12jkeJ9aS46R00op0sCJ8Y08eg0CQQRLiJ5l0yczyCBcNhbUzF2Zj6fh+K/FfdJ57GOQq+y2d6JqOMvzbc3ku/HYThkyxdX09UbE3f1SS0axCKNppRGqKqIETdJ57LO7WQrKnkHFBqvV6vV6vV6F2NkSG3N97eS7zWKwBHvw/Y2PUZmPn6dBwotbb5PPZEoVMjHG3c4aP4/TuO3DnrTPXDnoQy/KwJ+4ADt0G7r0Z8PBP5yemyj6TxTp59LCxcbEKvRk8z9u3deo8GHbyfAYOm9Nh/afTJP2tgMYtNHKnns9LjtE0nRfzzHf7ST4oSfz1/8AubBYWTviMDPDzWjUC6MPGvRfzOa+Q+0IuK7GxoO1B1+wxuCBvJAvN1HQuKMq/BNzfOEX932pAPcxsO3bvlqahJXEWrjqY6DhTpMmV1riJXF/jiNRZj3z79lhv5/cGNK4R+NElc9lzWt64j1xGritXFauK1cVq4rVxHp/7i6Xu229XFAOewic9xCnzyHb77StcNK4S1wa4JrhNXDkrhyVw5a4clcOSuHLXDlrhSVwXrgtXArgL88GKrAdv+cf/8QAKhEAAwACAgECBQQDAQAAAAAAAQIDAAQREgUQMBMgITFABjJBQhQiI2D/2gAIAQIBAQgA/wDX0rOSl6L5Gd+xguzXkcjyFA3Sr7YB4WNjTnn8Tc3Z6s+732Nncpy2hExk4YU6gkpsRZ+Aev8AGsR2PP4dKLNWd9i1d25bIQSahVB685dyw4x81b/FRc3tp5XgZ6uymxJLL+F5zZ6omuuugVecRsJxxn+RVfot2H0opDMGo3g9npV4N+F5Gnxd2uTSlOQgDIeGDY5xixdSG0aJLo+8iRnLWEKmVJ1AIIBHz8e0XLVo+PbqiQQP1PB74z542RptVmZCfcCn6h1fh1k5IzSfvr67H0594/Y59uc1+HcAy8d26s/YqWRg/OeBVAWq9tK0zwP1RP8A5RxNalHE0iaSlOI7Nj7bxR3w7+x8RqDQ3BtSD+9UfCvVTq6kSZvBNRREtnkfHSaVbSmM0bGDgmO8yqFfzu01XnAQ1xFAgPpWYojzZXVO82/T/POwPe8xDptF8jUxrKo0xPZmyY+s/S4pKL/zOfIyPkq6wKNr0O1uX2HxvXY01twRoQnFGRfd8vqm0O6n6/bwu/REMn2N3Y2D/wBMAGb8qL1OeK6lLOMb1GRPDD3jnkfHvCwaUYLGaoOfQHGVXVkfQkYjZiThPqMT9y+/bjrxiFVP+1Gkh4KqKdUH+MPocYFGKnkffCfkGTHLL79v6jFVSTzQRc8mbBXBJovw+mVcO3ZeflAyI+pPv73lklQyTX2BsSFQR6EH+ePUehmFRSQMReoA990Vtuqv4wuLUTCMiP3sLUJTgqEUKhSadKKxMi0+jmKlgY8OAjVbs2RT+x9/yvjq/EOxHxenZGNrHJqxJ68U4f4gVW6UznlNjF/cuWHDtkx0Qvk0LHB+DRP7DASp5Du7DghmAIALAEADACzDmn3ChE6jj5TnOA+40ufqCrD75xnGcZxkl5/2Pz8ZxnHudVOGaHDEfwZsPRRwAPyqJ/Yfh8en19fr83H/AIP/xAArEQACAgIBAwQBBAIDAAAAAAABAgADBBESBRAwEyAhMSIUIzNAMmBBQ1H/2gAIAQMBAQgA/wBW+f7IVmOlwcFGFvrv0zDKsFfpl6/IxujeonO3OwBjCtl/qVVNY2gqpUvx09vUFvHJSupDZanU8Zk4RRYD+XVv46fHqamveFLEKFC0oBLHLHZxM18ZmZcjMtyWDWAiYNy2V+m2aEdvTNiFGKn+lh17LPLTyMPYRFZvpOSmb2Zl17UP4f8Aib8NK8KljQiagE5BawsFw3uUkszPCvJWU+49t+LX4gQruNWRNTUvYCtDHLcSUwbQ9Z1LRqxx5h7DDsfIe87hX4DDUzGZqwi05lVgO+mHWxK6nchVq6VjKS7+hTrUzcDFKPZAK+IU30+m2h5R+SKY1jjkHa78wkpuYHgTLV5KQGppLcz0yrluyVMB8Ctu1qc0dIyNy2csfjVvsfHivusCMAwKnJNlLLYBcpKFDVYfkOSDovipadzGqWqta1U6Mqb6hhmRiq5DDqFRQ1HufCe2NZxfRlnT3yLV9PE6bi4o/bBMyaq7ARZ+kWpiRX9HtVP/ACGWTqC7pY+fHu5gA0KFAAAglgliRRrYirsypfqGGWTK+a7e58mCm7gZk1XWIvo4tGdko1iW2WYnq5Nh61Yeayqxb6ktX0fy3EriLqGGWGZbaqsMHY+Tp4/kMtutRF9LEfqGOhSvNqsvxmRExbv1RuOBiPRT6VvpCBAO7GWNM+zSqnc+8e3GD1oWlTkxGnIa2UtrffDkoIXt89uQJICZll2VaqWN9k32+q7N3PvEPsdiEUigxGnUbdjHpbp+IteRzrtfIuttylvy7vXxbaVrzkqzP1OOnUba6bE6gttFjZNGFV6VIJzsj/qX2Hx0XKyBHRB9hWmVdUq1i7lhlsc4r3XUjIxZo13dKDXtuq8Tp9u8WoTKf9RkV0DJyBUpaEkkkwe/Xgw8gD9p46JYOL0Y1FTc1emixlsd66nZHcuCCC7pTW5TEHFHue+42uWPYeDfgpzSulsS6p/8RORnKcjNzNt0FqXuOx7bm5vyCyxfoZd4i59g/wAqsup/jtc/Ox29g7Hya8GLkn+J+2pr+hubmxPifE+J8TevqbE3N/6H/8QANhAAAQIDBAgDBgcBAAAAAAAAAQACESExEDBBUQMSICIyQGFxQlKBUJGhscHRIzNicHKC4fD/2gAIAQEACT8C/awIIbMFBBBBDYP7THmzYL0ezMExhWjK1mxxKIs3RmVpNH7wns99oKBQtKJ9ibxRhumQ2G63XFOd2W6wYZ2+Uey5N+JsyufL7K4cBb5TbusHE77J71vge/YwaI8ueS4RXvsYpphgUNRuJP0UgLeF+8LMSuxHsipkLviE2FCBVGTsp4eWb70QnBCPa+oJC5cGMwzWkimw/WgHdUICyooseUmc9kKbbv0u5IRC4cLMrO45KpXqbj1F13uu68Neyo4QHzXicRD1VPoqu3v85TiN1Q1ue1rdbqtC4ddrBVAIf90eCLfhJHd0bS4qI0Tfj05St5hcZ2VepvxP2uOAxaR0MwqJx1I78EINZTtsYHnPW3Hb8O6yyE1I7VB80whmapiqs4f47GfsPLYyis4LEwXpsYAobutMqGrqys8Y1Ss7a5BANC0jk9yOsOqk7yr0C9y9RyuO1lsU+v8AqrDWYVg9rXfJVaP82P8AprjZ+J904y4ckKiR+iEBryCzXo37quJ2sBYFSh53DiHRTBqFvaIiYyRiP+kqOW8DVUtmF4fkvynfA2cLKd1n+GPqq47dCv625c3msUdYYZhCERNOFaBEMyiqtkUYscdR7cnYbFQoEHNM9MEIAygMlQSFzkuH52Zc3iLcDNZRCdq6lUJ6R26EIGAeNn1aviv4tuqZWHi5zwz2KjdKp4eynqmIQgAzVCeE9OB2OpuyQqia8QjzmFtHSKlpG8J+hWElTHY3ha2V74DDnOzrSEd3F2dxIeM/S+yjzvp2sgI8Vz632II52oovDh1VT7VrV1jZkRKB96B96o0GHyXZAloq7BT5DCPPYSFjzqxEkYGU1pHokHEoVMCeihuScFBpNAqPHxv8BDnsTGxjoRqhEyktE+39XzX6V5hf+Mx53ym0Oi0QKY9MesTFAO1qxQETGXdQj0TogX1XGCo0Q53ylMKEDyUhVx6L0OYto2Te/L4bdBxWcTdmZyWjfC1paeu2CdWbrOLSTPZcI3WWTLpBeHns1IYlBUqNiGu6ZJw6IgqUt5VMmfdf1PVDMEJkJRccFpDrdKIRE4hAM0cBP7Iu12iMVD8Sh+i/KJi8ZKrt1ttT+X2uaX1BcZ2T6qUpW5p0CJ9EPdRCD8QqMaEDSMViQfghE0aOqcD0WLipOdj0T9YOkYrCiExuk5rh0e6O9nA3jP05j1uKOtwGxBwFNaq0YQg5v/QXibJCDuEBdF5rPMsRZUyCqBAfysmXL+xzPL+l0JZoRfgFU7DQd0TxWkcO6MdY16lcWjkRZTXAHyWDrPMFg6FlGSb/ACVGV72fmP8AgOX4bwb7Plss1g2hQfFSA4WquIzCbqR4j9l4TFMAxjZUTCIhVVMgvAIu7qrjEobjKdTy/DlfDcPEMr4jcmAc1V83dljXspNbdiOaabkRUzlhyFMWfZSN6SYSEVx6T5XmMzY0KITo90AfVMKaiAou5Ns/MKpwcMjVMcLug3nezGNKbDsnuWkaU0O7Jjhs1eYDt7SbqnNq32ZitvlHtQb3iZms7mex6ctPYKFhvBuudvd7SEULDsTXu5mSKCB2CjbBAIBQULN4HBE7bSiAp+wALCUUUQoKCggFBQUFBFqcE5FyHv8A3z//xAAqEAACAQIFAwQDAQEBAAAAAAAAAREhMRBBUWFxIIGhMJGxwdHh8EDxcP/aAAgBAQABPyH/AMo30bxzkWTxDYGwjZRso2UTjaGwJYCGoWizmN42BuL/AGSOI2yG7PqlEk4J6BOCSSeicJNxm8JegtSE3/LAjBLKo5wn/HJJOMkkkiZIVUP/ABzjFaE9Gxhc8EsCNBGhldGVK4z1V0IejNhm3h8GFus2CUrJIbl3+N2cYPRHpbuNWjN16E40FqWBwOCKFCSSSScJ8hNu4N0JJGy7/G/h17CF02SS2KVCTSJVPdj6pOGJQa8KxO5VZ/c2isck3oL3ZJZ39an/ADn5OD2OLP8AiYVuDZFsoWcVnAk5EGS6PhxZa/x/XrcdlVu7HoQXsebLMKKLCiKiX5CeUd9UU8yetqyEIoNcn8Eet8OLPO/x/XpaMZCV2UKf9nCEkrUPOisM5QRuFSlcrsi47DRwPW+HGCleemUQIEr0/r0ISbbhK7G7XvbvCuWQuMr9RBpYNcP+ahEDHnZxk4ITKLFgrQAmcxn63w9L1Bj3ew2OIepMZeFqiadvQ+vR+/Ho7YZvasGk2ayQxyno6alNdhYk9lbBEGT/ACxlinMe85r3GpIbuhCTZSWqX6GSJ1J1Xq/D0IZegERlFVYyL9fSfyt17CUUM92F0MZDGFOt7o07jo8ujTIt7UucsNUFeea9X4uhDYkIuAlrQJ9kNQXuLUKuHKe+K3IfC/QMm0/jlnEZi63tL1iu5HeJz+RTcR0X5wUhjt+DNRwf7wc3uJ5GS0rz6nx4sRfglEpeb05K3yt9dKeFPfM722qNDAuj6jp+mEQXtyYlCQkbmLrcmQkN6owrGw5C7YT72kWU01PbshDzJSSr3aPD+lb+p8WLFgoK4RVX8noVC/7uhpwN1crHv0fTD3i/whKWkIXWzSWVXJZF3uXTGyH4b+CQlo7lSMN2eFfIwXeQeSdvZhsS68OnqfFi8GOFTdkiL+sXpdpZuIc4t36Ppg8nSBeUiW4Sq2xfCD58Ih4x5f31amJT2EutCFxKTlVFlrPe4MpaYbyo6eR7piflYP20walhWrKvp/FiyJoLKN79NDLDGzSJlxyKo8fphdmrPzg3Q9LTTuK6U3sbCXhPQ5Ud5t2GxK7hoftuUNLcUZykPVR4ZhGtHarVf3LqcXdOsvT+LF+tLC5UCHGhsTJwNG0meNgiRSMuubPaYtWKMhZ0+Sdx9R8dNKtYYm6eI8/Ijsz+zoQ2qibEwZRU9X4Le1GPBqLsfj0/i6LPqoY2YlJtlQQnGgxycHmPgt8CwgRWUyfL8C+tQq3KgoVK+NJbuRLaro4Fp9qiyJfZaZl+wrXl1ulUjuWA3nTQvi77JGyPHzilDzRF4d/cj7kCsvfLRaQrFNebPgdSwuySp38DO4qaGPxf4is5VCwPLNsMnBVzjxhYRjWjt+C0LXU4BO/agyN+V/8ARptI5Ut8EAmeNDtkl3REd5RKM2/w+CbdObFDXL5/jGU0EtJVLD0O7hURTSqLOQfL+TkNVl1bu30sQTXNCFKVdLJu4xWfR2x+P/E8AYsDUtHg7OBiQo0lFPVjGrqXA0NocgQ5k5q5bDrZLb3qobbMczXlrfUomrZneaDs+XcpZrZ4vqkysPW6KLvWbl/XLJp7THFeg90lNPr+hLzk3wfqMkq7iBrpjbQmJ3JGSrPsRD3BIWjD4uj4fWSQWCx2nB28YJA5e9RpNqioZPcV+TgnXt5KIgrp4J75m1m9yIXhzRuhakh3Y3fBYKNcLqtBWdFVfYfttodQilZF7YiW1oojbl5jQxOh2HfZAoyzWVL5CFoSEjCfF/inZmbEW908H4sEhX/2WLf9ILEPVa9lUiuhhHmsoJ8f0pDyOdGpmhVwWDtmd7dbiddw1dXFKWSm8tj1WirwTrErbXf8BNxUUgqj2rH3yLD4ui/x690WP8DBcEu3szxSRalAq/h7ojBfPLaCAy74Wj7CeCtXkSeK4sLSHjiZAxbRUFbWyWxcsaXb4PAx9PwgMxCkZYqRg+LoucevTzCPJw8DCDUOzoSc3eO2WNW/M5MrUi5G3QbSSFLGNNVFnyJIhCxVSnqiCXf3JtcrNcit2lZLNkIidUrXF4GPpeCsbs+2HxdF/rvJEeSsPCxgT9Dk8KK9B1k3LMn/AIUzU06yZPGu5Z/kKOTtIhYu2Bj6GMZFrEXtTD4v8ZXzYeTh4WKJHUTUMesq7UI0FlaCVigVLTQXoBs2WQhsRT0wxkKwz4uizt9e70fw+hVGlZ/ruJUId/wEleJYn0q4hhM2iUrBsbG+sxnnPD4l0fA9Z4fgzw8J4eD0qX77WRZD/wBXiZpYa1bnuqauw2mM7Np2cCVg8yoEZTwqIalCZFJRWfBQy0Y2NjH0sYzghsPiXR5HrPDbiMy40WHg9OlDe1QQ6XkQts2Hap4JyKP7h0bSaRdp3kVDb1LRkJZRU20WFlS131Kaddx/JJI5uSRsb6mPBw/Xl4fEujyvWsYiVNX1F00CbHu88hDEzZGkVLlPebwvhHyrjWcEtgkKe9QtZlAJkkkjfWx4Kgv7WywvcLo8r1pMVsIF3fUaNN8OG6GClIIhswn+IbnWEOaj3SQhs0M9VyyuKNEUlkoKylQlEV3wkn0WMsyhCwGE7YXuOjzvVeVl2ZG8jxo6FE9f6Siw3IfeRdMX+FyZtCa+X7DTyecGNkH/AOd7Y3uF0eR6uX7iQ7k2gQucYLomznzHoPEEWjoRrA2Cwq2kNlkuQvrpgTTJ7t2Sv2FxiVSS5KWMnGCQdYkiyWrG1E5FJY7dlGynwi/IxJsqAt5pV6vN43uF0OkyzE01K9NkxsbbHgyGWd3zjR0VRebETpoaZj4I4QepeP0BYTCEmAnRmSSI5UaplCEzVx1KCXNlW9xcLvpFxJdX+sMU0Fa5CJJvRNQUsmXcSY5pLk37Bh9VWYm1Lt/Wf9J5QtmZWf3rhFlAxTnJR+XfovdsUI5Rf05cLIQz3VfPRBdzopGjEUoyHBJ8BAzWpKdBMQszRPuPpNVWbZk41Nct2Js1ChiIpmfzuMuS5LgbXNFDfmIC7yV9gSFuqoTZ9xrmWapE9ASoQmjfoWShw6m3KXD9E2qJZ3/YMbJUm/8AwzEiSSQkoS26L/CwYhDVD6b0ftWJFitlVuiEIWd236PkdHnpuUdJqc0JIqErI7iXcQmcidOyKIcj/KjcfOaacNhd0vdRpnhjazfcdUP7Uo937PBgLz1ecIqm9I3ZEPNb5lc7u7FaSoX5Kk2flOm/2xyG8BiEi7qbSuMdERm8Gz6v99Pzeh9tuz0eqHkOeRbF8mKfsYyqXS3gmN0GjPxcGo77TUipfg3MVuyFuX4tk8ELT4WgtZZP3g0HLifsokg7/a9jJYzz/hEA+CoUX836r/bpJjSQXIUsjgynI10cDYRGDY58tZa/rq819TuPKz/UVV0J+UyrwXNaBD0V5c7im+ijLaDGtWOJawMyyEXBFItqppgxuHybiQ0V2LMqJcsg3dCeLS+ufyXS13jhVf5t13+3RUExhYIK6slEshYNBCUsYRRk1frr819bpWrrz/jokkknCcJJJENRDVP1J+VO1/Zf1arTULcgQv313+3Q6+C7DIlqqrxgEaiw4EMDLTA/juxTLasj0PNfWiaaaTTo0x4692aXLENF079EkkkkkkkmmUWZU2qSaNCg2dxl9C/2weC3F8BeKO38bp5HYaaJ+Br+J+R/2F+RNyLlob/JCVWZvbwIIRJaL0fP9H2b0BW+JAax2b8E5OjJXRXp/XsoW1lb0kxX/wAnn+og8dXwNWfmMluRXzJ8wJ8EJK6JNzBs9G9gy1z/AJHaTrJirLG6E07eu0moRNbiC67X6JGl8DlElhF+UnLr6cYrat/8lKHNgYqWEr1GryiU7euthFqr4Cbgqa7ioktKdbTdpCGYTFk4MnSsv8pfQXD3XHNhrkk4Ev8AIYroLMlCfk9RcIL8Z/eLVcGvVwNczuN9oRcjGUJyhW2w6q3T9lrf6HMpbDyXuNGR8MaW6CSVhHZhIO8jjEjLFEbeBb69ioSqptqUooEvV9EDSqeXjp8nhsqKZ33/AEJIhElt/uhDeT02uGPKQbH9jZjw+7fuNr3H/cNv3G17ja95/P8A5g2v7BufshZqEjX7vkVgS4/9y//EACoQAQACAQIEBgMBAQEBAAAAAAEAETEhQRBRYXEgMIGRobHB0fBA4XDx/9oACAEBAAE/EP8AydQyhEMyS3PaOyNgDRpXZx5X7/8APEDmZmwPrE9zs8IN4+jK8/v/AM4ZgIT+ZB90EgJj3z/YglDlMbRMkot5ZZLIazSURMfLopbLZfBZNGIFgvWAb5lrBOSzEMsf8lQMsBvG9sDeJxWwYcGPh0jB8Fy5biXLlvAD5ywvUh0TOH/GURVsIUaROjBJrEHIMW2R2rldlLm8ejGDz0ZiSdjL3DFYsIuXL4F8mVzIcELqZ0iG8iHMIFl4Bve7CZk5YNH+PV0JQcgxfJ2g7TkRhIJ59DX6lpkSWS+Fy5fBZLORL5SK+mkVGlGdiVB2kvkJSVlZXnKc4W4F7EMJ+sxQIM7Abi4qJUun/HqPjK5gGbE3OQsPSda7+6NqW3DfsINgBJVcwwV1jFAv2W412NOkZrgrwdtaCHwo4X9rLEHVBonMlf8AOwTD9l+p/wBAgs9LUHdInLJWI7EHmdqJuh7sMIvBn7JRxGw5p/jdR41cE843xT5zqAalrABecaNSN0XWtsjzmx626mNGZRlGRgu2cHMcA1jdMZla96V5xn7JfEL7T/G4/wAx4SgDVMBLPnRxAPoA6TTXJ1RlHDFLd0cpoxW7seg7IBUpXOC6maVUQW/nGfs40cx3XAHwWTqcV6nl5P8AMeAAQlTAEt2ol8z/AHRwqV392akSU9Ew2UQAoCCzormMqoFEZc0VoMKgqUzApUWuJd0RMKlPOZ+zgTtDr6Q4oVhZd+AisfMtaKQVXQ7D+pa7wvnAd2ARSD9XkZP8xxuGr6mr+iQQqD1/lYEyqzsJUflm62FuDvo3PRkWUW1Q3XdN1YIKh9EhaRnRPw4AcmMIsT6oS85MezRfeFiB2G4+bn7OIzUwAG8RGHg98OqA5wGCQUtQmtLZ434fji3OH7e0QADbfm82E0ehj+Z9oSpcSwSzETXD0Jp9Vc253SE7DGYlak22iLrCZTVZ2y/Kebk7ODN49YmEuKk3SE32jPnWFX0cthCj1zGD3Rbg/GZToVkFcDmTQaTRN/WHxfh+OKwmfsj7i/U0jZqpSgMGIVDPBjHrFDpWr6jibTNcxWHV1SNDv0QgFA79bISh653FeaZpMWgBuQg8VQ/Ano+Zl7PAyvgkA2HsDnBIgz/SGDw9MtwHZNZYW/0nmQ0FWcOy4zLS/ULbueHZ/McLEY1+kQDGxUz+iW8BiEvwbp5/o6sDJhRlqt4xDCqsDSh3jxb9RYFzOwzHYyOmJQiuwdHXhW1of0jzMnZ4Ci1mgFij9vQmixeu8+QddQHETFnqSqW6aQ1IZjOB18Gz+YjCR7fxvXwSI8LYRjF2afVaEqBsVz0PSanUldtKo9z3+cLNMn66GVrpqfR7mLe391i7V4KbpWe+fKPp5mTs8DGu0IE6bIWO9KGv0HladAtFtz9mObLF9P8AwfBs/mOFbYq+hb8srZeUEIA9mA1VmjKxuXS1jpRw/KkHvxYxaialc6DU6kPx1HTsugTUC5poDDVNNosRqy3J4qBecxCmgFAAbAaBwqHM0jndTe7Hl5Ozg8AgjVdCWwFGrnscvLLixpJnuWjzNnhD3JT3xBw/mIxf/p0pgy6xTnHEHdCt7aak128ICpcXg0yIac2hr1h2vWFp73s3TXl+XsQVnE28F8iA21DWoXWjcsDmLjwUbtBnS7PLzdnHKa7Od+bpZlO1zj4F9YFnc4vyPqK43XsLLXOULMJe4kLDRYxle7BnkRdQIekgiLkfkeC46TYftUSVdEc2pOhFW1aXXFBu0vzqvJKiXB/O4ffGtufcGnE4X/oi2WzWWwXhcL45Ozi5gv1/NxOFImUtRHrKY3C8X5H1LXG33po7L6m6DjpggI6G+afRCXh6MC2uXlUH0FWJaJ1HnAH3JcuXMp9IEx7kKhrqLrRlzjloqD1tJZD4KAtDK1HQ1PVWmGkKK7ov7cV7bX1yafQjI2ApcpWnoQhZmGyjd/3JXj2/ycOKyioD0ywIWGU2+4y2tVLyuObs4MczB2fOGuUJ6uA1ROblB7mnB+R9QU9n6GO+0+uAYLDWqOAE9Eq61AoXYZvci0CYDDlc+hYvuHtmwz3ZPHBcF4daerQ6Ixu+R9XpJ0XFvIeSQoMgaa1kTp6PuNUEXloAGdCDdYXlatunUt2CJ5OX0lrqovUK8a4FaJYQQkso/QQquZeYepBEooLmfw8c/ZwY5n0vnCgzUzCPSpd/HeHyX1AIjh0ZqHL9lSZguB3Ne44F1Dz/AHTmMXKGAL/g9IlqSF+5oSgcTZgwOpRT0iN0cwA1FR7dhsrntMIR8KCxUT2EIpfulbcbH0xFW92zPSj6loJu13fK07Yk2wvQyXVqotSy7oftjWExKajwY5y78GRc0WtqXAfkxth1JYh2aJEeWtnoBwzdnFzPuecJ+RZ6MehHrEnMD2NcPlvqM5YiHaAaUkejA7la5fTC4JrYfqZdLe30gLIIhe/lJ3RvsUwEqDZIC7IZevBvNyevm8v8TY9RhHmBida7aqTd9lZQWiDp9w4VEC5GhEGsq14Z4jwNw5xdHpR+JSSZ11PV0zkQaHQjFSmjXfXhm7ODGfQ+cbLmJDQOTM466Q/PA11n9MZybq+H6eF1iXDaA7+v4YYIVLrpxyX9ehSt6h9m6VTlI2hZ4tDzLIbQawJqg5bS9ki92Zoh7ympRdY0b4IVHZ6kdc8AQcU4avF1uTl5JfYr/wDSHbKo1AGtQAUAAaAcuGbs4McwnZ/I88UfVmRFTda+OHy/54OP9wceIXdiSqpAm+1sBXVJOequyFiR3YwPItTa6pEFVbNC8LDbEs/gdIUOGirmtUIsG3Lax+py4aHY04GyGHieDMIzcSQQKb1LG479hDucM3Zxc/4GFPkvubJ8PwYK7P8APAqCwUdHSNk919S9TgSnbVHIMfxsOa/K4TLAbgspYpo0mlS5fRChBRonJzoEFCiwlJLdPLek6MN6feychBGgtWV+ojtrmmiYFhww4+bGPB4RYwrDT/ZocM3ZwY5lRnl5/wAj9zA4u+X40swH9DHCwVAN3EUJAqupXWbGRbUw0CuBwcUt0SUDA5MVVOTcCBgIUKHTQm3DJx8mMfC9ea9ep4M3ZwZvBq+h56sc0/MME+H4fJcToFheSVMruv2x9MMfgzrlrJcXH52GiiGIQ4OLWKCzqtOcRJhtBXSzitIouG8Xg8ZsWMcM3ZweHzvy8/XDlN1zt9uFDuPBWFr+v5ukU2sj52HqYq21P1YHSCVmCczgYjqHxOxsv28p6QwQA40+G8eDxX/Ds8M3AZvPm/l51h5KZc3Jq6SfFcKHeeCmV4UL88Cy7XlrH8TLS2P7yhvdEtBWq3tfdIc1Uc7LTPVgnuMToexfpCSWY9eENwq2nNoxBeEDi7j14PB8BdeThufgM3nxnnVjmD8w28FbvJPV/wCcKHfX4aVb9PSolM2f280yFu8c0EzQvXvGy+5+wITgm0stOJiBXZCwcznG8qwO+C+sMkw0oxr6IQONcy1f1G/gNjwYuBRV+O5fhm4DN58J9edpdJYYZidunocKHbb8DvMe0FxUsveqYiPOc9UUbEl3DlmBmGH4praJSINxwjNH++iTq0fpg2+iYW+P9YjNEhwGbY8HixRR0LEUNeO3wWHh/J087ogaOxpNCfSYFlgZr1HXgAp5N+C3SIeuYELQ5kNCcNCyCJlb2rEu4rtSzSBHsKSnBRLETTrkrBUfHWLSDCJ6YIbDQUcLRUV4PgYzKKGrdJ9Wrh4V2LFcPjOD5+HyM07zVWOgOesQ7Y9svEFHk34CG8IvvlznMdF9o4AvdMRCEOI8L8V8TpsKK/Kmv6lixgBFwkXq+48p2+HDjgirkPNtTY17piivtlw49ScQEdb4t3E7jKP4BBIQI3Tc0Mk0jAQecGKEHQxJVXYJogm2wQmzs8x5IxogCijWnkDVgIFtRTpEFqBBEsR4IDkSxLQy4NRo9jtQE7jS7IuPC7vGApKPcsndFHNAg5u/pNie8919R8OHE3lbFrQw0tj5eEIjM3bmBfQioveXfkeq1eIBHW+LmWy5Xywus9Dl3OU6xVZEoOtnW5oZ08sO0UGmNx5aw3wuPXVEGGPZJ8MD6Z0obtMarzBdtsOrg6hXnSwjAbYW6VCYdhQa8wmiu2AhdFJcP5wnUOcW8SK/gHsrqiYhQnXd8NXmYgYjmpMUszR3c1YAogQXlLeme9G8xXH4304OJqYCiOY3tvz5bWut7sFR61sStUvsTsPAAju+OLma5HRU8rfxGYlAoJpJejzeV7wiOn2kcJaRBtheSqXDvIW5MKfbt9cuMFMoqBWiC7IU1z0A6lCJ5h/Rr6MFm6eqqhlDJ8Lx2Eqn1t9vN4qYBWwJYhrAS/4+qytBJRQFKzUYmonAcg1ElmB+zRAjpt5LwUAEIfUEF8OFgGgHg/ocoTBh0uEywHoZUW9/48kAWzoH0RMuYoOsVINqnmsyJ9QXg+f+PFzE0aazs2p9JF2BTlrcIeMNQQwKVXptoMaQJolYM0iVjR6Yfr1wzTHQeSsy5ut/sV90I7vUdttZ6C/lJ0OH3JXKG6D4SU7Yx7QspWhtlSpcsCc/NculpWVsq6q92MnEM+VyCITY67dXh+L9IRhoCUBCw4YEbMkIOBw8/Efq9JticiCXcihD1XQiWCimvsc3fw/I+h4COcgZ2IcqfW0ddxhzqUIgDiF9SvwjocTW1ND8V0Ggwmp9goXxV5c1U6QH2Cc5OVu4jr38Z8PbvriWS9/xdTmQ/QvAFoB3FudsF9cXgDrtzuaLYbst9PifF+kMxJvHNaCKcxW2jNIVnWUsPRgZAOj7sRZUcpsa0HLAd3OKGrDyuhBZImtv/TxfwOR4hto28udO8I4g3ow7hYMyC3R+Ux9Oermg7YfqEON0Mlc45LjhxQ2FV6x+vsEoq9Je0pyByRbIcHYCNMpsHC3SkJKD/Wy9Yeu0zql+JbKWtsfqg8XxfpCKVL1gA1nI4JuZox5NOEVvad4QUICWgogBR6to9/KAAAADQDxfwOR4mNRVuHNkRLOJSAhBA8F8FIwJHdytoqEqijHonrB2OqPB5cKaCW7W/U+P4v0OCxXaInalYHddJrBRkvkvNwhEYE65AcJgyBlJzcQmHc0IsIOv8FvkfwuR4xrGiWI6IjBFvOXdYsDyh0HqPHUYQcOvFZZbVCpWGiPRJQlyhaDAWBiYdec+R8X6HDBhhhCK1/dX9HDNbzqn3KZqfQ7vZdl6nN0PuphxdD9OGD+CLszA/wBLUXOTyr2/kwCRYCjyflfryQGgwnL/AG/gLlhvZPe0SOoGR0ZZuTThbLi3wO2X7zH2wBQACgcvIdp0PqozCawc2oFAG3+P5H68tBEQRyM1djvo91JqLbzY70cn0zAJA1KsWkps2vssg2Hi6EErXPbyf6sZhBfbff8AkzJIntDh7kHqZgdoTp5lMp4L0bILPUZZg/siCDuZgZgZbQZdCGWC/wBoPydp0jMJtER/kNXpeo9YGihsxOpI8yaDQ9c/E3Q9ce5AFoe3nawffLyN2bKEUzI0EYADAB2APH87zNKDdqPnWWYdCMYDLw60rbvP/LQAeTudma4R5aP0lqneiAYNOpO0xqTc1fesGBB0jwo/bT4mGgI4R8rW5XIqDH4854YUesxC6Q/kCa+1yVMc7Zf3Mi+mD2JXBDLF6Ax+9iLDhkN9yAAAAaAf50EpBOTALFzHXxiEzOh/JNV9CfTTPl8JUzp3lm5BrCkDafoLMAvUH7JTlPQ/EBKqQKujsfNP6mKYKUlAPSNGg9oVlECFUbHSkpjWVQoLimU43UR3JrUrcjX6mSXoPdE1YPlqfgmv926e1J0wEKPj/cpkHvMt6BX1FY7o/KxXySP4j+CYdn1xgRbA7/qJmV6v6n9r9cDf/EcWW6R/OkHyn1YN0Pf9EPwpQb9cn5Zh9E2m+afgqhVD8gH1/wC5f//EADsRAAECAwQGBwQKAwAAAAAAAAECEQAhMQMSQWEQMkBRcYEgIkKhscHwMGJykQQjM1Jgc4KSouGy0eL/2gAIAQIBCT8A/F6ghIxJYQTdSpippFTQXTwj6OtsLRPWTXkoZwL0Bm2WajqIxKvWtDrPZR2Anh4wQSVuf2wQlIqcoWznGQgMcRDAkSGNR6OyFkpDk5N6aJPqjBNj4ZqzjmcSfVImIkBhnoP1iZKGLYK545vBZdmL3Mmh4hPWjtCY3LxGx1WXX+WKDmf8Y1lV6As7oq6R41hJsVvVJ6hzBqCO0IJJKmJxJY4/pjVWHR+cP9jw2OYSoIHACfeTAfecPnDPxB7xpD2YtAi0GN1WLbvNoCiEXiZVYqCeBLJfjH2rBVtl1eqOb3lco7CweTh4oQ44bFUrJ/kYNQ9ocSszA5CsEPu0glKkOrg4bx6mbmAbpk+IOB5VMVmhR4TSf5S0VNkPmA3lsW/zgzYt8W6HNy0I42agCg87xH6YqlTHyPPHRisJJ90DyvvygG0BooRrOj991QjrHtHsie+FSQlr3rjKCYLsKHfh4zhaiytd5fKkawksYPv5+2AITakFPuXj5FxFrYrJkbMnrFDylrBQaXDGAm4FNmbR3pk7wkItEhzuKRlo+zVJQwyVyxgFf3Ve60o11FyMQJhCebuqGJOurev1JOmig0JcAEHJTxqsCOLn2wYWiQoH3xJXgDFUrfk8Ka8AuyObf3OEsm7dJzJaEqloSLVA1ZsRle3R2aDC9RPyDt0SEKxzEVqpXtg67LrD8vtDz5ROAsKs52S27BOq+WGUKZOCBJPyx0h7En6xW47jx7MJCXtGHwhP/U+liPbgmytFdTeLU9nzREzVat68S/Nh0A6VBiMv6qImU2rg77MpF090832UAz74DgjveEPHVLkk5YCFhsfij0IrR8sB3y2Y3YWzCKDHJorff9Lxu2YAlOso0vbmgXZsR7w9ONII6WuT3bCZ3i3xPEk3KPVQIY6A5AkISUl+TcYaYpnFErmcbsButPhAcmpzaKiaTlFBKOWwJKga7wvhAIYMkHWvGpbQq6YYpu98VAlH3o3jxj1KKmQ4RzOxc9JlugsIMjXQanujCOexyMDpctmAgQWiY2zmPxF//8QAOhEAAQICBgUJBwQDAAAAAAAAAQACETEDEiFBUXEQMkBhgRMgIjBicpGx0QQzQlKhwfBggrLSosLh/9oACAEDAQk/AP1eCSmtfKqI7jGSaaOkh0TWMK2TiUCfTNPNETJsBqqkNIHEiUNlsF53LojG8qyDx5IykO3gotNSAzgjWbcboL5z5bJMlccS7QA8Om0qAA1WCWgisy7Flx+xUg23vFfg2O6xveUhLm2ESN/ijEqbZ9zY5kR4nm2EiP7kZ+gWrJqvENjwHksUNOCnPMYcZBS1hkZ+Wj5tjElf+FX6Jhp8fwQR5NwNrSrwSMq4IQmJ3QQ5Z5Mzq8GSVHRwwqhMqOEiLLYyIkmCGF/jNWtNrT12A8YJrx2lGsRHdVVoMu9onMZoAOjbmtUWDKP38gVxz03+cwjCBU4/SHXTaYcLlJwgUI1TVpBfUjP6RRBtiMlRvI7pQI3QRLXflyuHNABvRiIEfv66Tv5aJO96bgMfRMDn30pHSj5N0NDvPxmjWbcsed8JBHX64nlj6rjnzced8h6+TRH7DzT6haYm0jo1cQvaC0B0IFzoxgjyjCGNDI/GNZy9nJdNrbfcwmbFYHC0YOvCw52EOv3JnKEmBG6C9nrAujaL4DeME2NIYGrvBtTWhjvZgw2j3vJNGrwUK1cndVIHpz7zE90deNc2DdpgBef+yT2vhOBBRAcZNjb4T5hBImMDdFe4YIcfmzPlolJvd6+VUeEEdD+To3uJpH7hu4xVMylowwyMHRMgWIOg10Q64NBsEd0Va+l9nEGfDypLgTCSfWBoSWEGVIAciFSBtGw9FsYEgOtJxTiG0oq0g7ZH4WrXf0jlcPrHipnXPZw9dgMC2W9ua8dFGKVhJzBhMfdB7KXlREW6hNtqMGOf0xkfvfkFYRRDxL3f3V9G7+JXwkj/ACj/ALLUo7X53/14qZ1ArSZnfsJ7h34emgRF2aEXXEpgc4SPqL02Lmap4q0XhNDQ0RDRKuja82u7ANvipfCOzsfSHzXwTxlI+B52bu7cPudmc4J0U1rl0Dgcc9GNmV21GNnQPCW12fof/9k=";
//        Toast.makeText(this, "Image "+encodedImageData, Toast.LENGTH_SHORT).show();


        KycRequest kycRequest=new KycRequest();
        kycRequest.setPanCardNumber(pancardNumber);
//      kycRequest.setPanCardImage(imageString);
        kycRequest.setPanCardImage(encodedImageData);

        try{
            CheckConnection.api.addPancardDetails(tokenName,kycRequest).enqueue(new Callback<KycResponse>() {
                @Override
                public void onResponse(Call<KycResponse> call, Response<KycResponse> response) {
                    if (response.isSuccessful()){

                        if (response.code()==422){
                            Toast.makeText(KYC.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }else {
                          //  Toast.makeText(KYC.this,"Error ",Toast.LENGTH_SHORT).show();
                            Toast.makeText(KYC.this,"document upload"+response.body().getStatus(),Toast.LENGTH_SHORT).show();

                            editor.putString("UploadPanCard","True");
                            editor.apply();
                            editor.commit();

                            cardViewPanCard.setVisibility(View.GONE);

                        }


                        Toast.makeText(KYC.this,"document upload"+response.body().getStatus(),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(KYC.this,"Error ",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<KycResponse> call, Throwable t) {

                    Toast.makeText(KYC.this,"onFailure "+t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(KYC.this,"Exception "+e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }




    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    private void getUserDocuments(){

        SharedPreferences preferences = KYC.this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);//second parameter default value.
        String tokenName="Bearer "+retrivedToken;


        CheckConnection.api.getDocumentsDetails(tokenName).enqueue(new Callback<UserDocumentsResponse>() {
            @Override
            public void onResponse(Call<UserDocumentsResponse> call, Response<UserDocumentsResponse> response) {


                if (response.isSuccessful()){

                    try{
                        verify=response.body().getDocumentsData().getPanCardImageVerfiy();

                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(KYC.this, "  ", Toast.LENGTH_SHORT).show();
                    }



                    if (verify.isEmpty() || verify.length()==0 || verify==null || verify.equals(null)){

                        Toast.makeText(KYC.this, "", Toast.LENGTH_SHORT).show();

                    }

                    if(verify.equals("0")){


                        t57.setText("Pending");
                        i20.setVisibility(View.GONE);
                        t57.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Color6));
                        t92.setText("Please Wait We Check Your Information.");
                        cardViewPanCard.setVisibility(View.GONE);
//                        cc.setVisibility(View.GONE);
                        cc.setClickable(false);



                    }else if(verify.equals(1)){
                        t57.setText("Verified");
                        String penCarNumber = response.body().getDocumentsData().getPanCardNumber();
                        String cardImage=response.body().getDocumentsData().getPanCardImage();
                        t92.setText(String.valueOf(penCarNumber));
                        String fullImage=CheckConnection.image+cardImage;
//                        Glide.with(KYC.this).load(fullImage).into(imgPanCard);
                        cardViewPanCard.setVisibility(View.GONE);
                        cc.setVisibility(View.GONE);
                        cc.setClickable(false);
                        Toast.makeText(KYC.this, "nnx"+penCarNumber, Toast.LENGTH_SHORT).show();


                    }else if(verify.equals("2")){
                        t57.setText("Reject");
                        cc.setEnabled(true);
                        i20.setVisibility(View.VISIBLE);

                        t57.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Color1));
                        t92.setText("Please Verify Your Pan Card.");
                        cardViewPanCard.setVisibility(View.GONE);

                        cc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(layout.getVisibility()==View.GONE){
                                    layout.setVisibility(View.VISIBLE);
                                    i20.setImageResource(R.drawable.ic_baseline_expand_less_24);
                                }
                                else {
                                    layout.setVisibility(View.GONE);
                                    i20.setImageResource(R.drawable.ic_baseline_expand_more_24);
                                }
                            }
                        });

                    }else{
                        Toast.makeText(KYC.this,"Some Think went wrong   : ",Toast.LENGTH_SHORT).show();
                    }
//                    if(response.body().getDocumentsData()==null){
//
//                    }else {
//
//
//                    }




                }else{
                    Toast.makeText(KYC.this,"error  : ",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserDocumentsResponse> call, Throwable t) {

            }
        });

    }


    private void selectImage() {
       final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
//        final CharSequence[] options = {  "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(KYC.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent camera_intent
                            = new Intent(MediaStore
                            .ACTION_IMAGE_CAPTURE);

                    startActivityForResult(camera_intent, 100);

                }
                else
                if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent=new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 200);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
               /* imgPanCard = (ImageView) data.getExtras()
                        .get("data");*/

                // Set the image in imageview for display
           //     persistImage();
                //createImageString();
                imgPanCard.setImageBitmap(photo);
                Toast.makeText(KYC.this,";mkjhvg"+imgPanCard,Toast.LENGTH_SHORT).show();


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();

                 encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);



                Log.e("klfdlg",encodedString+"");


            } else if (requestCode == 200) {
            //    Uri selectedImage = data.getData();

                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    imgPanCard.setImageURI(selectedImageUri);

                }
            }
        }
    }


    private void DisplayProfile(){

        SharedPreferences preferences = KYC.this.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        String retrivedToken  = preferences.getString("TOKEN",null);//second parameter default value.
        String tokenName="Bearer "+retrivedToken;

        CheckConnection.api.getProfile(tokenName).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful()){
//                    API();
//                    Toast.makeText(Profile.this,"Suucc"+response.body().getData().getUsername(),Toast.LENGTH_SHORT).show();
                    String userName=response.body().getData().getUsername();
//                 String name=response.body().getData().get
                    String email=response.body().getData().getEmail();
                    String dob=response.body().getData().getDob();
                    String phone=response.body().getData().getPhone();
                    String state=response.body().getData().getState();

                    tvPhoneNumber.setText("91+ "+phone);





                }else {
                    Toast.makeText(KYC.this,"Token Expire",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(KYC.this,"onFailure",Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        getUserDocuments();
    }
}