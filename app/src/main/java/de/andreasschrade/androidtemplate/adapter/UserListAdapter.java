package de.andreasschrade.androidtemplate.adapter;

import java.util.List;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import de.andreasschrade.androidtemplate.GlobalParams;
import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.dummy.UserRoleEnum;
import de.andreasschrade.androidtemplate.model.Userinfo;

/*
 * 用户列表数据适配器
 * @author alisa
 */
public class UserListAdapter extends BaseAdapter   {

    private List<Userinfo> listItems;//数据集合
    private LayoutInflater layoutinflater;//视图容器，用来导入布局

    static class ViewHolder
    {
        private TextView username;
        private TextView password;
        private TextView date;
        private TextView role;
        private ImageView image;
    }

    /*
     * 实例化Adapter
     */
    public UserListAdapter(Context context, List<Userinfo> dataSet)
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
        Userinfo device = listItems.get(position);
        ViewHolder holder;
        View view;
        if(convertView == null)
        {
            holder= new ViewHolder();
            //获取listitem布局文件
            view = layoutinflater.inflate(R.layout.list_item_user, null);

            //获取控件对象
            holder.username = (TextView) view.findViewById(R.id.username);
            holder.password = (TextView) view.findViewById(R.id.password);
            holder.date = (TextView) view.findViewById(R.id.date);
//            holder.role = (TextView) view.findViewById(R.id.role);

//            holder.image = (ImageView) view.findViewById(R.id.collectorlist_image);

            view.setTag(holder);
        }
        else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        //设置图片和文字
        holder.username.setText(device.getUsername());
        holder.password.setText(device.getPassword());
        holder.date.setText(device.getUpdatedAt());
//        holder.role.setText(UserRoleEnum.enumName(device.getRole()));

//        final ImageView img = (ImageView) convertView.findViewById(R.id.thumbnail);
//        Glide.with(GlobalParams.activity).load(R.drawable.icon_login).asBitmap().fitCenter().into(new BitmapImageViewTarget(img) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(GlobalParams.activity.getResources(), resource);
//                circularBitmapDrawable.setCircular(true);
//                img.setImageDrawable(circularBitmapDrawable);
//            }
//        });
        return view;
    }

}
