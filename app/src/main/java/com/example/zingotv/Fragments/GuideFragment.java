package com.example.zingotv.Fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Query;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zingotv.Adapters.Adapter;
import com.example.zingotv.Adapters.BottomAdapter;
import com.example.zingotv.Adapters.TopAdapter;
import com.example.zingotv.DataBase.authItemDatabase;
import com.example.zingotv.Interfaces.JsonPlaceHolderApi;
import com.example.zingotv.Interfaces.ListEventsDAO;
import com.example.zingotv.Interfaces.ListsDAO;
import com.example.zingotv.MainActivity;
import com.example.zingotv.Models.JSONData;
import com.example.zingotv.Models.Lists;
import com.example.zingotv.R;
import com.example.zingotv.ViewModel.authItemViewmodel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GuideFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GuideFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuideFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final String TAG = "cat";
    private com.example.zingotv.ViewModel.authItemViewmodel authItemViewmodel;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private JSONData data;
    private List<Lists> lists;
    Adapter adapter;
    TopAdapter topadapter;
    BottomAdapter bottomAdapter;
    private com.example.zingotv.Interfaces.authItemDAO authItemDAO;
    private ListsDAO listsDAO;
    private ListEventsDAO listEventsDAO;
    authItemDatabase database;
    RecyclerView recyclerView;
    RecyclerView recyclerView_top;
    RecyclerView recyclerView_bottom;
    TextView livetv;
    TextView videos;
    TextView library;
    TextView all;
    TextView premium;
    TextView international;
    TextView crowd_tv;
    TextView sports;
    TextView favourites;
    MainActivity context;
    View fragmentView;
    public static final int BANNER = 0;
    public static final int RECVIEW1 = 1;
    public static final int RECVIEW2 = 2;
    private static int currFocus = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GuideFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GuideFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuideFragment newInstance(String param1, String param2) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guide, container, false);
        fragmentView=v;
        context = (MainActivity) getActivity();

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView_top=v.findViewById(R.id.selection_top);
        recyclerView_bottom=v.findViewById(R.id.selection_bottom);
        Log.i("tag1", "onCreateView: ");
        authItemViewmodel = ViewModelProviders.of(this).get(com.example.zingotv.ViewModel.authItemViewmodel.class);

        database = Room.databaseBuilder(getActivity(), authItemDatabase.class, "post_database").allowMainThreadQueries().build();
        authItemDAO = database.authItemDAO();
        data = authItemDAO.getDataDb();

        database = Room.databaseBuilder(getActivity(), authItemDatabase.class, "post_database").allowMainThreadQueries().build();
        listsDAO = database.listsDAO();
        Log.i("Lists", "onCreateView: " + listsDAO.LISTS().get(0).getTitle());
        database = Room.databaseBuilder(getActivity(), authItemDatabase.class, "post_database").allowMainThreadQueries().build();
        listEventsDAO = database.listEventsDAO();
        Log.i("events", "onCreateView: "+listEventsDAO.getEvents().get(0).getPchNo());

        v.setFocusable(true);
        v.requestFocus();



        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.i(TAG, "onKey: dpad");
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return true;
                    }
                    if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
                        recyclerView_top.setHasFixedSize(true);
                        topadapter = new TopAdapter(getActivity(), data.getResponse().getMenuItems(),recyclerView_top);
                        recyclerView_top.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
                        recyclerView_top.setAdapter(topadapter);
                        recyclerView_top.setVisibility(View.VISIBLE);

                        recyclerView_bottom.setHasFixedSize(true);
                        bottomAdapter=new BottomAdapter(getActivity(),data.getResponse().getFilterDetails(),recyclerView_bottom);
                        recyclerView_bottom.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
                        recyclerView_bottom.setAdapter(bottomAdapter);
                        recyclerView_bottom.setVisibility(View.VISIBLE);

                        recyclerView.setHasFixedSize(true);
                        adapter = new Adapter(getActivity(), data, recyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(adapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        fragmentView.setFocusable(false);
                        //recyclerView.scrollToPosition(adapter.getFocusedItemPosition());
                        //recyclerView.get
                        //recyclerView.findViewHolderForAdapterPosition().itemView.requestFocus();
                        //recyclerView.requestFocus();
                        //recyclerView.has;
                        return true;
                    }
                }
                return false;
            }
        });






       /*
       videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportSQLiteDatabase sqLiteDatabase = database.getOpenHelper().getWritableDatabase();
                sqLiteDatabase.delete("items_auth_table", null, null);
                sqLiteDatabase.delete("Lists_table", null, null);

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(httpLoggingInterceptor).build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://zingovod.zingotv.com/")
                            .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build();
                    jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
                    final Call<JSONData> call = jsonPlaceHolderApi.getVideoData("/android//zingotv//xml//categories_xml_android.php?argstring=category:00000000139!accountid:40000016!regToken:1555501710ZNOPJ!ver_name:setupbox3!ver_code:5!in_detailscreen:1");
                    call.enqueue(new Callback<JSONData>() {
                        @Override
                        public void onResponse(Call<JSONData> call, Response<JSONData> response) {
                            if (response.isSuccessful()) {
                                JSONData data = response.body();
                                  Log.i(TAG, "onResponse: " + data.getResponse().isShowCategoryHeader());
                                authItemViewmodel.insert(data);
                                List<Lists> lists = data.getResponse().getItems().getLists();
                                Log.i(TAG, "onResponse: " + lists.size());
                                authItemViewmodel.insertLists(lists);
                                CategoryFragment categoryFragment=new CategoryFragment();
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.mani_activity, categoryFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();

                            } else {
                                switch (response.code()) {
                                    case 404:
                                        Log.i(TAG, "onResponse: not found");

                                        break;
                                    case 500:
                                        Log.i(TAG, "onResponse: not logged in or server broken");
                                        break;
                                    default:
                                        Log.i(TAG, "onResponse: unknown error");
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONData> call, Throwable t) {
                            Log.i(TAG, "onFailure: " + t.getMessage());
                        }
                    });


            }
        });*/

        return v;
    }


    public boolean onBackPressed(){
        Log.i(TAG, "onBackPressed: guideFragment");
        if(recyclerView.hasFocus()){
        //if(recyclerView.getFocusedChild().isFocused()){
            Log.i(TAG, "onBackPressed: items recyclerview focused");
            recyclerView_bottom.requestFocus();
            return true;
        }
        else if(recyclerView_bottom.hasFocus()){
        //else if(recyclerView_bottom.getFocusedChild().isFocused()){
            Log.i(TAG, "onBackPressed: bottom recyclerview focused");
            recyclerView_top.requestFocus();
            return true;
        }
        else if(recyclerView_top.hasFocus()){
        //else if (recyclerView_top.getFocusedChild().isFocused()){
            Log.i(TAG, "onBackPressed: top recyclerview focused");

            recyclerView.setVisibility(View.INVISIBLE);
            recyclerView_top.setVisibility(View.INVISIBLE);
            recyclerView_bottom.setVisibility(View.INVISIBLE);
            fragmentView.setFocusable(true);
            fragmentView.requestFocus();
            return true;
        }
        Log.i(TAG, "onBackPressed: nothing focused");
        return true;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




}
