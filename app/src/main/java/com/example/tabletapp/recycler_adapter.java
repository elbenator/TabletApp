package com.example.tabletapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.ViewHolder> {

    private RecyclerClick listener; //for the click
    List<String> titles;
    List<Integer> images;
    List<String> hiddenId;
    List<String> prices;
    List<String> category;
    Context context;
    LayoutInflater inflater;

    public recycler_adapter(Context ctx, List<String> titles, List<Integer> images, List<String> hiddenId, List<String> prices, RecyclerClick listener){ // for the click
        this.titles = titles;
        this.images=images;
        this.hiddenId = hiddenId;
        this.prices = prices;
        this.inflater = LayoutInflater.from(ctx);
        this.listener = listener; // for the click
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.hiddenId.setText(hiddenId.get(position));
        holder.getPrice.setText(prices.get(position));
        holder.gridIcon.setImageResource(images.get(position));

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{//======For the click 3:10
        public TextView title, getPrice, hiddenId;
        public ImageView gridIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_nameF);
            gridIcon = itemView.findViewById(R.id.imageViewF);

            getPrice = itemView.findViewById(R.id.priceF);
            hiddenId = itemView.findViewById(R.id.hidden_idF);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //======For the click
            listener.onClick(itemView, getAdapterPosition());
        }
    }



    //======For the click
    public interface RecyclerClick{
        void onClick(View v, int position);
    }
}
