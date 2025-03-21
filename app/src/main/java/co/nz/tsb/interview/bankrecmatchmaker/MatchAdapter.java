package co.nz.tsb.interview.bankrecmatchmaker;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;

public class MatchAdapter extends ListAdapter<MatchItem, MatchAdapter.ViewHolder> {

    private final OnItemClickListener onItemClickListener;
    private MatchItem checkedItem;

    public MatchAdapter(
            @NonNull DiffUtil.ItemCallback<MatchItem> diffCallback,
            OnItemClickListener onItemClickListener
    ) {
        super(diffCallback);
        this.onItemClickListener = onItemClickListener;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CheckedListItem listItem = (CheckedListItem) layoutInflater.inflate(R.layout.list_item_match, parent, false);
        return new ViewHolder(listItem, checkedItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MatchItem matchItem = getItem(position);
        holder.bind(matchItem, onItemClickListener);
    }

    public void setCheckedItem(@Nullable MatchItem checkedItem) {
        this.checkedItem = checkedItem;
    }

    public interface OnItemClickListener {
        void onItemClick(MatchItem item, Boolean isChecked);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mainText;
        private final TextView total;
        private final TextView subtextLeft;
        private final TextView subtextRight;
        private final CheckedListItem checkedListItem;
        private final MatchItem itemToCheck;

        public ViewHolder(CheckedListItem itemView, MatchItem itemChecked) {
            super(itemView);
            itemToCheck = itemChecked;
            checkedListItem = itemView;
            mainText = itemView.findViewById(R.id.text_main);
            total = itemView.findViewById(R.id.text_total);
            subtextLeft = itemView.findViewById(R.id.text_sub_left);
            subtextRight = itemView.findViewById(R.id.text_sub_right);
        }


        @SuppressLint("DefaultLocale")
        public void bind(MatchItem matchItem, OnItemClickListener onItemClickListener) {
            mainText.setText(matchItem.getPaidTo());
            total.setText(String.format("%.2f", matchItem.getTotal()));
            subtextLeft.setText(matchItem.getTransactionDate());
            subtextRight.setText(matchItem.getDocType());
            if (itemToCheck != null && matchItem.getTotal() == itemToCheck.getTotal()) {
                checkedListItem.setChecked(true);
                onItemClickListener.onItemClick(itemToCheck, true);
            }
            checkedListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedListItem.setChecked(!checkedListItem.isChecked());
                    onItemClickListener.onItemClick(matchItem, checkedListItem.isChecked());
                }
            });
        }

    }

    public static class DiffCallback extends DiffUtil.ItemCallback<MatchItem> {
        @Override
        public boolean areItemsTheSame(@NonNull MatchItem oldItem, @NonNull MatchItem newItem) {
            return oldItem.getPaidTo().equals(newItem.getPaidTo()) &&
                    oldItem.getDocType().equals(newItem.getDocType()) &&
                    oldItem.getTransactionDate().equals(newItem.getTransactionDate()) &&
                    BigDecimal.valueOf(oldItem.getTotal()).compareTo(BigDecimal.valueOf(newItem.getTotal())) == 0;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MatchItem oldItem, @NonNull MatchItem newItem) {
            return oldItem.equals(newItem);
        }
    }
}