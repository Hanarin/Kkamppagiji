package com.example.afinal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BelongingFragment extends Fragment {
    private static final String TAG = "BelongingFragment";
    OnDatabaseCallback callback;
    ArrayList<BelongingInfo> belongingList;
    RecyclerView belongingListView;
    BelongingAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnDatabaseCallback) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.belonging_fragment, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), 1);
        decoration.setDrawable(getResources().getDrawable(R.drawable.line_divider, null));

        belongingListView = rootView.findViewById(R.id.belongingRecyclerView);
        adapter = new BelongingAdapter(callback);
        belongingListView.setLayoutManager(linearLayoutManager);
        belongingListView.setAdapter(adapter);
        belongingListView.addItemDecoration(decoration);
        updateListData();

        return rootView;
    }

    public void updateListData() {
        belongingList = callback.selectAllBelonging();
        adapter.clearItems();
        for (int i = 0; i < belongingList.size(); i++) {
            adapter.addItem(new BelongingItem(BelongingAdapter.ViewType.BELONGING, belongingList.get(i)));
        }
        adapter.addItem(new BelongingItem(BelongingAdapter.ViewType.ADD));
        adapter.notifyDataSetChanged();
    }
}
