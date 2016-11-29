package ml.statshub.statshub.Hockey;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marcohc.robotocalendar.RobotoCalendarView;

import java.util.Calendar;

import ml.statshub.statshub.R;


public class CalendarFragment extends Fragment implements  RobotoCalendarView.RobotoCalendarListener{
    private RobotoCalendarView robotoCalendarView;
    private View view;

    public CalendarFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        robotoCalendarView = (RobotoCalendarView) view.findViewById(R.id.robotoCalendarPicker);
        robotoCalendarView.setRobotoCalendarListener(this);
        return view;
    }


    @Override
    public void onDayClick(Calendar daySelectedCalendar) {
        Toast.makeText(HockeyActivity.context, "onDayClick: " + daySelectedCalendar.getTime().getMonth() + " " + daySelectedCalendar.getTime().getDate(), Toast.LENGTH_SHORT).show();
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
