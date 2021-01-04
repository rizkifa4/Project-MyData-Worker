package com.prokel.mydataworker.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.prokel.mydataworker.R;
import com.prokel.mydataworker.config.Constants;
import com.prokel.mydataworker.model.MessageModel;
import com.prokel.mydataworker.model.SelectPegawaiModel;
import com.prokel.mydataworker.network.APIService;
import com.prokel.mydataworker.util.Imageutils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahPegawaiActivity extends AppCompatActivity implements Imageutils.ImageAttachmentListener {

    //Declaring views
    private Button buttonPilihGambar;
    private ImageView imageViewFoto;
    private Toolbar toolbar;
    private EditText editTextNama;
    private RadioButton radioButtonLaki;
    private RadioButton radioButtonPerempuan;
    private Spinner spinnerAgama;
    private CheckBox checkBoxWeb;
    private CheckBox checkBoxMobile;
    private CheckBox checkBoxDesktop;
    private EditText editTextKontak;
    private EditText editTextEmail;
    private RadioButton radioButtonAdmin;
    private RadioButton radioButtonPegawai;
    private Button buttonSimpan;
    private Button buttonBatal;


    private Imageutils imageutils;
    private File fileImage;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_pegawai);
        progressDialog = ProgressDialog.show(UbahPegawaiActivity.this, "", "Load Data.....", true, false);

        initData();

        imageutils = new Imageutils(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Ubah Pegawai");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle dataExtra = getIntent().getExtras();
        final String id = dataExtra.getString("id");
        loadData(id);

        buttonPilihGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });


        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(UbahPegawaiActivity.this, "", "Menyimpan.....", true, false);
                saveDataPegawai(id);
            }
        });

        buttonBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadData(String id){
        Call<SelectPegawaiModel> call = APIService.Factory.create().postSelectPegawai(id);
        call.enqueue(new Callback<SelectPegawaiModel>() {
            @Override
            public void onResponse(Call<SelectPegawaiModel> call, Response<SelectPegawaiModel> response) {
                progressDialog.dismiss();
                editTextNama.setText(response.body().getNama());
                editTextKontak.setText(response.body().getKontak());
                editTextEmail.setText(response.body().getEmail());

                if (response.body().getJk().equals("L")) {
                    radioButtonLaki.setChecked(true);
                }else{
                    radioButtonPerempuan.setChecked(true);
                }

                if (response.body().getLevel().equals("Admin")) {
                    radioButtonAdmin.setChecked(true);
                }else{
                    radioButtonPegawai.setChecked(true);
                }

                imageViewFoto.setVisibility(View.VISIBLE);
                Glide.with(UbahPegawaiActivity.this)
                        .load(Constants.IMAGES_URL+response.body().getFoto())
                        .into(imageViewFoto);

                ArrayAdapter<String> adapter;
                List<String> agama;

                agama = new ArrayList<>();
                agama.add(response.body().getAgama());
                agama.add("Islam");
                agama.add("Katolik");
                agama.add("Protestan");
                agama.add("Budha");
                agama.add("Hindu");
                agama.add("Konghucu");
                adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, agama);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAgama.setAdapter(adapter);

                String keahlian = response.body().getKeahlian();
                if(keahlian.matches("(.*)Web(.*)") && keahlian.matches("(.*)Mobile(.*)") && keahlian.matches("(.*)Desktop(.*)")){
                    checkBoxWeb.setChecked(true);
                    checkBoxMobile.setChecked(true);
                    checkBoxDesktop.setChecked(true);
                }else if(keahlian.matches("(.*)Web(.*)") && keahlian.matches("(.*)Mobile(.*)")){
                    checkBoxWeb.setChecked(true);
                    checkBoxMobile.setChecked(true);
                }else if(keahlian.matches("(.*)Web(.*)") && keahlian.matches("(.*)Desktop(.*)")){
                    checkBoxWeb.setChecked(true);
                    checkBoxDesktop.setChecked(true);
                }else if(keahlian.matches("(.*)Desktop(.*)") && keahlian.matches("(.*)Mobile(.*)")){
                    checkBoxDesktop.setChecked(true);
                    checkBoxMobile.setChecked(true);
                }else if(keahlian.matches("(.*)Web(.*)")){
                    checkBoxWeb.setChecked(true);
                }else if(keahlian.matches("(.*)Mobile(.*)")){
                    checkBoxMobile.setChecked(true);
                }else if(keahlian.matches("(.*)Desktop(.*)")){
                    checkBoxDesktop.setChecked(true);
                }else{
                    checkBoxWeb.setChecked(false);
                    checkBoxMobile.setChecked(false);
                    checkBoxDesktop.setChecked(false);
                }

            }

            @Override
            public void onFailure(Call<SelectPegawaiModel> call, Throwable t) {
                Toast.makeText(UbahPegawaiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveDataPegawai(String id) {

        String jk = "";
        String level = "";
        String keahlian = "";

        if (radioButtonLaki.isChecked()) {
            jk = "L";
        } else if (radioButtonPerempuan.isChecked()) {
            jk = "P";
        }

        if (checkBoxWeb.isChecked()) {
            keahlian += checkBoxWeb.getText().toString() + ", ";
        }

        if (checkBoxMobile.isChecked()) {
            keahlian += checkBoxMobile.getText().toString() + ", ";
        }

        if (checkBoxDesktop.isChecked()) {
            keahlian += checkBoxDesktop.getText().toString();
        }


        if (radioButtonAdmin.isChecked() && radioButtonAdmin.isChecked()) {
            level = radioButtonAdmin.getText().toString();
        } else if (radioButtonPegawai.isChecked()) {
            level = radioButtonPegawai.getText().toString();
        }


        RequestBody requestBodyId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody requestBodyNama = RequestBody.create(MediaType.parse("text/plain"), editTextNama.getText().toString());
        RequestBody requestBodyJk = RequestBody.create(MediaType.parse("text/plain"), jk);
        RequestBody requestBodyKeahlian = RequestBody.create(MediaType.parse("text/plain"), keahlian);
        RequestBody requestBodyAgama = RequestBody.create(MediaType.parse("text/plain"), spinnerAgama.getSelectedItem().toString());
        RequestBody requestBodyKontak = RequestBody.create(MediaType.parse("text/plain"), editTextKontak.getText().toString());
        RequestBody requestBodyEmail = RequestBody.create(MediaType.parse("text/plain"), editTextEmail.getText().toString());
        RequestBody requestBodyLevel = RequestBody.create(MediaType.parse("text/plain"), level);

        if(fileImage != null){
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileImage);
            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("foto", fileImage.getName(), requestBody);

            Call<MessageModel> call = APIService.Factory.create().postUbahPegawai(requestBodyId, requestBodyNama, requestBodyJk,
                    requestBodyKeahlian, requestBodyAgama, requestBodyKontak, requestBodyEmail,
                    requestBodyLevel, multipartBody);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    progressDialog.dismiss();
                    Toast.makeText(UbahPegawaiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(UbahPegawaiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Call<MessageModel> call = APIService.Factory.create().postUbahPegawai(requestBodyId, requestBodyNama, requestBodyJk,
                    requestBodyKeahlian, requestBodyAgama, requestBodyKontak, requestBodyEmail,
                    requestBodyLevel, null);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    progressDialog.dismiss();
                    Toast.makeText(UbahPegawaiActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(UbahPegawaiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initData() {
        toolbar = findViewById(R.id.ubah_pegawai_toolbar);
        buttonPilihGambar = findViewById(R.id.ubah_pegawai_button_add_foto);
        imageViewFoto = findViewById(R.id.ubah_pegawai_imageview_foto);
        editTextNama = findViewById(R.id.ubah_pegawai_edittext_nama);
        editTextEmail = findViewById(R.id.ubah_pegawai_edittext_email);
        editTextKontak = findViewById(R.id.ubah_pegawai_edittext_kontak);
        radioButtonAdmin = findViewById(R.id.ubah_pegawai_radiobutton_admin);
        radioButtonPegawai = findViewById(R.id.ubah_pegawai_radiobutton_pegawai);
        radioButtonLaki = findViewById(R.id.ubah_pegawai_radiobutton_laki);
        radioButtonPerempuan = findViewById(R.id.ubah_pegawai_radiobutton_perempuan);
        checkBoxWeb = findViewById(R.id.ubah_pegawai_checkbox_web);
        checkBoxDesktop = findViewById(R.id.ubah_pegawai_checkbox_dekstop);
        checkBoxMobile = findViewById(R.id.ubah_pegawai_checkbox_mobile);
        buttonSimpan = findViewById(R.id.ubah_pegawai_button_simpan);
        buttonBatal = findViewById(R.id.ubah_pegawai_button_batal);
        spinnerAgama = findViewById(R.id.ubah_pegawai_spinner_agama);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        imageViewFoto.setVisibility(View.VISIBLE);
        imageViewFoto.setImageBitmap(file);

        String path = imageutils.getPath(uri);
        fileImage = new File(path);
    }
}
