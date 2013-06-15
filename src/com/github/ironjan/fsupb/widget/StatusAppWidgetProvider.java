package com.github.ironjan.fsupb.widget;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.util.*;
import android.widget.*;

import com.github.ironjan.fsupb.*;
import com.github.ironjan.fsupb.stuff.*;

public class StatusAppWidgetProvider extends AppWidgetProvider {

	private static final String TAG = StatusAppWidgetProvider.class
			.getSimpleName();
	StatusBean statusBean;

	public StatusAppWidgetProvider() {
		super();
		statusBean = StatusBean_.getInstance_(null);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.v(TAG, "onUpdate");
		final int N = appWidgetIds.length;

		Call c = new Call(context, appWidgetManager, appWidgetIds);

		statusBean.refreshStatus(c);

	}

	private static class Call implements StatusCallback {

		private final RemoteViews views;
		private static final String TAG = Call.class.getSimpleName();
		private final AppWidgetManager appWidgetManager;
		private final int[] appWidgetIds;
		private final Context context;

		public Call(Context context, AppWidgetManager appWidgetManager,
				int[] appWidgetIds) {
			this.context = context;
			this.appWidgetManager = appWidgetManager;
			this.appWidgetIds = appWidgetIds;
			views = new RemoteViews(context.getPackageName(),
					R.layout.widget_status);
		}

		@Override
		public void setStatus(int parseStatus) {
			Log.v(TAG, "Received status update: " + parseStatus);

			views.setInt(R.id.imgStatus, "setImageLevel", parseStatus);
			views.setTextViewText(R.id.txtStatus, context.getResources()
					.getStringArray(R.array.stati)[parseStatus]);

			Intent intent = new Intent(context, Test_.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, 0);
			views.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent);

			appWidgetManager.updateAppWidget(appWidgetIds, views);

		}
	}
}
