package dejavu.appzonegroup.com.dejavuandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import dejavu.appzonegroup.com.dejavuandroid.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by CrowdStar on 5/7/2015.
 */
public class PeopleAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    String contact[] = {"abe", "abe", "abe", "abe", "abe", "abe", "abe", "abe", "abe", "abe",
            "bel", "bel", "bel", "bel", "bel", "bel", "bel", "bel", "bel", "bel", "bel", "bel", "bel", "cel", "cel"
            , "cel", "cel", "cel", "cel", "cel", "cel", "cel", "cel", "cel", "cel", "cel", "del", "del", "del", "del"
            , "del", "del", "del", "del", "del", "del", "del", "del"
    };

    public PeopleAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return contact.length;
    }

    @Override
    public Object getItem(int position) {
        return contact[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FunctionHolder holder;
        if (convertView == null) {
            holder = new FunctionHolder();
            convertView = mLayoutInflater.inflate(R.layout.people_item, parent, false);
            holder.functionButton = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (FunctionHolder) convertView.getTag();
        }

        holder.functionButton.setText(contact[position]);
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup viewGroup) {
        FunctionHolder holder;
        if (convertView == null) {
            holder = new FunctionHolder();
            convertView = mLayoutInflater.inflate(R.layout.header, viewGroup, false);
            holder.functionButton = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (FunctionHolder) convertView.getTag();
        }
        //set header text as first char in name
        String headerText = "" + contact[position].subSequence(0, 1).charAt(0);
        holder.functionButton.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return contact[position].subSequence(0, 1).charAt(0);
    }
}
