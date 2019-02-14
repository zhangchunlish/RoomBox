package de.andreasschrade.androidtemplate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import android.graphics.Bitmap;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.dummy.UserRoleEnum;
import de.andreasschrade.androidtemplate.model.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import de.andreasschrade.androidtemplate.GlobalParams;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
/*
 * 房间列表数据适配器
 * @author alisa
 */
public class RoomListAdapter extends BaseAdapter   {

    private List<Room> listItems;//数据集合
    private LayoutInflater layoutinflater;//视图容器，用来导入布局

    static class ViewHolder
    {
        private TextView roomid;
        private TextView comment;
        private TextView date;
        private ImageView image;
    }

    /*
     * 实例化Adapter
     */
    public RoomListAdapter(Context context, List<Room> dataSet)
    {
        this.listItems = dataSet;
        this.layoutinflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room device = listItems.get(position);
        final ViewHolder holder;
        View view;
        if(convertView == null)
        {
            holder= new ViewHolder();
            //获取listitem布局文件
            view = layoutinflater.inflate(R.layout.list_item_room, null);

            //获取控件对象
            holder.roomid = (TextView) view.findViewById(R.id.roomid);
            holder.comment = (TextView) view.findViewById(R.id.comment);
            holder.date = (TextView) view.findViewById(R.id.date);
            holder.image = (ImageView) view.findViewById(R.id.thumbnail);

            view.setTag(holder);
        }
        else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        //设置图片和文字
        holder.roomid.setText(device.getRoomid());
        holder.comment.setText(device.getComment());
        holder.date.setText(device.getUpdatedAt());

//        Glide.with(GlobalParams.activity).load(R.drawable.p1).asBitmap().fitCenter().into(new BitmapImageViewTarget(holder.image) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(GlobalParams.activity.getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                holder.image.setImageDrawable(circularBitmapDrawable);
//            }
//        });
        return view;
    }

}
