package com.example.zingotv.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zingotv.Models.JSONData;
import com.example.zingotv.Models.ListsEvents;
import com.example.zingotv.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter extends RecyclerView.Adapter {

    private static final String TAG = "dpadl";
    private JSONData jsonData;
    private Context mContext;
    int focusedItem = 10;
    RecyclerView recyclerView;
    List eventPositionList=new ArrayList<Integer>();
    private long mLastKeyDownTime = 0;
    //SingleItemHolder singleItemHolder;


    private static OnsingleitemclickListner sClickListener;
    private int selectedChannelItem;

    public Adapter(FragmentActivity activity, JSONData data, RecyclerView recyclerView) {
        /*Log.i("tag", "Adapter: "+data.size());*/
        this.mContext = activity;
        this.jsonData = data;
      //  this.eventPositionList=new ArrayList<Integer>(jsonData.getResponse().getItems().getLists().size()+1);
//        Log.i(TAG, "Adapter: array size"+this.eventPositionList.size());
        this.recyclerView=recyclerView;
        focusedItem=10;
        //selectedChannelItem=1;
    }

    public int getFocusedItemPosition(){
        return focusedItem;
    }

    public void requestFocus(){

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

      if(focusedItem==position) {
          singleItemHolder.itemView.requestFocus();
      }
      singleItemHolder.itemView.setFocusable(focusedItem == position);


     //       Log.i(TAG, "focusedItem position "+focusedItem);

        /*if(selectedChannelItem==position) {
            Log.i(TAG, "focusedItem and position: "+focusedItem);
            singleItemHolder.channelSelection.setBackgroundColor(mContext.getResources().getColor(R.color.colorChannelSelection));
        }else{
            Log.i(TAG, "position background deselect "+position);
            singleItemHolder.channelSelection.setBackgroundColor(mContext.getResources().getColor(R.color.light_white));
        }*/
      //  recyclerView.scrollToPosition(focusedItem);

        singleItemHolder.itemView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    long current = System. currentTimeMillis();
                    boolean res = false;
                    Log.i("key", "onKey: time current :"+current+" time lastkey- "+mLastKeyDownTime);
                    if (current - mLastKeyDownTime < 300 ) {
                        res = true;
                    }
                    else {
                        mLastKeyDownTime = current;
                     //   res = super.onKeyDown(keyCode, event);
                        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            Log.i(TAG, "onKey: down "+focusedItem);
                            return tryMoveSelection(lm, 1, singleItemHolder);
                        }
                        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            Log.i(TAG, "onKey: up "+focusedItem);
                            return tryMoveSelection(lm, -1, singleItemHolder);
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
                    return res;
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
        LinearLayout linearLayout, channelSelection;
        ImageView chleft;
        ImageView chright;
        RelativeLayout single_item;

        public SingleItemHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            imgTitle = itemView.findViewById(R.id.img_title);
            linearLayout = itemView.findViewById(R.id.events);
            channelSelection = itemView.findViewById(R.id.channelselection);
            event = itemView.findViewById(R.id.event);
            chleft = itemImage.findViewById(R.id.chleft);
            chright = itemView.findViewById(R.id.chright);
            single_item = itemView.findViewById(R.id.single_item);
        }

    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        Log.i(TAG, "onViewAttachedToWindow: "+holder.getAdapterPosition());
        super.onViewAttachedToWindow(holder);
      //  recyclerView.smoothScrollToPosition(focusedItem+1);
        //holder.itemView.requestFocus();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        Log.i(TAG, "onViewDetachedFromWindow: "+holder.getAdapterPosition());
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
        //recyclerView.requestFocus();

        recyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i(TAG, "onFocusChange: hasFocus-"+hasFocus);
            }
        });

        recyclerView.scrollToPosition(focusedItem);


        if(recyclerView.findFocus()==null){
            Log.i(TAG, "onAttachedToRecyclerView: focus find is null");
        }
        if(recyclerView.isFocused()){
            Log.i(TAG, "onAttachedToRecyclerView: focused recyclerview root layout");
        }
        //recyclerView.requestFocus();
        //recyclerView.smoothScrollToPosition(focusedItem);
        //recyclerView.requestFocus();
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

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int i, SingleItemHolder singleItemHolder) {
        int tryFocusItem = focusedItem + i;
        int scrollPosition = focusedItem + i*2;
        if(scrollPosition<0){
            scrollPosition=0;
        }
        Log.i("try", "tryMoveSelection: "+tryFocusItem);
        // If still within valid bounds, move the guide_frag_selector, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
            Log.i("down", "tryMoveSelection: "+focusedItem);
            recyclerView.smoothScrollToPosition(scrollPosition);
            //recyclerView.findViewHolderForAdapterPosition(focusedItem).itemView.requestFocus();
            //recyclerView.findViewHolderForAdapterPosition(focusedItem).itemView.requestFocus();
            notifyItemChanged(focusedItem);
            return true;
        }
        return false;
    }
}