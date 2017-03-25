package com.example.android.graduationdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.graduationdemo.firstaid.FirstAidList;

import java.util.ArrayList;

/**
 * Created by Abshafi on 3/25/2017.
 */

public class FirstAidCustomAdapter extends RecyclerView.Adapter<FirstAidCustomAdapter.FirstAidViewHolder> {


    private ArrayList<FirstAidList> arrayList;
    private Context mContext;
    private LayoutInflater inflater;


    public FirstAidCustomAdapter(ArrayList<FirstAidList> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class FirstAidViewHolder extends RecyclerView.ViewHolder
    {
         TextView textView;
         ImageView imageView;

         FirstAidViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.one_row_text);
            imageView = (ImageView) itemView.findViewById(R.id.one_row_image);

        }
    }
    @Override
    public FirstAidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.one_row_recycler,parent,false);

        return new FirstAidViewHolder(rootView) {
        };
    }


    @Override
    public void onBindViewHolder(FirstAidViewHolder holder, int position) {


        holder.textView.setText(arrayList.get(position).getName());
        holder.imageView.setImageResource(arrayList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
