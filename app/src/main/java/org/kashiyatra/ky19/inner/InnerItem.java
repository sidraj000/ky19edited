package org.kashiyatra.ky19.inner;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import org.greenrobot.eventbus.EventBus;
import org.kashiyatra.ky19.EventListActivity;
import org.kashiyatra.ky19.EventsActivity;
import org.kashiyatra.ky19.R;
import org.kashiyatra.ky19.utils.GlideApp;


public class InnerItem  extends com.ramotion.garlandview.inner.InnerItem {

    private final View mInnerLayout;

    public final TextView mHeader;
    public final TextView mName;
    public final TextView mAddress;
    public final ImageView mAvatar;
    public final View mAvatarBorder;
    public final FrameLayout mLayout;
    public int position;

    private InnerData mInnerData;

    public InnerItem(View itemView) {
        super(itemView);
        mInnerLayout = ((ViewGroup)itemView).getChildAt(0);

        mHeader = (TextView) itemView.findViewById(R.id.tv_header);
        mName = (TextView) itemView.findViewById(R.id.tv_name);
        mAddress = (TextView) itemView.findViewById(R.id.tv_address);
        mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
        mAvatarBorder = itemView.findViewById(R.id.avatar_border);
        mLayout=itemView.findViewById(R.id.background_layout);
        mInnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(InnerItem.this);
            }
        });
    }

    @Override
    protected View getInnerLayout() {
        return mInnerLayout;
    }

    public InnerData getItemData() {
        return mInnerData;
    }

    public void clearContent() {
        GlideApp.with(mAvatar.getContext()).clear(mAvatar);
        mInnerData = null;
    }

    void setContent(InnerData data) {
        mInnerData = data;
        mHeader.setText(data.title);
        mName.setText(data.name);
   /*    GlideApp.with(itemView.getContext())
                .load(data.avatarUrl)
                .placeholder(R.drawable.avatar_placeholder)
                .transform(new CircleCrop())
                .into(mAvatar);*/
        mAvatar.setBackground(data.avatarUrl);


    }

}
