package com.example.android.sunshine.app;



import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;




import com.example.android.sunshine.app.data.WeatherContract;


// A placeholder fragment containing a simple view
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASTAG = " #Sunshine App";
    private String mForecast;
    private ShareActionProvider mShareActionProvider;

    private static final int DETAIL_LOADER = 0;

    private static final String[]FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,

    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes.
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP =3;
    private static final int COL_WEATHER_MIN_TEMP = 4;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // The detail Activity called via Intent. Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mForecast = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.detail_text)).setText(mForecast);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

         mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share action provider is null?");
        }
    }


    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        /* This prevents the activity we are sharing to from being placed unto the activity stack.
    * What will happen when we don't have this flag is that when you click on the icon, to return
    * to the application later, you may actually end up in another application. But when is used, it
    * will actually return you to your application instead. */
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASTAG);
        shareIntent.setType("text/plain");
        return shareIntent;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER,null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if(intent == null){
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                FORECAST_COLUMNS,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if(!data.moveToFirst()){
            return;
        }

        String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
        String weatherDescription = data.getString(COL_WEATHER_DESC);
        boolean isMetric = Utility.isMetric(getActivity());

        String high  = Utility.formatTemperature(data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        String low = Utility.formatTemperature(data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

        mForecast = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

        TextView detailTextView = (TextView)getView().findViewById(R.id.detail_text);
        detailTextView.setText(mForecast);

        // If onCreateOptionsMenu has already happened, we need o update the share intent now.
        if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }



    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }


}