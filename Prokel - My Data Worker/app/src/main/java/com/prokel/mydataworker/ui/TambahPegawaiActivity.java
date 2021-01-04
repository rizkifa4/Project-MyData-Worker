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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.prokel.mydataworker.R;
import com.prokel.mydataworker.model.MessageModel;
import com.prokel.mydataworker.network.APIService;
import com.prokel.mydataworker.util.Imageutils;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahPegawaiActivity extends AppCompatActivity implements Imageutils.ImageAttachmentListener {

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
    private CheckBox checkBoxDekstop;
    private EditText editTextKontak;
    private EditText editTextEmail;
    private RadioButton radioButtonAdmin;
    private RadioButton radioButtonPegawai;
    private Button buttonSimpan;
    private Button buttonBatal;
    private EditText editTextPassword;

    private Imageutils imageutils;
    private File fileImage;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pegawai);

        initData();

        imageutils = new Imageutils(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tambah Pegawai");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonPilihGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(TambahPegawaiActivity.this, "", "Menyimpan.....", true, false);
                saveDataPegawai();
            }
        });

        buttonBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveDataPegawai() {
        if (fileImage!=null) {
            String jk = "";
            String level = "";
            String agama = "";
            String keahlian = "";


            if (radioButtonLaki.isChecked()) {
                jk = "L";
            } else if (radioButtonPerempuan.isChecked()) {
                jk = "P";
            }

            if (checkBoxWeb.isChecked()) {
                keahlian += checkBoxWeb.getText().toString() + ",";
            }

            if (checkBoxMobile.isChecked()) {
                keahlian += checkBoxMobile.getText().toString() + ", ";
            }

            if (checkBoxDekstop.isChecked()) {
                keahlian += checkBoxDekstop.getText().toString();
            }

            if (radioButtonAdmin.isChecked() && radioButtonAdmin.isChecked()) {
                level = radioButtonAdmin.getText().toString();
            } else if (radioButtonPegawai.isChecked()) {
                level = radioButtonPegawai.getText().toString();
            }

            agama = spinnerAgama.getSelectedItem().toString();

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileImage);
            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("foto", fileImage.getName(), requestBody);

            RequestBody requestBodyNama = RequestBody.create(MediaType.parse("text/plain"), editTextNama.getText().toString());
            RequestBody requestBodyJk = RequestBody.create(MediaType.parse("text/plain"), jk);
            RequestBody requestBodyKeahlian = RequestBody.create(MediaType.parse("text/plain"), keahlian);
            RequestBody requestBodyAgama = RequestBody.create(MediaType.parse("text/plain"), agama);
            RequestBody requestBodyKontak = RequestBody.create(MediaType.parse("text/plain"), editTextKontak.getText().toString());
            RequestBody requestBodyEmail = RequestBody.create(MediaType.parse("text/plain"), editTextEmail.getText().toString());
            RequestBody requestBodyPassword = RequestBody.create(MediaType.parse("text/plain"), editTextPassword.getText().toString());
            RequestBody requestBodyLevel = RequestBody.create(MediaType.parse("text/plain"), level);

            Call<MessageModel> call = APIService.Factory.create().postTambahPegawai(requestBodyNama, requestBodyJk,
                    requestBodyKeahlian, requestBodyAgama, requestBodyKontak, requestBodyEmail,
                    requestBodyPassword, requestBodyLevel, multipartBody);

            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
               /* progressDialog.dismiss();
                setResult(Activity.RESULT_OK);
                finish();*/
                    progressDialog.dismiss();
                    Toast.makeText(TambahPegawaiActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(TambahPegawaiActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            progressDialog.dismiss();
            Toast.makeText(TambahPegawaiActivity.this, "Semua Field Harus Terisi", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        toolbar = findViewById(R.id.addpegawai_toolbar);
        buttonPilihGambar = findViewById(R.id.addpegawai_button_add_foto);
        imageViewFoto = findViewById(R.id.addpegawai_imageview_foto);
        editTextNama = findViewById(R.id.addpegawai_edittext_nama);
        editTextEmail = findViewById(R.id.addpegawai_edittext_email);
        editTextPassword = findViewById(R.id.addpegawai_edittext_password);
        editTextKontak = findViewById(R.id.addpegawai_edittext_kontak);
        radioButtonAdmin = findViewById(R.id.addpegawai_radiobutton_admin);
        radioButtonPegawai = findViewById(R.id.addpegawai_radiobutton_pegawai);
        radioButtonLaki = findViewById(R.id.addpegawai_radiobutton_laki);
        radioButtonPerempuan = findViewById(R.id.addpegawai_radiobutton_perempuan);
        checkBoxWeb = findViewById(R.id.addpegawai_checkbox_web);
        checkBoxDekstop = findViewById(R.id.addpegawai_checkbox_dekstop);
        checkBoxMobile = findViewById(R.id.addpegawai_checkbox_mobile);
        buttonSimpan = findViewById(R.id.addpegawai_button_simpan);
        buttonBatal = findViewById(R.id.addpegawai_button_batal);
        spinnerAgama = findViewById(R.id.addpegawai_spinner_agama);
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
