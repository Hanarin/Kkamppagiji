package com.example.afinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimelineAdapter extends BaseExpandableListAdapter {
    OnDatabaseCallback callback;
    ArrayList<Date> dateItems; //부모 리스트를 담을 배열
    ArrayList<ArrayList<TimelineItem>> timelineItems; //자식 리스트를 담을 배열

    public TimelineAdapter(OnDatabaseCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getGroupCount() {
        return dateItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return timelineItems.get(groupPosition).size();
    }

    //리스트의 아이템 반환
    @Override
    public Date getGroup(int groupPosition) {
        return dateItems.get(groupPosition);
    }

    @Override
    public TimelineItem getChild(int groupPosition, int childPosition) {
        return timelineItems.get(groupPosition).get(childPosition);
    }

    //리스트 아이템의 id 반환
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //동일한 id가 항상 동일한 개체를 참조하는지 여부를 반환
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //리스트 각각의 row에 view를 설정
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        Context context = parent.getContext();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //convertView가 비어있을 경우 xml파일을 inflate 해줌
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.date_item, parent, false);
        }

        //View들은 반드시 아이템 레이아웃을 inflate한 뒤에 작성할 것
        ImageView arrowIcon = (ImageView) v.findViewById(R.id.arrowImageView);
        TextView date = (TextView) v.findViewById(R.id.dateTextView);

        //그룹 펼쳐짐 여부에 따라 아이콘 변경
        if (isExpanded)
            arrowIcon.setImageResource(R.drawable.arrow_down);
        else
            arrowIcon.setImageResource(R.drawable.arrow_right);

        //리스트 아이템의 내용 설정
        date.setText(format.format(getGroup(groupPosition)));

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        Context context = parent.getContext();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm a");

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.timeline_item, parent, false);
        }

        ImageView timeImage = (ImageView) v.findViewById(R.id.timeImageView);
        TextView timeText = (TextView) v.findViewById(R.id.timeTextView);
        ImageView placeImage = (ImageView) v.findViewById(R.id.placeImageView);
        TextView placeText = (TextView) v.findViewById(R.id.placeTextView);
        RecyclerView possessionList = (RecyclerView) v.findViewById(R.id.possessionRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        PossessionAdapter adapter = new PossessionAdapter(callback, getChild(groupPosition, childPosition).getPossessionItems());

        timeImage.setImageResource(R.drawable.time);
        timeText.setText(format.format(getChild(groupPosition, childPosition).getTimeline().getDatetime()));
        placeImage.setImageResource(R.drawable.marker);
//        placeText.setText(getChild(groupPosition, childPosition).getAddress());
        possessionList.setLayoutManager(linearLayoutManager);
        possessionList.setAdapter(adapter);

        v.setOnClickListener(v1 -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(v1.getContext());
            int timelineId = timelineItems.get(groupPosition).get(childPosition).getTimeline().getId();
            ArrayList<PossessionInfo> possessionInfo = callback.selectPossessionFromTimeline(timelineId);
            String belongingName[] = new String[possessionInfo.size()];
            String test[] = {"abc", "bbb", "ccc"};
            boolean possession[] = new boolean[possessionInfo.size()];

            for (int i = 0; i < possessionInfo.size(); i++) {
                belongingName[i] = callback.selectBelonging(possessionInfo.get(i).getBelongingId()).getName();
                possession[i] = possessionInfo.get(i).getPossession();
            }
            dialog.setTitle("소지품 확인")
//                    .setMessage("소지품을 소지중인지 확인 후 체크해주세요.")
//                    .setView(editText)
                    .setMultiChoiceItems(belongingName, possession, (dialog1, which, isChecked) -> {
                        possessionInfo.get(which).setPossession(isChecked);
                    })
                    .setPositiveButton("확인",
                            (dialog1, which) -> {
                                ArrayList<PossessionItem> possessionItems = new ArrayList<>();
                                for (int i = 0; i < possessionInfo.size(); i++) {
                                    callback.updatePossession(possessionInfo.get(i).getBelongingId(), possessionInfo.get(i).getTimelineId(), possessionInfo.get(i).getPossession());
                                    possessionItems.add(new PossessionItem(possessionInfo.get(i)));
                                }
                                getChild(groupPosition, childPosition).setPossessionItems(possessionItems);
                                adapter.setItems(getChild(groupPosition, childPosition).getPossessionItems());
                                adapter.notifyDataSetChanged();
                            })
                    .setNegativeButton("취소",
                            (dialog2, which) -> {
                            });
            dialog.show();
        });

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //리스트에 새로운 아이템을 추가
    public void addItem(int groupPosition, TimelineItem item) {
        timelineItems.get(groupPosition).add(item);
    }

    //리스트 아이템을 삭제
    public void removeChild(int groupPosition, int childPosition) {
        timelineItems.get(groupPosition).remove(childPosition);
    }
}
