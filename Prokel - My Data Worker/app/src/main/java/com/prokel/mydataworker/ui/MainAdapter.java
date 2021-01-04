package com.prokel.mydataworker.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.prokel.mydataworker.R;
import com.prokel.mydataworker.config.Constants;
import com.prokel.mydataworker.model.PegawaiModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private Context context;
    private List<PegawaiModel> list;
    private MainAdapter.Listener listener;

    MainAdapter(Context context, List<PegawaiModel> list, Listener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.item_pegawai, null);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder mainViewHolder, int i) {
        PegawaiModel model = list.get(i);
        Glide.with(context)
                .load(Constants.IMAGES_URL+model.getFoto())
                .apply(new RequestOptions().error(R.drawable.im_profile))
                .into(mainViewHolder.circleImageViewFoto);

        mainViewHolder.textViewNama.setText(model.getNama());
        mainViewHolder.textViewEmail.setText(model.getEmail());
        mainViewHolder.textViewLevel.setText(model.getLevel());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageViewFoto;
        private TextView textViewNama;
        private TextView textViewLevel;
        private TextView textViewEmail;
        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageViewFoto = itemView.findViewById(R.id.item_pegawai_circleimageview_foto);
            textViewNama = itemView.findViewById(R.id.item_pegawai_textview_nama);
            textViewEmail = itemView.findViewById(R.id.item_pegawai_textview_email);
            textViewLevel = itemView.findViewById(R.id.item_pegawai_textview_level);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onPegawaiLongClick(list.get(getAdapterPosition()).getId());
                    return true;
                }
            });
        }
    }

    public void replaceData(List<PegawaiModel> modelList){
        list = modelList;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onPegawaiClick(PegawaiModel pegawaiModel);

        void onPegawaiLongClick(String id);
    }
}
