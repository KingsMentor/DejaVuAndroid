package dejavu.appzonegroup.com.dejavuandroid.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import dejavu.appzonegroup.com.dejavuandroid.Adapter.FunctionAdapter;
import dejavu.appzonegroup.com.dejavuandroid.DataBases.Function;
import dejavu.appzonegroup.com.dejavuandroid.DataBases.FunctionCategory;
import dejavu.appzonegroup.com.dejavuandroid.DataSynchronization.ZoneDataUtils;
import dejavu.appzonegroup.com.dejavuandroid.R;


/**
 * Created by CrowdStar on 2/24/2015.
 */
public class CategoryFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_activity, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.main_function_grid);

        ZoneDataUtils.syncDB(getActivity());

        final ArrayList<FunctionCategory> categories = FunctionCategory.getParent(getActivity());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<FunctionCategory> childCategory = FunctionCategory.getChild(getActivity(), categories.get(position).getId());
                ArrayList<String> childArrayList = new ArrayList<String>();
                ArrayList<String> childID = new ArrayList<String>();
                ArrayList<Function> functionArrayList = Function.getFunctionByCategory(getActivity(), categories.get(position).getId());
                ArrayList<String> flowArrayList = new ArrayList<String>();
                ArrayList<String> flowIDArrayList = new ArrayList<String>();
                for (int x = 0; x < childCategory.size(); x++) {
                    childArrayList.add(childCategory.get(x).getName());
                    childID.add("" + childCategory.get(x).getId());
                }

                for (int x = 0; x < functionArrayList.size(); x++) {
                    flowArrayList.add(functionArrayList.get(x).getName());
                    flowIDArrayList.add("" + functionArrayList.get(x).getFlowGuid());
                }
//                ListFunction listfunction = new ListFunction();
//                Bundle bundle = new Bundle();
//                bundle.putStringArrayList(ListFunction.CHILD, childArrayList);
//                bundle.putStringArrayList(ListFunction.CHILD_ID, childID);
//                bundle.putStringArrayList(ListFunction.FLOW, flowArrayList);
//                bundle.putStringArrayList(ListFunction.FLOW_ID, flowIDArrayList);
//                listfunction.setArguments(bundle);


                Intent intent = new Intent(getActivity(), ListFunctionActivity.class);
                intent.putStringArrayListExtra(ListFunction.CHILD, childArrayList);
                intent.putStringArrayListExtra(ListFunction.CHILD_ID, childID);
                intent.putStringArrayListExtra(ListFunction.FLOW, flowArrayList);
                intent.putStringArrayListExtra(ListFunction.FLOW_ID, flowIDArrayList);
                startActivity(intent);

//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.addToBackStack(null);
//                transaction.add(R.id.content_frame, listfunction).commitAllowingStateLoss();
            }
        });

        gridView.setAdapter(new FunctionAdapter(getActivity(), categories));
        ZoneDataUtils.copyDBToSDCard();
        return rootView;
    }


}