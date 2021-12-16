package com.shrabonti.digitalhospital.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrabonti.digitalhospital.Model.AppointmentModel;
import com.shrabonti.digitalhospital.R;
import com.shrabonti.digitalhospital.RecylerviewClickInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.OrderAdapter_Holder> {
    private Context mContext;
    private List<AppointmentModel> mData;
    private RecylerviewClickInterface recylerviewClickInterface;

    public AppointmentAdapter(Context mContext, List<AppointmentModel> mData, RecylerviewClickInterface recylerviewClickInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.recylerviewClickInterface = recylerviewClickInterface;
    }

    @NonNull
    @Override
    public AppointmentAdapter.OrderAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_order, parent, false); //connecting to cardview
        return new AppointmentAdapter.OrderAdapter_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.OrderAdapter_Holder holder, int position) {
        //String dPhotoURL = mData.get(position).getProductPhotoUrl();
        //Picasso.get().load(dPhotoURL).fit().centerCrop().into(holder.mL4ItemImageView);
        String dsTitle = mData.get(position).getOrderUID();
        Date date =   mData.get(position).getDoctorOrderTime();
        if(date != null){
            SimpleDateFormat df2 = new SimpleDateFormat("hh:mma  dd/MMM/yy");
            String dateText = df2.format(date);
            holder.mL4ItemBioText.setText(dateText);
        }else{
            holder.mL4ItemBioText.setText("08:34PM 06/Nov/2021");
        }


        //String dsBio = mData.get(position).getProductBio();
        int diViews = 1234;
        //String dsBio = mData.get(position).getProductBio();
        holder.mL4ItemTitleText.setText("Appointment ID: "+dsTitle.substring(0,6));
        //holder.mL4ItemBioText.setText(dsBio);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class OrderAdapter_Holder extends RecyclerView.ViewHolder {

        ImageView mL4ItemImageView;
        TextView mL4ItemTitleText;
        TextView mL4ItemBioText;


        public OrderAdapter_Holder(@NonNull View itemView) {
            super(itemView);

            mL4ItemImageView = (ImageView) itemView.findViewById(R.id.card_order_imagecard);
            mL4ItemTitleText = (TextView) itemView.findViewById(R.id.card_order_title);
            mL4ItemBioText = (TextView) itemView.findViewById(R.id.card_order_bio);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   recylerviewClickInterface.onItemClick(getAdapterPosition());
                }
            });

        }
    }


}
