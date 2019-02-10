package com.example.fahrizal.pemilikmotor;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private MainActivity mainActivity;

    public CustomInfoWindow(Context ctx){
        context = ctx;
        mainActivity = new MainActivity();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.info, null);

        //inisiasi variabel
        TextView nama_pemilik = view.findViewById(R.id.nama);
        TextView nama_bengkel = view.findViewById(R.id.namaBengkel);
        TextView jam = view.findViewById(R.id.jam);
        ImageView img = view.findViewById(R.id.foto);
        LinearLayout panggil = view.findViewById(R.id.llPanggil);


        final Profil profil = (Profil) marker.getTag();

        nama_pemilik.setText(profil.getNama());
        nama_bengkel.setText(profil.getNamaTambal());

        //set image to img from url with glide
        Glide.with(context).load(profil.getUrlFotoProfil()).apply(new RequestOptions().centerCrop()).into(img);

        if (profil.getTxtbuka() != null && profil.getTxttutup() != null){
            jam.setText(profil.getTxtbuka()+" - "+profil.getTxttutup());
        }else {
            jam.setText("-");
        }

        panggil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Telpon", Toast.LENGTH_SHORT).show();
                mainActivity.telpon(profil.getNomorHp());
            }
        });

        return view;
    }
}
