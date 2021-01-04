package com.prokel.mydataworker.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prokel.mydataworker.R;
import com.prokel.mydataworker.config.Constants;
import com.prokel.mydataworker.model.MessageModel;
import com.prokel.mydataworker.model.PegawaiModel;
import com.prokel.mydataworker.model.SelectPegawaiModel;
import com.prokel.mydataworker.network.APIService;
import com.prokel.mydataworker.util.SessionUtils;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainAdapter.Listener {

    private FloatingActionButton floatingActionButtonAdd;
    private RecyclerView recyclerViewPegawai;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    private MainAdapter adapter;

    private ProgressDialog progressDialog;

    private TextView link, aktivasi;
    private Spanned Text;
    private View garis;
    SwipeRefreshLayout swLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Aplikasi");


        setupRecyclerView();

        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TambahPegawaiActivity.class);
                startActivityForResult(intent, 1001);
            }
        });
        aktivasi();
        loadAllPegawai();

        // Mengeset listener yang akan dijalankan saat layar di refresh/swipe
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swLayout.setRefreshing(false);
                        aktivasi();
                        loadAllPegawai();
                    }
                }, 5000);
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this){
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        adapter = new MainAdapter(this, new ArrayList<PegawaiModel>(), this);
        recyclerViewPegawai.setLayoutManager(linearLayoutManager);
        recyclerViewPegawai.setAdapter(adapter);
    }

    private void loadAllPegawai() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewPegawai.setVisibility(View.GONE);
        Call<PegawaiModel.PegawaiDataModel> call = APIService.Factory.create().getPegawai(SessionUtils.getLoggedUser(MainActivity.this).getId());
        call.enqueue(new Callback<PegawaiModel.PegawaiDataModel>() {
            @Override
            public void onResponse(Call<PegawaiModel.PegawaiDataModel> call, Response<PegawaiModel.PegawaiDataModel> response) {
                progressBar.setVisibility(View.GONE);
                recyclerViewPegawai.setVisibility(View.VISIBLE);
                assert response.body() != null;
                adapter.replaceData(response.body().getResults());
            }
            @Override
            public void onFailure(Call<PegawaiModel.PegawaiDataModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        floatingActionButtonAdd = findViewById(R.id.beranda_floatingactionbutton_add);
        recyclerViewPegawai = findViewById(R.id.beranda_recyclerview);
        progressBar = findViewById(R.id.progressbar);
        swLayout = findViewById(R.id.refresh);
        progressBar = findViewById(R.id.progressbar);
        aktivasi = findViewById(R.id.aktivasi);
        link = findViewById(R.id.linkAktivasi);
        garis = findViewById(R.id.garisbawah);
    }


    private void aktivasi() {
        Call<SelectPegawaiModel> api = APIService.Factory.create().postSelectPegawai(SessionUtils.getLoggedUser(MainActivity.this).getId());
        api.enqueue(new Callback<SelectPegawaiModel>() {
            @Override
            public void onResponse(Call<SelectPegawaiModel> call, retrofit2.Response<SelectPegawaiModel> response) {
                if (response.isSuccessful()){
                    if (response.body().getMessage().equalsIgnoreCase("Berhasil")){
                        if (response.body().getAktivasi().equals("T")){
                            aktivasi.setVisibility(View.VISIBLE);
                            Text = Html.fromHtml("<a href='"+ Constants.URL+"API/aktivasi.php?pesan=aktivasi&id="+
                                    response.body().getToken()+"'>Lakukan Aktivasi</a>");
                            link.setMovementMethod(LinkMovementMethod.getInstance());
                            link.setText(Text);



                            link.setVisibility(View.VISIBLE);
                            garis.setVisibility(View.VISIBLE);
                        }else {
                            aktivasi.setVisibility(View.GONE);
                            link.setVisibility(View.GONE);
                            garis.setVisibility(View.GONE);
                        }
                    }
                }else {
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<SelectPegawaiModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onPegawaiClick(PegawaiModel pegawaiModel) {
    }

    @Override
    public void onPegawaiLongClick(final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_edit_delete);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView textViewEdit = dialog.findViewById(R.id.dialog_edit);
        TextView textViewDelete = dialog.findViewById(R.id.dialog_delete);

        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UbahPegawaiActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        textViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading....", true, false);

                Call<MessageModel> call = APIService.Factory.create().postDeletePegawai(id);
                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        dialog.dismiss();
                        progressDialog.dismiss();
                        loadAllPegawai();
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        dialog.dismiss();
                        progressDialog.dismiss();
                        Log.e("ERROR", t.getMessage());
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK){
            loadAllPegawai();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_aplikasi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profil:
                Intent intents = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intents);
                break;
            case R.id.menu_logout:
                SessionUtils.logout(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}