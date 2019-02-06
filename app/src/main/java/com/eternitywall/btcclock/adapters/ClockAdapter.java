package com.eternitywall.btcclock.adapters;

import android.content.Context;
import com.eternitywall.btcclock.Clock;
import java.util.List;

public class ClockAdapter extends RadioAdapter<Clock> {
    public ClockAdapter(final Context context, final List<Clock> items) {
        super(context, items);
    }

    @Override
    public void onBindViewHolder(final RadioAdapter.ViewHolder viewHolder, final int i) {
        super.onBindViewHolder(viewHolder, i);
        viewHolder.mText.setText(mItems.get(i).name);
    }
}