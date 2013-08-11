package com.github.ironjan.fsupb.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.github.ironjan.fsupb.FSDroid_;
import com.github.ironjan.fsupb.R;
import com.github.ironjan.fsupb.helper.DataKeeper;
import com.github.ironjan.fsupb.helper.DataKeeper_;

public class StatusAppWidgetProvider extends AppWidgetProvider {

	private static final String TAG = StatusAppWidgetProvider.class
			.getSimpleName();

	public StatusAppWidgetProvider() {
		super();
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.v(TAG, "onUpdate");
		final int N = appWidgetIds.length;

		RemoteViews views = addOnClickListener(context);

		Call c = new Call(context, appWidgetManager, appWidgetIds, views);
		DataKeeper dataKeeper = DataKeeper_.getInstance_(context);
		dataKeeper.refresh(c);
	}

	private static RemoteViews addOnClickListener(Context context) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_status);
		Intent intent = new Intent(context, FSDroid_.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		views.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent);
		return views;
	}

	public static class Call {

		private final RemoteViews views;
		private static final String TAG = Call.class.getSimpleName();
		private final AppWidgetManager appWidgetManager;
		private final int[] appWidgetIds;
		private final Context context;

		public Call(Context context, AppWidgetManager appWidgetManager,
				int[] appWidgetIds, RemoteViews views) {
			this.context = context;
			this.appWidgetManager = appWidgetManager;
			this.appWidgetIds = appWidgetIds;
			this.views = views;
		}

		public void setStatus(int parseStatus) {
			Log.v(TAG, "Received status update: " + parseStatus);

			views.setInt(R.id.imgStatus, "setImageLevel", parseStatus);
			views.setTextViewText(R.id.txtStatus, context.getResources()
					.getStringArray(R.array.stati)[parseStatus]);

			appWidgetManager.updateAppWidget(appWidgetIds, views);

		}
	}
}
