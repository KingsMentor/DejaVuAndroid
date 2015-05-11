package dejavu.appzonegroup.com.dejavuandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ViewFlipper;

import dejavu.appzonegroup.com.dejavuandroid.R;

/**
 * Created by CrowdStar on 2/26/2015.
 */
public class HomeAdapter extends BaseAdapter {


    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int h;

    public HomeAdapter(Context context, int height) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        h = height;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FunctionHolder holder;
        if (convertView == null) {
            holder = new FunctionHolder();
            convertView = mLayoutInflater.inflate(R.layout.view_flipper, parent, false);
            holder.flipper = (ViewFlipper) convertView.findViewById(R.id.flipper);

        } else {
            holder = (FunctionHolder) convertView.getTag();
        }


        View topView = mLayoutInflater.inflate(R.layout.first_item, null);
        View bottomView = mLayoutInflater.inflate(R.layout.second_item, null);

        holder.flipper.addView(topView, 0);
        holder.flipper.addView(bottomView, 1);

        startAnimation(holder.flipper, position);
        convertView.setMinimumHeight(h - 74);
        convertView.setTag(holder);
        return convertView;
    }

    private void startAnimation(final ViewFlipper imageView, int position) {
        // TODO Auto-generated method stub
        int iDuration = 500;
        final ScaleAnimation scaleAnimationIN = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, (float) 0.5,
                Animation.RELATIVE_TO_SELF, (float) 0.5);
        final ScaleAnimation scaleAnimationOUT = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, (float) 0.5,
                Animation.RELATIVE_TO_SELF, (float) 0.5);
        scaleAnimationIN.setStartOffset(8000 + position * 2000);
        scaleAnimationIN.setDuration(iDuration);
        scaleAnimationOUT.setStartOffset(0);
        scaleAnimationOUT.setDuration(iDuration);
        scaleAnimationIN.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                imageView.startAnimation(scaleAnimationOUT);
            }
        });
        scaleAnimationOUT.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                flipViewFlipper(imageView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

                imageView.startAnimation(scaleAnimationIN);
            }
        });
        imageView.startAnimation(scaleAnimationIN);


    }

    private void flipViewFlipper(ViewFlipper flipper) {

        if (flipper.getDisplayedChild() == 0) {
            flipper.setDisplayedChild(1);
        } else {
            flipper.setDisplayedChild(0);
        }
    }
}
