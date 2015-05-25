package org.copia.windy.app;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import org.copia.windy.app.forecast.DateTimeMark;
import org.copia.windy.app.forecast.ForecastWindReport;
import org.copia.windy.app.forecast.ForecastReportService;
import org.copia.windy.app.user.UserService;

import javax.inject.Inject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class MainActivity extends ActionBarActivity implements OnItemSelectedListener {

    private Logger log = Logger.getAnonymousLogger();

    private static final String LOG_TAG = "MAIN_ACTIVITY";

    private ListView forecastListView;

    private TextView userMessageView;

    private Spinner mainMenu;

    private ForecastTaskForecastType currentForecastType = ForecastTaskForecastType.NOW;

    @Inject
    ForecastReportService forecastReportService;

    @Inject
    UserService userService;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        // Perform injection so that when this call returns all dependencies will be available for use.
        ( (WindyApplication) getApplication() ).inject( this );

        setContentView( R.layout.activity_main );

        mainMenu = (Spinner) findViewById( R.id.main_menu );
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.main_menu_choices, R.layout.main_spinner_item
        );

        adapter.setDropDownViewResource( R.layout.main_spinner_dropdown_item );
        mainMenu.setAdapter( adapter );
        mainMenu.post(
                new Runnable() {
                    public void run() {
                        mainMenu.setOnItemSelectedListener( MainActivity.this );
                    }
                }
        );

        forecastListView = (ListView) findViewById( R.id.list );
        userMessageView = (TextView) findViewById( R.id.message_view );
    }

    @Override
    protected void onResume() {
        super.onResume();

        currentForecastType = ForecastTaskForecastType.NOW;
        mainMenu.setSelection( ForecastTaskForecastType.NOW.getMenuNumber() );
        updateForecast( currentForecastType );
    }

    private List<String> makeAssumptionForecastListItems( List<ForecastWindReport> reports ) {
        ArrayList<ForecastWindReport> restReports = new ArrayList<ForecastWindReport>();
        Collections.sort( reports );
        restReports.addAll( reports );

        List<String> forecastRows = new ArrayList<String>();

        for ( DateTimeMark timeMark : DateTimeMark.values() ) {
            List<String> forecastRowPerTime = new ArrayList<String>();

            for ( ForecastWindReport report : reports ) {
                if ( timeMark.isBelong( report.getTime() ) ) {
                    restReports.remove( report );
                    forecastRowPerTime.add(
                            formatForecastItem(
                                    report.getWindSpeed(),
                                    report.getTime(),
                                    report.getPlace().getTitle()
                            )
                    );
                }
            }

            if ( forecastRowPerTime.size() > 0 ) {
                if ( forecastRows.size() > 0 ) {
                    forecastRows.add( "" );
                }
                forecastRows.add( timeMark.getReadableName() );
                forecastRows.addAll( forecastRowPerTime );
            }
        }

        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat( "MMMM dd" );
        // daily reports
        List<String> forecastRowPerTime = new ArrayList<String>();
        for ( int i = 0; i < restReports.size(); i++ ) {
            ForecastWindReport report = restReports.get( i );
            if ( i != 0 &&
                    !restReports.get( i - 1 ).getTime().equals( restReports.get( i ).getTime() ) ) {
                if ( restReports.get( i - 1 ).getTime().after( DateTimeMark.getLastTimeMark() ) ) {
                    forecastRows.add( "" );
                    forecastRows.add( simpleTimeFormat.format( restReports.get( i - 1 ).getTime() ) );
                    forecastRows.addAll( forecastRowPerTime );
                }
                forecastRowPerTime.clear();
            }
            forecastRowPerTime.add(
                    formatForecastItem(
                            report.getWindSpeed(),
                            report.getPlace().getTitle()
                    )
            );
        }
        if ( restReports.size() > 0 && restReports.get( restReports.size() - 1 ).getTime().after( DateTimeMark.getLastTimeMark() ) ) {
            forecastRows.add( "" );
            forecastRows.add( simpleTimeFormat.format( restReports.get( restReports.size() - 1 ).getTime() ) );
            forecastRows.addAll( forecastRowPerTime );
        }

        return forecastRows;
    }

    private List<String> makeAssumptionNowListItems( List<ForecastWindReport> reports ) {
        List<String> forecastRows = new ArrayList<String>();

        for ( ForecastWindReport report : reports ) {
            forecastRows.add(
                    formatForecastItem(
                            report.getWindSpeed(),
                            report.getTime(),
                            report.getPlace().getTitle()
                    )
            );
        }

        return forecastRows;
    }

    private void updateAssumptionListItems( List<String> rows ) {
        ArrayAdapter<String> items = new ArrayAdapter(
                this, R.layout.forecast_list_item, rows
        );
        forecastListView.setAdapter(
                items
        );
    }

    private String formatForecastItem( Double windSpeed, Date time, String placeTitle ) {
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat( "HH:mm" );
        return String.format(
                "%.2fm/s %s %s",
                windSpeed,
                simpleTimeFormat.format( time ),
                placeTitle
        );
    }

    private String formatForecastItem( Double windSpeed, String placeTitle ) {
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat( "HH:mm" );
        return String.format(
                "%.2fm/s --:-- %s",
                windSpeed,
                placeTitle
        );
    }

    private void removerForecastListItems() {
        forecastListView.setAdapter(
                new ArrayAdapter(
                        this, R.layout.forecast_list_item, new ArrayList()
                )
        );
    }

    @Override
    public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
        Log.d( LOG_TAG, "onItemSelected: Main menu item selected" );
        currentForecastType = ForecastTaskForecastType.get( position );
        updateForecast( currentForecastType );
    }

    private void updateForecast( ForecastTaskForecastType type ) {
        new GetForecastTask().execute( type );
    }

    @Override
    public void onNothingSelected( AdapterView<?> parent ) {
        Log.d( LOG_TAG, "Main menu nothing selected" );
    }

    private void showUserMessage( String message ) {
        userMessageView.setText( message );
        if ( userMessageView.getVisibility() == View.VISIBLE ) {
            log.fine( "Conflict using user message" );
        }
        userMessageView.setVisibility( View.VISIBLE );
    }

    private void hideUserMessage() {
        if ( userMessageView.getVisibility() == View.GONE ) {
            log.fine( "Conflict using user message" );
        }
        userMessageView.setVisibility( View.GONE );
    }

    private void updateUserMessage( String message ) {
        userMessageView.setText( message );
    }

    public enum ForecastTaskForecastType {
        NOW( 0 ),
        FORECAST( 1 ),
        NOTIFICATIONS( 2 );

        private int menuNumber;

        ForecastTaskForecastType( int number ) {
            this.menuNumber = number;
        }

        public static ForecastTaskForecastType get( int number ) {
            for ( ForecastTaskForecastType type : values() ) {
                if ( type.getMenuNumber() == number ) {
                    return type;
                }
            }
            throw new Error( "Unknown menu number" );
        }

        public int getMenuNumber() {
            return menuNumber;
        }
    }

    private class GetForecastTask extends AsyncTask<Object, Integer, ForecastWindReport[]> {

        private ForecastTaskForecastType forecastType;

        @Override
        protected void onPreExecute() {
            removerForecastListItems();
            showUserMessage( "wait...." );
            mainMenu.setEnabled( false );
        }

        @Override
        protected ForecastWindReport[] doInBackground( Object[] params ) {
            ForecastTaskForecastType type = (ForecastTaskForecastType) params[ 0 ];
            this.forecastType = type;

            List<ForecastWindReport> reports;
            try {
                reports = Collections.EMPTY_LIST;
                if ( type == ForecastTaskForecastType.NOW ) {
                    reports = getCurrentState();
                } else if ( type == ForecastTaskForecastType.FORECAST ) {
                    reports = getForecast();
                } else if ( type == ForecastTaskForecastType.NOTIFICATIONS ) {
                    reports = getNotifications();
                } else {
                    throw new Error( "Unknown forecast task type" );
                }
                return reports.toArray( new ForecastWindReport[ reports.size() ] );
            } catch ( IOException e ) {
                return null;
            }
        }

        private List<ForecastWindReport> getCurrentState() throws IOException {
            try {
                return forecastReportService.getCurrentState( userService.getGeoPoints() );
            } catch ( IOException e ) {
                log.fine( "Error getting current wind state" + e.toString() );
                throw e;
            }
        }

        private List<ForecastWindReport> getForecast() throws IOException {
            try {
                return forecastReportService.getForecast( userService.getGeoPoints() );
            } catch ( IOException e ) {
                log.fine( "Error getting forecast wind state" + e.toString() );
                throw e;
            }
        }

        private List<ForecastWindReport> getNotifications() throws IOException {
            try {
                return forecastReportService.getNotifications( userService.getGeoPoints() );
            } catch ( IOException e ) {
                log.fine( "Error getting forecast wind state" + e.toString() );
                throw e;
            }
        }

        @Override
        protected void onPostExecute( ForecastWindReport[] reports ) {
            mainMenu.setEnabled( true );

            if ( reports == null ) {
                updateUserMessage( "Network error" );
                return;
            } else if ( reports.length == 0 ) {
                updateUserMessage( "No items" );
                return;
            }

            hideUserMessage();
            if ( forecastType == ForecastTaskForecastType.NOW ) {
                updateAssumptionListItems(
                        makeAssumptionNowListItems(
                                Arrays.asList( reports )
                        )
                );
            } else {
                updateAssumptionListItems(
                        makeAssumptionForecastListItems(
                                Arrays.asList( reports )
                        )
                );
            }
        }
    }

}
