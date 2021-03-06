package ml.statshub.statshub.Soccer;

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
import ml.statshub.statshub.R;
import ml.statshub.statshub.Request.HTTPRequest;


public class SCalendarFragment extends Fragment implements  RobotoCalendarView.RobotoCalendarListener{
    private RobotoCalendarView robotoCalendarView;
    private RecyclerView rView;
    private SGamesAdapter gAdapter;
    private View view;
    private AppCompatActivity c;

    public SCalendarFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scalendar, container, false);
        robotoCalendarView = (RobotoCalendarView) view.findViewById(R.id.SrobotoCalendarPicker);
        robotoCalendarView.setRobotoCalendarListener(this);
        rView = (RecyclerView)view.findViewById(R.id.soccerCalendar);
        rView.setLayoutManager(new LinearLayoutManager(SoccerActivity.context));
        gAdapter = new SGamesAdapter(SoccerActivity.context,new ArrayList<Games>());
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

        new BackgroundGetSoccerGames(rView,date).execute();
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

class BackgroundGetSoccerGames extends AsyncTask<Void,Void,String> {
    private String date;
    private String URLQuery;
    private String jsonString;
    private HashMap<String,String> hMap = new HashMap<>();
    private Context c;
    private RecyclerView rView;

    BackgroundGetSoccerGames(RecyclerView rview, String date){
        this.rView = rview;
        this.c = SoccerActivity.context;
        this.date = date;
    }

    @Override
    protected void onPreExecute(){URLQuery = ml.statshub.statshub.Request.URLQuery.URL_SOCCER_GET_GAME;}

    @Override
    protected String doInBackground(Void... params){
        hMap.put("date",date);
        HTTPRequest request = new HTTPRequest();
        jsonString = request.postQueryToHDB(URLQuery,hMap);

        return jsonString;
    }

    @Override
    protected void onPostExecute(String s) {
        SGamesAdapter gAdapter;
        List<Games> gamesList = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(s);
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Games games = new Games();
                games.setId(json_data.getInt("idSGames"));
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
            gAdapter = new SGamesAdapter(c,gamesList);
            rView.setAdapter(gAdapter);

            gAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}


