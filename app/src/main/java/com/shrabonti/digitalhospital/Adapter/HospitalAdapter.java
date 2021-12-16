package com.shrabonti.digitalhospital.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrabonti.digitalhospital.Model.HospitalModel;
import com.shrabonti.digitalhospital.R;
import com.shrabonti.digitalhospital.RecylerviewClickInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalAdapter_Holder> {
    private Context mContext;
    private List<HospitalModel> mData;
    private RecylerviewClickInterface recylerviewClickInterface;
    public HospitalAdapter (android.content.Context mContext, List<HospitalModel> mData, RecylerviewClickInterface recylerviewClickInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.recylerviewClickInterface = recylerviewClickInterface;
    }

    @NonNull
    @Override
    public HospitalAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_hospital,parent,false); //connecting to cardview
        return new HospitalAdapter_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalAdapter_Holder holder, int position) {
        String dPhotoURL = mData.get(position).getHospitalPhotoUrl();
        Picasso.get().load(dPhotoURL).fit().centerCrop().into(holder.mItemImageView);
        String dsTitle = mData.get(position).getHospitalName();
        int diViews = 1234;
        String dsBio = mData.get(position).getHospitalBio();
        holder.mItemTitleText.setText(dsTitle);
        holder.mItemBioText.setText(dsBio);
        holder.mItemViewsText.setText(String.valueOf(diViews));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class HospitalAdapter_Holder extends RecyclerView.ViewHolder {

        ImageView mItemImageView;
        TextView mItemTitleText;
        TextView mItemViewsText;
        TextView mItemBioText;

        public HospitalAdapter_Holder(@NonNull View itemView) {
            super(itemView);

            mItemImageView = (ImageView) itemView.findViewById(R.id.hospital_item_img);
            mItemTitleText = (TextView)itemView.findViewById(R.id.hospital_title_id);
            mItemViewsText = (TextView)itemView.findViewById(R.id.hospital_views);
            mItemBioText = (TextView)itemView.findViewById(R.id.hospital_bio_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewClickInterface .onItemClick(getAdapterPosition());
                }
            });
        }
    }



}
