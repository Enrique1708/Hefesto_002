package corp.carrizales.hefesto_002;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Luis on 23/10/2017.
 */

public class Temperatura_fragment extends Fragment{

    private static final String TAG = "Temperatura_fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.temperatura_fragment,container,false);

        return view;
    }
}
