package tabbedactivitytest.mobilesw.kau.time_travel2

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.Toast

/**
 * Created by AHYEON on 2017-12-26.
 */

class WidgetProvider : AppWidgetProvider() {
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val action = intent.action
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE == action) {
            val extras = intent.extras
            //Bundle 은 Key-Value 쌍으로 이루어진 일종의 해쉬맵 자료구조
            //한 Activity에서 Intent 에 putExtras로 Bundle 데이터를 넘겨주고,
            //다른 Activity에서 getExtras로 데이터를 참조하는 방식입니다.
            if (extras != null) {
                val appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS)
                if (appWidgetIds != null && appWidgetIds.size > 0)
                    this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds)
            }
        }//업데이트인 경우
        else if (action == "Click1") {
            Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val N = appWidgetIds.size
        for (i in 0 until N) {
            val appWidgetId = appWidgetIds[i]
            val views = buildViews(context)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun buildURIIntent(context: Context): PendingIntent {
        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://puzzleleaf.tistory.com"))
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    //Click1 이라는 Action을 onReceive로 보낸다.
    private fun buildToastIntent(context: Context): PendingIntent {
        val `in` = Intent("Click1")
        return PendingIntent.getBroadcast(context, 0, `in`, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    //위젯에 멀티 버튼 추가하기
    private fun buildViews(context: Context): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget)
        views.setOnClickPendingIntent(R.id.simple_btn, buildURIIntent(context))

        return views
    }
}
