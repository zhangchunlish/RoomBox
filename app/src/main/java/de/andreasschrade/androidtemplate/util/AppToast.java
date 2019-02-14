package de.andreasschrade.androidtemplate.util;

import de.andreasschrade.androidtemplate.GlobalParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import de.andreasschrade.androidtemplate.R;

public class AppToast extends Toast {

	private static AppToast instance = null;
	private View layout;
	private TextView text;

	private AppToast() {
		super(GlobalParams.activity);
		init();
	}

	public static AppToast getToast() {
		if (instance == null) {
			instance = new AppToast();
		}
		return instance;
	}

	private void init() {
		Context that =GlobalParams.activity;
		LayoutInflater inflate = (LayoutInflater) GlobalParams.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflate.inflate(R.layout.toast_transient_notification, null);
		text = (TextView) layout.findViewById(R.id.message);
		this.setView(layout);
	}

	public void show(String msg) {
		text.setText(msg);
		this.setDuration(Toast.LENGTH_SHORT);
		this.show();
	}

	public void show(int msg) {
		text.setText(msg);
		this.setDuration(Toast.LENGTH_SHORT);
		this.show();
	}

}
