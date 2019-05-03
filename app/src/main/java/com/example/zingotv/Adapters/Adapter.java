package com.example.zingotv.Adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zingotv.Models.JSONData;
import com.example.zingotv.Models.ListsEvents;
import com.example.zingotv.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter extends RecyclerView.Adapter {

    private static final String TAG = "dpadl";
    private JSONData jsonData;
    private Context mContext;
    int focusedItem = 0;
    RecyclerView recyclerView;
    List eventPositionList=new ArrayList<Integer>();
    //SingleItemHolder singleItemHolder;


    private static OnsingleitemclickListner sClickListener;

    public Adapter(FragmentActivity activity, JSONData data, RecyclerView recyclerView) {
        /*Log.i("tag", "Adapter: "+data.size());*/
        this.mContext = activity;
        this.jsonData = data;
      //  this.eventPositionList=new ArrayList<Integer>(jsonData.getResponse().getItems().getLists().size()+1);
//        Log.i(TAG, "Adapter: array size"+this.eventPositionList.size());
        this.recyclerView=recyclerView;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        Log.i("tag", "onCreateViewHolder: ");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_recyclerview_item, parent, false);
        return new SingleItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final SingleItemHolder singleItemHolder = (SingleItemHolder) holder;
        Log.i(TAG, "onBindViewHolder: position"+position);
        singleItemHolder.itemView.setFocusable(focusedItem == position);
      //  recyclerView.scrollToPosition(focusedItem);
        singleItemHolder.itemView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        Log.i(TAG, "onKey: down "+focusedItem);
                        return tryMoveSelection(lm, 1);
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        Log.i(TAG, "onKey: up "+focusedItem);
                        return tryMoveSelection(lm, -1);
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        Log.i(TAG, "onKey: right");
                        changeEvents(position,+1, singleItemHolder);
                        //jsonData.getResponse().getItems().getLists().get(position).getEvents().get()
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        Log.i(TAG, "onKey: left");
                        changeEvents(position,-1, singleItemHolder);
                    }
                }
                return false;
            }
        });

        singleItemHolder.imgTitle.setText(jsonData.getResponse().getItems().getLists().get(position).getTitle());
        try {
            Glide.with(mContext).load(jsonData.getResponse().getItems().getLists().get(position).getImg().getSmall()).into(singleItemHolder.itemImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //singleItemHolder.Event.setText(jsonData.getResponse().getItems().getLists().get(position).getEvents().get(position).getName());

        //singleItemHolder.itemView.setSelected(focusedItem==position);
      //  if(eventPositionList.size()<position) { //set now time position instead of 0
        //    eventPositionList.add(position, 0);
        //}
      //  eventPositionList.get(position);
        int eventPosition = jsonData.getResponse().getItems().getLists().get(position).getCurrentEventPosition();
        singleItemHolder.event.setText(jsonData.getResponse().getItems().getLists().get(position).getEvents().get(eventPosition).getName());
    }

    private void changeEvents(int position, int direction, SingleItemHolder holder) {

        //int eventPosition= (int) eventPositionList.get(position)+direction;
        int eventPosition=jsonData.getResponse().getItems().getLists().get(position).getCurrentEventPosition()+direction;
        Log.i(TAG, "changeEvents: position-"+position+" eventPosition-"+eventPosition);
        List<ListsEvents> events = jsonData.getResponse().getItems().getLists().get(position).getEvents();
        if(eventPosition>=0 && eventPosition<events.size()) {
            holder.event.setText(events.get(eventPosition).getName());
            //eventPositionList.set(position,eventPosition);
            jsonData.getResponse().getItems().getLists().get(position).setCurrentEventPosition(eventPosition);
        }
    }

    @Override
    public int getItemCount() {
        return jsonData.getResponse().getItems().getLists().size();
    }

    interface OnsingleitemclickListner {
        void onItemClick(int position);
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView imgTitle;
        TextView event;
        LinearLayout linearLayout;
        ImageView chleft;
        ImageView chright;
        RelativeLayout single_item;

        public SingleItemHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            imgTitle = itemView.findViewById(R.id.img_title);
            linearLayout = itemView.findViewById(R.id.events);
            event = itemView.findViewById(R.id.event);
            chleft = itemImage.findViewById(R.id.chleft);
            chright = itemView.findViewById(R.id.chright);
            single_item = itemView.findViewById(R.id.single_item);
        }

    }


  @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
     /*   recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        Log.i(TAG, "onKey: down");
                        return tryMoveSelection(lm, 1);
                    }if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        Log.i(TAG, "onKey: up");
                        return tryMoveSelection(lm, -1);
                    }if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){

                    }if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){

                    }

                  *//*  else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
                        //Toast.makeText(context, "Item click on positon ="+focusedItem, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(context,VideoDetailsActivity.class);
                        intent.putParcelableArrayListExtra("videoList",catVideoList);
                        context.startActivity(intent);

                    }*//*
                }
                return false;
            }
        });*/
    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int i) {
        int tryFocusItem = focusedItem + i;

        Log.i("try", "tryMoveSelection: "+tryFocusItem);
        // If still within valid bounds, move the guide_frag_selector, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
            Log.i("down", "tryMoveSelection: "+focusedItem);
            lm.scrollToPosition(focusedItem);
            notifyItemChanged(focusedItem);
            return true;
        }

        return false;
    }
}