package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

import org.w3c.dom.Text;

import static android.R.attr.data;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

    private final int VIEW_TYPE_TODAY = 0;
    private final int VIEW_TYPE_FUTURE_DAY = 1;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    @Override
    public int getItemViewType(int position) {
        // This means when the position in the list is zero, it is the today's view type
        // otherwise, it is the future day view type
        return (position == 0)? VIEW_TYPE_TODAY: VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        // The numbers of the instance variables, have to start at zero
        // because the values cannot be greater than the getViewTypeCount
        return 2;
    }

    /*
                This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
                string.
             */
    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor


        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    // Normally, the forecastAdapter class usually would handle only the general weather data layout which contains
    // just one list item. But it can also be made to handle more layout.
    // This means it can handle multiple list items. In this case either list_item_forecast_toda layout or
    // list_item_forecast layout.
    // The process of doing this requires that you call a method "getViewType".
    // This method can make the adapter now choose whether it is the today's forecast that is needed to be called
    // or the normal forecast data where all days are shown.


    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //Choose the layout type to inflate
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        if(viewType == VIEW_TYPE_TODAY){ // If the viewType is today, use this Resource ID
           layoutId = R.layout.list_item_forecast_today ;
        } else if (viewType == VIEW_TYPE_FUTURE_DAY){ // else use this resource ID
            layoutId = R.layout.list_item_forecast;
        }


        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

      // Read weather icon ID from cursor
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_ID);
        // Use placeholder image for now
        ImageView iconView = (ImageView)view.findViewById(R.id.list_item_icon);
        iconView.setImageResource(R.drawable.ic_launcher);

        // TODO Read date from cursor
        long dateId = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        TextView dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
        //dateView.setText(Utility.formatDate(dateId));
        dateView.setText(Utility.getFriendlyDayString(context,dateId));
        //dateView.setText(Utility.getFormattedMonthDay(context,dateId));

        // TODO Read weather forecast from cursor
        String weatherForecast = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        TextView descriptionView = (TextView)view.findViewById(R.id.list_item_forecast_textview);
        descriptionView.setText(weatherForecast);

        // Reader user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        TextView highView = (TextView)view.findViewById(R.id.list_item_high_textview);
        highView.setText(Utility.formatTemperature(high,isMetric));

        //TODO Read low temperature from cursor.
        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        TextView lowView = (TextView)view.findViewById(R.id.list_item_low_textview);
        lowView.setText(Utility.formatTemperature(low,isMetric));
    }


}


