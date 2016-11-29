package ml.statshub.statshub.Hockey;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marcohc.robotocalendar.RobotoCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ml.statshub.statshub.Class.Games;
import ml.statshub.statshub.Class.Players;
import ml.statshub.statshub.Marqueur.Hockey.HGamesAdapter;
import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;
import ml.statshub.statshub.Request.URLQuery;


public class CalendarFragment extends Fragment implements  RobotoCalendarView.RobotoCalendarListener{
    private RobotoCalendarView robotoCalendarView;
    private RecyclerView rView;
    private GamesAdapter gAdapter;
    private View view;
    private AppCompatActivity c;

    public CalendarFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        robotoCalendarView = (RobotoCalendarView) view.findViewById(R.id.robotoCalendarPicker);
        robotoCalendarView.setRobotoCalendarListener(this);
        rView = (RecyclerView)view.findViewById(R.id.hockeyCalendar);
        rView.setLayoutManager(new LinearLayoutManager(HockeyActivity.context));
        gAdapter = new GamesAdapter(HockeyActivity.context,new ArrayList<Games>());
        rView.setAdapter(gAdapter);
        return view;
    }


    @Override
    public void onDayClick(Calendar daySelectedCalendar) {
        String debut = "20";
        int year = daySelectedCalendar.getTime().getYear();
        int month = daySelectedCalendar.getTime().getMonth() + 1;
        int day = daySelectedCalendar.getTime().getDate();

        String yearToChange = String.valueOf(year);
        String monthToChange = String.valueOf(month);
        String dayToChange = String.valueOf(day);
        if(monthToChange.length() == 1)
            monthToChange = String.format(Locale.CANADA_FRENCH,"%02d", month);
        if(dayToChange.length() == 1)
            dayToChange = String.format(Locale.CANADA_FRENCH,"%02d", day);
        year = Integer.parseInt(yearToChange.substring(yearToChange.length() - 2));
        String date = debut + year + "-" + monthToChange + "-" + dayToChange;

        new BackgroundGetGames(rView,date).execute();
        //Toast.makeText(HockeyActivity.context, date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDayLongClick(Calendar calendar) {

    }

    @Override
    public void onRightButtonClick() {

    }

    @Override
    public void onLeftButtonClick() {

    }
}

class BackgroundGetGames extends AsyncTask<Void,Void,String>{
    private String date;
    private String URLQuery;
    private String jsonString;
    private HashMap<String,String> hMap = new HashMap<>();
    private Context c;
    private RecyclerView rView;

    BackgroundGetGames(RecyclerView rview, String date){
        this.rView = rview;
        this.c = HockeyActivity.context;
        this.date = date;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_HOCKEY_GET_GAME;}

    @Override
    protected String doInBackground(Void... params){
        hMap.put("date",date);
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URLQuery,hMap);

        return jsonString;
    }

    @Override
    protected void onPostExecute(String s) {
        GamesAdapter gAdapter;
        List<Games> gamesList = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(s);
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Games games = new Games();
                games.setId(json_data.getInt("idHGames"));
                games.setAway(json_data.getInt("idAwayTeams"));
                games.setAwayName(json_data.getString("AwayName"));
                games.setHome(json_data.getInt("idHomeTeams"));
                games.setHomeName(json_data.getString("HomeName"));
                games.setLocation(json_data.getString("Location"));
                String date = json_data.getString("Time");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CANADA_FRENCH);
                Date newDate = format.parse(date);
                games.setDate(format.format(newDate));
                gamesList.add(games);
            }
            gAdapter = new GamesAdapter(c,gamesList);
            rView.setAdapter(gAdapter);

            gAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
