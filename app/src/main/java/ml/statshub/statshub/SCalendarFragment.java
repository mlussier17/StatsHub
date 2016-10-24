package ml.statshub.statshub;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcohc.robotocalendar.RobotoCalendarView;

import java.util.Calendar;


public class SCalendarFragment extends Fragment implements  RobotoCalendarView.RobotoCalendarListener{
    private RobotoCalendarView robotoCalendarView;
    private View view;

    public SCalendarFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scalendar, container, false);
        robotoCalendarView = (RobotoCalendarView) view.findViewById(R.id.SrobotoCalendarPicker);
        robotoCalendarView.setRobotoCalendarListener(this);
        return view;
    }


    @Override
    public void onDayClick(Calendar calendar) {

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


