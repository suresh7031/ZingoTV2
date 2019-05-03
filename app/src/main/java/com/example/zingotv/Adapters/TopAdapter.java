package com.example.zingotv.Adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zingotv.Models.MenuItems;
import com.example.zingotv.R;

import java.util.List;

public class TopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "dpad";

    private List<MenuItems> menuItems;
    private Context mContext;
    int focusedItem = 0;
    RecyclerView mRecyclerview;

    public TopAdapter(FragmentActivity activity, List<MenuItems> menuItems, RecyclerView recyclerView_top) {
        this.mContext = activity;
        this.menuItems = menuItems;
        this.mRecyclerview = recyclerView_top;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("tag", "onCreateViewHolder: ");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_selection_up, parent, false);
        return new TopAdapter.SingleItemTop(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.i(TAG, "onAttachedToRecyclerView: ");
        super.onAttachedToRecyclerView(recyclerView);
recyclerView.requestFocus();
        recyclerView.scrollToPosition(focusedItem);
        recyclerView.smoothScrollToPosition(focusedItem);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final SingleItemTop singleItemTop = (SingleItemTop) holder;
        singleItemTop.toptext.setText(menuItems.get(position).getName());
        Log.i("top", "onBindViewHolder: " + menuItems.get(position).getName());

        singleItemTop.itemView.setFocusable(focusedItem == position);


        singleItemTop.itemView.setOnKeyListener(new View.OnKeyListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = mRecyclerview.getLayoutManager();
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        Log.i(TAG, "onKey: down");
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        Log.i(TAG, "onKey: up");

                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        Log.i(TAG, "onKey: right");
                        return tryMoveSelection(lm, 1);
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        Log.i(TAG, "onKey: left");
                        return tryMoveSelection(lm, -1);
                    }
                }


                return false;
            }
        });


    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int i) {

        int tryFocusItem = focusedItem + i;

        Log.i(TAG, "tryMoveSelection: " + tryFocusItem);
        // If still within valid bounds, move the guide_frag_selector, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focusedItem);
            focusedItem = tryFocusItem;
          //  notifyDataSetChanged();
             Log.i(TAG, "tryMoveSelection: " + focusedItem);
            notifyItemChanged(focusedItem);
            lm.scrollToPosition(focusedItem);
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    private class SingleItemTop extends RecyclerView.ViewHolder {
        TextView toptext;
        View itemView;

        public SingleItemTop(View itemView) {
            super(itemView);
            toptext = itemView.findViewById(R.id.uptext);
            this.itemView=itemView;
        }
    }

}
