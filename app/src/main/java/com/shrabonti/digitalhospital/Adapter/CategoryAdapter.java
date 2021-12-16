package com.shrabonti.digitalhospital.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrabonti.digitalhospital.Model.CategoryModel;
import com.shrabonti.digitalhospital.R;
import com.shrabonti.digitalhospital.RecylerviewClickInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryAdapter_Holder> {
    private Context mContext;
    private List<CategoryModel> mData;
    private RecylerviewClickInterface recylerviewClickInterface;
    boolean HospitalOwnerFoundBool = false;
    public CategoryAdapter (android.content.Context mContext, List<CategoryModel> mData, RecylerviewClickInterface recylerviewClickInterface, boolean HospitalOwnerFoundBool) {
        this.mContext = mContext;
        this.mData = mData;
        this.recylerviewClickInterface = recylerviewClickInterface;
        this.HospitalOwnerFoundBool = HospitalOwnerFoundBool;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_category,parent,false); //connecting to cardview
        return new CategoryAdapter.CategoryAdapter_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryAdapter_Holder holder, int position) {
        String dPhotoURL = mData.get(position).getCategoryPhotoUrl();
        Picasso.get().load(dPhotoURL).fit().centerCrop().into(holder.mItemImageView);
        String dsTitle = mData.get(position).getCategoryName();
        int diViews = 1234;
        String dsBio = mData.get(position).getCategoryBio();
        holder.mItemTitleText.setText(dsTitle);

        if(HospitalOwnerFoundBool == true){
            holder.mItemImageAddCircle.setVisibility(View.VISIBLE);
        }else{
            holder.mItemImageAddCircle.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class CategoryAdapter_Holder extends RecyclerView.ViewHolder {

        ImageView mItemImageView;
        ImageView mItemImageAddCircle;
        TextView mItemTitleText;


        public CategoryAdapter_Holder(@NonNull View itemView) {
            super(itemView);

            mItemImageView = (ImageView) itemView.findViewById(R.id.card_category_imagecard);
            mItemImageAddCircle = (ImageView) itemView.findViewById(R.id.card_category_add_circle_img);
            mItemTitleText = (TextView)itemView.findViewById(R.id.card_category_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewClickInterface .onItemClick(getAdapterPosition());
                }
            });
            mItemImageAddCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewClickInterface .onAddCircleClick(getAdapterPosition());
                }
            });
        }
    }



}
