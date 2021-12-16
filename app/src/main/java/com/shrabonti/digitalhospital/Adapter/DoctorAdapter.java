package com.shrabonti.digitalhospital.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrabonti.digitalhospital.Model.DoctorModel;
import com.shrabonti.digitalhospital.R;
import com.shrabonti.digitalhospital.RecylerviewClickInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DoctorAdapter  extends RecyclerView.Adapter<DoctorAdapter.DoctorAdapter_Holder> {
    private Context mContext;
    private List<DoctorModel> mData;
    private RecylerviewClickInterface recylerviewClickInterface;

    public DoctorAdapter(Context mContext, List<DoctorModel> mData, RecylerviewClickInterface recylerviewClickInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.recylerviewClickInterface = recylerviewClickInterface;
    }

    @NonNull
    @Override
    public DoctorAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_doctor_item,parent,false); //connecting to cardview
        return new DoctorAdapter_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter_Holder holder, int position) {
        String dPhotoURL = mData.get(position).getDoctorPhotoUrl();
        Picasso.get().load(dPhotoURL).fit().centerCrop().into(holder.mDoctorImage);
        String dsTitle = mData.get(position).getDoctorName();
        int diViews = 1234;
        String dsBio = mData.get(position).getDoctorBio();

        holder.mDoctorTitleText.setText(dsTitle);
        holder.mDoctorDesignatinoText.setText("||  "+mData.get(position).getDoctorDesignation());
        holder.mDoctorRatingTxt.setText(String.valueOf(mData.get(position).getDoctoriRating()));
        holder.mDoctorTimeTableText.setText(mData.get(position).getDoctorTimeTable());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class DoctorAdapter_Holder extends RecyclerView.ViewHolder {

        ImageView mDoctorImage;
        TextView mDoctorTitleText, mDoctorDesignatinoText, mDoctorRatingTxt, mDoctorTimeTableText;


        public DoctorAdapter_Holder(@NonNull View itemView) {
            super(itemView);

            mDoctorImage = (ImageView) itemView.findViewById(R.id.doctor_card_item_img);
            mDoctorTitleText = (TextView)itemView.findViewById(R.id.doctor_card_title);
            mDoctorDesignatinoText = (TextView)itemView.findViewById(R.id.doctor_card_deisgnation);
            mDoctorRatingTxt = (TextView)itemView.findViewById(R.id.doctor_card_rating);
            mDoctorTimeTableText = (TextView)itemView.findViewById(R.id.doctor_card_time_table);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewClickInterface .onDoctorItemClick(getAdapterPosition());
                }
            });

        }
    }



}
