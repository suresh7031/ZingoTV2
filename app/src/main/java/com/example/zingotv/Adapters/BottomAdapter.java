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

import com.example.zingotv.Models.FilterDetails;
import com.example.zingotv.R;

import java.sql.Statement;
import java.util.List;

public class BottomAdapter extends RecyclerView.Adapter {
    private static final String TAG = "bottom";
    List<FilterDetails> filterDetails;
    Context mcontext;
    RecyclerView mRecyclerViewbottom;
    RecyclerView subRecyclerView;
    int focused = 0;

    public BottomAdapter(FragmentActivity activity, List<FilterDetails> filterDetails, RecyclerView recyclerView_bottom) {
        this.filterDetails = filterDetails;
        this.mcontext = activity;
        this.mRecyclerViewbottom = recyclerView_bottom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_selection_down, parent, false);
        return new BottomAdapter.SingleItemBottom(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.i(TAG, "onAttachedToRecyclerView: ");
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(focused);
        recyclerView.smoothScrollToPosition(focused);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        SingleItemBottom singleItemBottom = (SingleItemBottom) holder;
        singleItemBottom.bottomtext.setText(filterDetails.get(position).getName());

        Log.i(TAG, "onBindViewHolder:");
        singleItemBottom.itemView.setFocusable(focused == position);

        singleItemBottom.itemView.setOnKeyListener(new View.OnKeyListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                RecyclerView.LayoutManager lm = mRecyclerViewbottom.getLayoutManager();
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        Log.i(TAG, "onKey: right"+focused);
                  //      mRecyclerViewbottom.requestFocus();
                        //focused+=1;
                       // lm.scrollToPosition(focused);
                        //notifyItemChanged(focused);
                      // notifyDataSetChanged();
                     //   return true;
                        return tryMoveSelection(lm, 1);

                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        Log.i(TAG, "onKey: left "+focused);
                   //     mRecyclerViewbottom.requestFocus();
                     //   focused-=1;
                        //lm.scrollToPosition(focused);
                        //notifyItemChanged(focused);
                      //  notifyDataSetChanged();
                        //return true;
                        return tryMoveSelection(lm, -1);

                    }

                  /*  else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
                    //Toast.makeText(context, "Item click on positon ="+focusedItem, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context,VideoDetailsActivity.class);
                    intent.putParcelableArrayListExtra("videoList",catVideoList);
                    context.startActivity(intent);

                }*/
                }
                return false;
            }
        });

    }

    private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int i) {
        int tryFocusItem = focused + i;

        Log.i(TAG, "tryMoveSelection: " + tryFocusItem);
        // If still within valid bounds, move the guide_frag_selector, notify to redraw, and scroll
        if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
            notifyItemChanged(focused);
            focused = tryFocusItem;
            Log.i(TAG, "tryMoveSelection: " + focused);
            lm.scrollToPosition(focused);
            notifyItemChanged(focused);
            return true;
        }

        return false;
    }

    @Override
    public int getItemCount() {
        return filterDetails.size();
    }

    private class SingleItemBottom extends RecyclerView.ViewHolder {
        TextView bottomtext;


        public SingleItemBottom(View itemView) {
            super(itemView);
            bottomtext = itemView.findViewById(R.id.downtext);

        }
    }


}
