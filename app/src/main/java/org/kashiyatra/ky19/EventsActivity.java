package org.kashiyatra.ky19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.ramotion.garlandview.TailLayoutManager;
import com.ramotion.garlandview.TailRecyclerView;
import com.ramotion.garlandview.TailSnapHelper;
import com.ramotion.garlandview.header.HeaderTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.kashiyatra.ky19.inner.InnerData;
import org.kashiyatra.ky19.inner.InnerItem;
import org.kashiyatra.ky19.outer.OuterAdapter;
import org.kashiyatra.ky19.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {
    String[] subeventName;
    String[] eventName;
    String[] eventDetails;
    String[] subEventDetails;
    String[] subEventDescriptions;
   TypedArray eventBanner;
    TypedArray backgrounds;

    TypedArray icons;

    private final static int OUTER_COUNT = 8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        final List<List<InnerData>> outerData = new ArrayList<>();
         icons = getResources().obtainTypedArray(R.array.event_icons);

        eventName= getResources().getStringArray(R.array.event_names);
        eventDetails=getResources().getStringArray(R.array.event_descriptions);
        backgrounds = getResources().obtainTypedArray(R.array.event_backgrounds);

        for (int i = 0; i < OUTER_COUNT ; i++) {
            final List<InnerData> innerData = new ArrayList<>();
            innerData.add(new InnerData(
                    eventName[i],
                    eventDetails[i],
                    "varanasi " + "UP",
                    getDrawable(icons.getResourceId(i, -1)),
                    getResources().getColor(R.color.no1)


            ));
            switch (i) {
                case 1:
                    subeventName = getResources().getStringArray(R.array.bandish_subevent_names);
                    eventBanner = getResources().obtainTypedArray(R.array.bandish_subevent_images);
                    subEventDescriptions= getResources().getStringArray(R.array.bandish_subevent_short_description);
                    break;
                case 3:
                    subeventName = getResources().getStringArray(R.array.enquizta_subevent_names);
                    eventBanner = getResources().obtainTypedArray(R.array.enquizta_subevent_images);
                    subEventDescriptions= getResources().getStringArray(R.array.enquizta_subevent_short_description);
                    break;
                case 4:
                    subeventName = getResources().getStringArray(R.array.mirage_subevent_names);
                    eventBanner = getResources().obtainTypedArray(R.array.mirage_subevent_images);

                    subEventDescriptions= getResources().getStringArray(R.array.mirage_subevent_short_description);
                    break;
                case 6:
                    subeventName = getResources().getStringArray(R.array.samwaad_subevent_names);
                    eventBanner = getResources().obtainTypedArray(R.array.samwaad_subevent_images);
                    subEventDescriptions= getResources().getStringArray(R.array.samwaad_subevent_short_description);
                    break;
                case 0:
                    subeventName = getResources().getStringArray(R.array.abhinay_subevent_names);
                    eventBanner = getResources().obtainTypedArray(R.array.abhinay_subevent_images);
                    subEventDescriptions= getResources().getStringArray(R.array.abhinay_subevent_short_description);
                    break;
                case 7:
                    subeventName = getResources().getStringArray(R.array.toolika_subevent_names);
                    eventBanner = getResources().obtainTypedArray(R.array.toolika_subevent_images);
                    subEventDescriptions= getResources().getStringArray(R.array.toolika_subevent_short_description);
                    break;
                case 5:
                    subeventName = getResources().getStringArray(R.array.natraj_subevent_names);
                    eventBanner = getResources().obtainTypedArray(R.array.natraj_subevent_images);
                    subEventDescriptions= getResources().getStringArray(R.array.natraj_subevent_short_description);
                    break;
                case 2:
                    subeventName = getResources().getStringArray(R.array.crosswindz_subevent_names);
                    eventBanner = getResources().obtainTypedArray(R.array.crosswindz_subevent_images);
                    subEventDescriptions= getResources().getStringArray(R.array.crosswindz_subevent_short_description);
                    break;
            }
            for (int j = 0; j < subeventName.length ; j++) {
                innerData.add(new InnerData(
                        subeventName[j],
                        subEventDescriptions[j],
                        "varanasi " + "UP",
                        getDrawable(eventBanner.getResourceId(j, -1)),
                        j
                ));
            }
            outerData.add(innerData);
        }
        initRecyclerView(outerData);
    }
    private void initRecyclerView(List<List<InnerData>> data) {
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        final TailRecyclerView rv = (TailRecyclerView) findViewById(R.id.recycler_view);
        ((TailLayoutManager)rv.getLayoutManager()).setPageTransformer(new HeaderTransformer());
        rv.setAdapter(new OuterAdapter(data));
        rv.addOnItemTouchListener(new RecyclerItemClickListener(EventsActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(EventsActivity.this, EventListActivity.class);
                        intent.putExtra("POSITION", position);
                        startActivity(intent);

                    }
                })
        );
        new TailSnapHelper().attachToRecyclerView(rv);
    }



}