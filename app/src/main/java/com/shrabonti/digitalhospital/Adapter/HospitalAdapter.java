package com.shrabonti.digitalhospital.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrabonti.digitalhospital.HospitalModel;
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
        view = mInflater.inflate(R.layout.card_book_item,parent,false); //connecting to cardview
        return new HospitalAdapter_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalAdapter_Holder holder, int position) {
        String dPhotoURL = mData.get(position).getHospitalPhotoUrl();
        Picasso.get().load(dPhotoURL).fit().centerCrop().into(holder.mL4ItemImageView);
        String dsTitle = mData.get(position).getHospitalName();
        int diViews = 1234;
        String dsBio = mData.get(position).getHospitalBio();
        holder.mL4ItemTitleText.setText(dsTitle);
        holder.mL4ItemBioText.setText(dsBio);
        holder.mL4ItemViewsText.setText(String.valueOf(diViews));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class HospitalAdapter_Holder extends RecyclerView.ViewHolder {

        ImageView mL4ItemImageView;
        TextView mL4ItemTitleText;
        TextView mL4ItemViewsText;
        TextView mL4ItemBioText;

        public HospitalAdapter_Holder(@NonNull View itemView) {
            super(itemView);

            mL4ItemImageView = (ImageView) itemView.findViewById(R.id.level_d_item_img);
            mL4ItemTitleText = (TextView)itemView.findViewById(R.id.level_d_title_id);
            mL4ItemViewsText = (TextView)itemView.findViewById(R.id.level_d_views);
            mL4ItemBioText = (TextView)itemView.findViewById(R.id.level_d_bio_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewClickInterface .onItemClick(getAdapterPosition());
                }
            });
        }
    }



}
