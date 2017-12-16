package badeeb.com.daringo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import badeeb.com.daringo.R;
import badeeb.com.daringo.fragments.EditBlocksFragment;
import badeeb.com.daringo.models.Block;

/**
 * Created by meldeeb on 12/9/17.
 */

public class EditBlocksRecyclerAdapter extends BaseRecyclerAdapter<Block> {

    private EditBlocksFragment fragment;

    public EditBlocksRecyclerAdapter(Context context, EditBlocksFragment fragment) {
        super(context);
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_edit_block, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Block block = getItemAt(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.etBlock.addTextChangedListener(new MyTextWatcher(position));
        viewHolder.etBlock.setHint("Bet #" + block.getOrder());
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        EditText etBlock;

        public ViewHolder(View itemView) {
            super(itemView);
            etBlock = itemView.findViewById(R.id.etBlock);
        }
    }

    public boolean allBlocksCompleted() {
        for (Block b : getItems()) {
            if (TextUtils.isEmpty(b.getText())) {
                return false;
            }
        }
        return true;
    }

    private class MyTextWatcher implements TextWatcher {
        private int position;

        public MyTextWatcher(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            getItemAt(position).setText(editable.toString());
            fragment.checkFieldsCompleted();
        }
    }
}