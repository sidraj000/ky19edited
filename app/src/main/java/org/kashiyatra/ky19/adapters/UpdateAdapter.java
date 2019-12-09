package org.kashiyatra.ky19.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.kashiyatra.ky19.R;

/**
 * Created by HemanthSai on 07-Jan-18.
 */

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.ViewHolder> {

    String[] mBodies, mImageUrls, mLinks;
    long[] mTimes;
    Context mContext;

    public UpdateAdapter(Context context, String[] bodies, String[] imageUrls, String[] links, long[] times) {
        mBodies = bodies;
        mImageUrls = imageUrls;
        mLinks = links;
        mContext = context;
        mTimes = times;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_view, parent, false);
        return new UpdateAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UpdateAdapter.ViewHolder holder, final int position) {
        holder.mBodyTextView.setText(mBodies[position]);
        holder.mTimeTextView.setText(DateUtils.getRelativeDateTimeString(mContext, mTimes[position],
                DateUtils.SECOND_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0));

        Glide.with(mContext)
                .load(mImageUrls[position])
                .apply(new RequestOptions()
                        .centerCrop()
                        .dontAnimate()
                        .dontTransform())
                .into(holder.mImageView);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(mLinks[position]);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBodies.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView mBodyTextView, mTimeTextView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mBodyTextView = v.findViewById(R.id.update_text);
            mImageView = v.findViewById(R.id.update_image);
            mTimeTextView = v.findViewById(R.id.update_time);
        }
    }
}
