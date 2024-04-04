package com.example.afinal;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class BelongingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    class ViewType {
        public static final int BELONGING = 0;
        public static final int ADD = 1;
    }
    private ArrayList<BelongingItem> items;
    private OnDatabaseCallback callback;

    public class BelongingViewHolder extends RecyclerView.ViewHolder {
        private TextView belongingTextView;
        private ImageView updateImageView;
        private ImageView deleteImageView;
        public BelongingViewHolder(@NonNull View itemView) {
            super(itemView);

            belongingTextView = itemView.findViewById(R.id.belongingTextView2);
            updateImageView = itemView.findViewById(R.id.updateImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);

            updateImageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    final EditText editText = new EditText(v.getContext());
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("소지품 수정")
                          .setMessage("수정할 소지품의 이름을 적어주세요.")
                          .setView(editText)
                          .setPositiveButton("확인",
                            (dialog1, which) -> {
                                String name = editText.getText().toString();
                                callback.updateBelonging(items.get(position).getBelonging().getId(), name);
                                items.get(position).getBelonging().setName(name);
                                notifyItemChanged(position);
                            })
                          .setNegativeButton("취소",
                            (dialog2, which) -> {
                            });
                    dialog.show();
                }
            });
            deleteImageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("소지품 삭제")
                          .setMessage("소지품 " + items.get(position).getBelonging().getName() + "를 삭제하시겠습니까?")
                          .setPositiveButton("확인",
                            (dialog1, which) -> {
                                callback.deleteBelonging(items.get(position).getBelonging().getId());
                                items.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, items.size());
                            })
                          .setNegativeButton("취소",
                            (dialog2, which) -> {
                            });
                    dialog.show();
                }
            });
        }
        public TextView getBelongingTextView() {
            return belongingTextView;
        }

        public ImageView getUpdateImageView() {
            return updateImageView;
        }

        public ImageView getDeleteImageView() {
            return deleteImageView;
        }
    }

    public class AddViewHolder extends RecyclerView.ViewHolder {
        private ImageView addImageView;
        public AddViewHolder(@NonNull View itemView) {
            super(itemView);

            addImageView = itemView.findViewById(R.id.addImageView);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    final EditText editText = new EditText(v.getContext());
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("소지품 추가")
                          .setMessage("추가할 소지품의 이름을 적어주세요.")
                          .setView(editText)
                          .setPositiveButton("확인",
                            (dialog1, which) -> {
                                String name = editText.getText().toString();
                                int id = callback.insertBelongingRecord(name);
                                ArrayList<TimelineInfo> timeline = callback.selectAllTimeline();
                                for (int i = 0; i < timeline.size(); i++) {
                                    callback.insertPossessionRecord(id, timeline.get(i).getId(), false);
                                }
                                items.add(items.size()-1, new BelongingItem(ViewType.BELONGING, new BelongingInfo(id, name)));
                                notifyItemInserted(items.size()-1);
                            })
                          .setNegativeButton("취소",
                            (dialog2, which) -> {
                            });
                    dialog.show();
                }
            });
        }
        public ImageView getAddImageView() {
            return addImageView;
        }

    }

    public BelongingAdapter(OnDatabaseCallback callback) {
        this.callback = callback;
        this.items = new ArrayList<>();
    }
    public BelongingAdapter(OnDatabaseCallback callback, ArrayList<BelongingItem> items) {
        this.callback = callback;
        this.items = items;
    }
    //--------------------------------------------------

    public void addItem(BelongingItem item) {
        items.add(item);
    }

    public ArrayList<BelongingItem> getItems() {
        return items;
    }

    public void clearItems() {
        items.clear();
    }

    @NonNull
    @Override   // ViewHolder 객체를 생성하여 리턴한다.
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ViewType.ADD) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_belonging_item, parent, false);
            return new AddViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.belonging_item, parent, false);
            return new BelongingViewHolder(view);
        }
    }

    @Override   // ViewHolder안의 내용을 position에 해당되는 데이터로 교체한다.
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BelongingViewHolder) {
            String name = items.get(position).getBelonging().getName();
            ((BelongingViewHolder)holder).belongingTextView.setText(name);
        }
    }

    @Override   // 전체 데이터의 갯수를 리턴한다.
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }
}