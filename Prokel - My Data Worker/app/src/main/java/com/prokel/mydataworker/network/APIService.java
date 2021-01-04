package com.prokel.mydataworker.network;

import com.prokel.mydataworker.config.Constants;
import com.prokel.mydataworker.model.MessageModel;
import com.prokel.mydataworker.model.PegawaiModel;
import com.prokel.mydataworker.model.SelectPegawaiModel;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

    @FormUrlEncoded
    @POST("API/login.php")
    Call<PegawaiModel.PegawaiDataModel> postLogin(@Field("email") String email,
                                                   @Field("password") String password);

    @FormUrlEncoded
    @POST("API/lupaPassword.php")
    Call<MessageModel> postLupa(@Field("email") String email);

    @FormUrlEncoded
    @POST("API/selectPegawai.php")
    Call<SelectPegawaiModel> postSelectPegawai(@Field("id") String id);

    @FormUrlEncoded
    @POST("API/getPegawai.php")
    Call<PegawaiModel.PegawaiDataModel> getPegawai(@Field("id") String id);

    @Multipart
    @POST("API/tambahPegawai.php")
    Call<MessageModel> postTambahPegawai(@Part("nama") RequestBody nama,
                                         @Part("jk") RequestBody jk,
                                         @Part("keahlian") RequestBody keahlian,
                                         @Part("agama") RequestBody agama,
                                         @Part("kontak") RequestBody kontak,
                                         @Part("email") RequestBody email,
                                         @Part("password") RequestBody password,
                                         @Part("level") RequestBody level,
                                         @Part MultipartBody.Part foto);

    @Multipart
    @POST("API/ubahPegawai.php")
    Call<MessageModel> postUbahPegawai(@Part("id") RequestBody id,
                                       @Part("nama") RequestBody nama,
                                       @Part("jk") RequestBody jk,
                                       @Part("keahlian") RequestBody keahlian,
                                       @Part("agama") RequestBody agama,
                                       @Part("kontak") RequestBody kontak,
                                       @Part("email") RequestBody email,
                                       @Part("level") RequestBody level,
                                       @Part MultipartBody.Part foto);

    @Multipart
    @POST("API/profilPegawai.php")
    Call<MessageModel> postProfil(@Part("id") RequestBody id,
                                       @Part("nama") RequestBody nama,
                                       @Part("jk") RequestBody jk,
                                       @Part("keahlian") RequestBody keahlian,
                                       @Part("agama") RequestBody agama,
                                       @Part("kontak") RequestBody kontak,
                                       @Part("email") RequestBody email,
                                       @Part("password_lama") RequestBody passwordLama,
                                       @Part("password_baru") RequestBody passwordBaru,
                                       @Part("password_ulang") RequestBody passwordUlang,
                                       @Part MultipartBody.Part foto);

    @FormUrlEncoded
    @POST("API/hapusPegawai.php")
    Call<MessageModel> postDeletePegawai(@Field("id") String id);

    class Factory{
        public static APIService create(){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.connectTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);

            OkHttpClient client = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit.create(APIService.class);
        }
    }
}
