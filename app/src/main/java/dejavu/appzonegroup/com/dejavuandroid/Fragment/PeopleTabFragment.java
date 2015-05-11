package dejavu.appzonegroup.com.dejavuandroid.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import dejavu.appzonegroup.com.dejavuandroid.Adapter.MessageAdapter;
import dejavu.appzonegroup.com.dejavuandroid.Adapter.PeopleAdapter;
import dejavu.appzonegroup.com.dejavuandroid.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by CrowdStar on 5/7/2015.
 */
public class PeopleTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contact_layout, container, false);
        StickyListHeadersListView listView = (StickyListHeadersListView ) rootView.findViewById(R.id.list);
        listView.setAdapter(new PeopleAdapter(getActivity()));

        return rootView;
    }
}
