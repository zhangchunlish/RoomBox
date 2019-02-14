package de.andreasschrade.androidtemplate.ui.quote;
import java.util.ArrayList;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.dummy.DummyContent;
import de.andreasschrade.androidtemplate.dummy.ResultCode;
import de.andreasschrade.androidtemplate.model.Room;
import de.andreasschrade.androidtemplate.util.*;
import de.andreasschrade.androidtemplate.GlobalParams;
/**
 * Shows a list of all available quotes.
 * <p/>
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ArticleListFragment extends ListFragment {
    private MyListAdapter adapter;
    private Callback callback = dummyCallback;

    /**
     * A callback interface. Called whenever a item has been selected.
     */
    public interface Callback {
        void onItemSelected(String id);
    }

    /**
     * A dummy no-op implementation of the Callback interface. Only used when no active Activity is present.
     */
    private static final Callback dummyCallback = new Callback() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setHasOptionsMenu(true);
    }
    private void initData() {
        List<Room> mList_room=new ArrayList<Room>();
//        mList_room=BmobDataUtil.getRoomList(GlobalParams.USERNAME);
//        this.adapter=new MyListAdapter(GlobalParams.activity,mList_room);
//        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        // notify callback about the selected list item
        final Room item = (Room) this.adapter.getItem(position);
        callback.onItemSelected(item.getId());
    }

    /**
     * onAttach(Context) is not called on pre API 23 versions of Android.
     * onAttach(Activity) is deprecated but still necessary on older devices.
     */
    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /**
     * Deprecated on API 23 but still necessary for pre API 23 devices.
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /**
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        if (!(context instanceof Callback)) {
            throw new IllegalStateException("Activity must implement callback interface.");
        }

        callback = (Callback) context;
    }

    private class MyListAdapter extends BaseAdapter {
        private Context mContext;
        private List<Room> mList;

        public MyListAdapter(Context context, List<Room> list) {
            this.mContext = context;
            this.mList = list;
        }
        @Override
        public int getCount() {
            if (mList == null) {
                return 0;
            } else {
                return mList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_article, container, false);
            }

            final Room item = (Room) getItem(position);
            ((TextView) convertView.findViewById(R.id.article_title)).setText(item.getRoomid());
            ((TextView) convertView.findViewById(R.id.article_subtitle)).setText(item.getComment());
            ((TextView) convertView.findViewById(R.id.article_date)).setText(item.getUpdatetime());
            ((TextView) convertView.findViewById(R.id.article_delete)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ResultCode res=BmobDataUtil.deleteRoom(item);
                    if(res.getStatus()==0){
                        AppToast.getToast().show("删除成功");
                        mList.remove(item);
                        notifyDataSetChanged();
                    }else{
                        AppToast.getToast().show("删除失败");
                    }
                }
            });
            final ImageView img = (ImageView) convertView.findViewById(R.id.thumbnail);
            Glide.with(getActivity()).load(R.drawable.p1).asBitmap().fitCenter().into(new BitmapImageViewTarget(img) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    img.setImageDrawable(circularBitmapDrawable);
                }
            });

            return convertView;
        }
    }

    public ArticleListFragment() {
    }
}
