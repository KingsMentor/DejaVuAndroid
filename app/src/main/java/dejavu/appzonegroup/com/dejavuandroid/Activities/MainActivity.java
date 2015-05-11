package dejavu.appzonegroup.com.dejavuandroid.Activities;


import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.appzone.zone.orchestra.engine.datatypes.Step;
import com.appzone.zone.orchestra.engine.enums.StepTypeEnum;
import com.astuetz.PagerSlidingTabStrip;

import dejavu.appzonegroup.com.dejavuandroid.DataSynchronization.FlowSyncService;
import dejavu.appzonegroup.com.dejavuandroid.DataSynchronization.ZoneDataUtils;
import dejavu.appzonegroup.com.dejavuandroid.Fragment.HistorytabFragment;
import dejavu.appzonegroup.com.dejavuandroid.Fragment.MessageTabFragment;
import dejavu.appzonegroup.com.dejavuandroid.Fragment.PeopleTabFragment;
import dejavu.appzonegroup.com.dejavuandroid.R;
import dejavu.appzonegroup.com.dejavuandroid.ToastMessageHandler.ShowMessage;

/**
 * Created by CrowdStar on 2/24/2015.
 */
public class MainActivity extends ActionBarActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());


        pager.setAdapter(adapter);

        tabs.setViewPager(pager);

    }

    public class MyPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {


        private final int[] ICON = {R.drawable.ic_home_color_20, R.drawable.ic_home_color_20, R.drawable.ic_home_color_20, R.drawable.ic_home_color_20, R.drawable.ic_home_color_20};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new FlipActivity();
            else if (position == 1)
                return new CategoryFragment();
            else if (position == 2)
                return new PeopleTabFragment();
            else if (position == 3)
                return new MessageTabFragment();
            else if (position == 4)
                return new HistorytabFragment();
            return new SuperAwesomeCardFragment().newInstance(position);

        }

        @Override
        public int getPageIconResId(int i) {
            return ICON[i];
        }
    }



    BroadcastReceiver categoryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(FlowSyncService.PARAM_MESSAGE, -1);
            if (result < 0) {
                new ShowMessage(MainActivity.this, "category failure", Toast.LENGTH_LONG);
            } else {
                new ShowMessage(MainActivity.this, "category success", Toast.LENGTH_LONG);
            }
        }
    };

    BroadcastReceiver functionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(FlowSyncService.PARAM_MESSAGE, -1);
            if (result < 0) {
                new ShowMessage(MainActivity.this, "function failure", Toast.LENGTH_LONG);
            } else {
                new ShowMessage(MainActivity.this, "function success", Toast.LENGTH_LONG);
            }
        }
    };

    BroadcastReceiver flowReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(FlowSyncService.PARAM_MESSAGE);
            if (result == null) {
                new ShowMessage(MainActivity.this, "flow failure", Toast.LENGTH_LONG);
            } else {
                new ShowMessage(MainActivity.this, "flow success", Toast.LENGTH_LONG);
            }
        }
    };

    BroadcastReceiver entityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(FlowSyncService.PARAM_MESSAGE);
            if (result == null) {
                new ShowMessage(MainActivity.this, "enitity failure", Toast.LENGTH_LONG);
            } else {
                new ShowMessage(MainActivity.this, "entity success", Toast.LENGTH_LONG);
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(categoryReceiver,
                new IntentFilter(FlowSyncService.ACTION_DOWNLOAD_FUNCTION_CATEGORY));
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(functionReceiver,
                new IntentFilter(FlowSyncService.ACTION_DOWNLOAD_FUNCTION));
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(flowReceiver,
                new IntentFilter(FlowSyncService.ACTION_DOWNLOAD_FLOW));
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(entityReceiver,
                new IntentFilter(FlowSyncService.ACTION_DOWNLOAD_FLOW_ENTITY));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(categoryReceiver);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(functionReceiver);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(flowReceiver);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(entityReceiver);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActionBar().getDisplayOptions() == (ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE)) {
                onBackPressed();
            }
        }
        if (item.getItemId() == R.id.noti) {
            if (ZoneDataUtils.isNetworkAvailable(MainActivity.this)) {

//              /  new ShowMessage(MainActivity.this, " " + categories.size(), Toast.LENGTH_LONG);
                ZoneDataUtils.syncDB(MainActivity.this);
                FlowSyncService.startActionSync(MainActivity.this);

//                String data = "{\"Name\":\"Edit Corporate\",\"Variables\":[\"Corporate\"],\"InitialStepID\":\"8d1deeb5-6581-4146-911c-ecf76501162c\",\"InitialFields\":[{\"Field\":\"Corporate\",\"FieldType\":2,\"SourceType\":2,\"SubMappings\":{\"ID\":{\"Field\":\"ID\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":5,\"id\":11},\"Name\":{\"Field\":\"Name\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":2,\"id\":12},\"Address\":{\"Field\":\"Address\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":7,\"id\":11},\"Email\":{\"Field\":\"Email\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":4,\"id\":14},\"Phone\":{\"Field\":\"Phone\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":5,\"id\":15},\"RC Number\":{\"Field\":\"RC Number\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":6,\"id\":16},\"Code\":{\"Field\":\"Code\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":7,\"id\":17},\"Business Category\":{\"Field\":\"RC Number\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":8,\"id\":18},\"Logo Path\":{\"Field\":\"Logo Path\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":7,\"id\":18},\"IsDeleted\":{\"Field\":\"IsDeleted\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":7,\"id\":18},\"Branches\":{\"Field\":\"Branches\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":0,\"id\":0},\"ParentCorporate\":{\"Field\":\"ParentCorporate\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":7,\"id\":18}},\"ValueSource\":null,\"type\":0,\"id\":0}],\"Steps\":{\"8d1deeb5-6581-4146-911c-ecf76501162c\":{\"CommandName\":\"{\\\"Name\\\":\\\"Create Account\\\",\\\"Description\\\":\\\"Page to Create Account\\\",\\\"IsNew\\\":true,\\\"Sections\\\":[{\\\"Name\\\":\\\"Account Info\\\",\\\"NumberOfColumn\\\":2,\\\"Parent\\\":null,\\\"RowNumber\\\":1,\\\"IsColumn\\\":false,\\\"Buttons\\\":[],\\\"Fields\\\":[],\\\"ID\\\":1098,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},{\\\"Name\\\":\\\"section827f\\\",\\\"NumberOfColumn\\\":0,\\\"Parent\\\":{\\\"Name\\\":\\\"Account Info\\\",\\\"NumberOfColumn\\\":2,\\\"Parent\\\":null,\\\"RowNumber\\\":1,\\\"IsColumn\\\":false,\\\"Buttons\\\":[],\\\"Fields\\\":[],\\\"ID\\\":1098,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"RowNumber\\\":1,\\\"IsColumn\\\":true,\\\"Buttons\\\":[],\\\"Fields\\\":[{\\\"Name\\\":\\\"Name\\\",\\\"Type\\\":\\\"SingleLineField\\\",\\\"DatasourceType\\\":1,\\\"SectionColumn\\\":null,\\\"Spec\\\":{\\\"IsRequired\\\":true,\\\"IsPrimaryIdentifier\\\":false,\\\"IsUnique\\\":false,\\\"IsEditable\\\":false,\\\"IsNotEditable\\\":false,\\\"FixedLength\\\":0,\\\"MinimumLength\\\":0,\\\"MaximumLength\\\":0,\\\"Format\\\":{\\\"Name\\\":\\\"String\\\",\\\"RegexFormat\\\":null,\\\"Validation\\\":{\\\"Name\\\":\\\"Regular Expression Validator\\\",\\\"FullLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.ValidateRegularExpression, DejaVu.Automation.ValidationModule\\\",\\\"LibraryPath\\\":\\\"DejaVu.ParameterValidation.RegEx.Module.dll\\\",\\\"ShortLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.ValidateRegularExpression\\\",\\\"ID\\\":1,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ValidationFields\\\":\\\"{\\\\\\\"RegularExpression\\\\\\\":\\\\\\\"^.*$\\\\\\\"}\\\",\\\"ID\\\":49,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ParameterMode\\\":\\\"Custom\\\",\\\"ID\\\":16208,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"IsCustom\\\":false,\\\"SourceContent\\\":\\\"\\\",\\\"RowNumber\\\":1,\\\"CustomSpec\\\":null,\\\"EntitySource\\\":null,\\\"TheMatchMode\\\":0,\\\"ID\\\":2365,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},{\\\"Name\\\":\\\"Account Type\\\",\\\"Type\\\":\\\"SingleSelection\\\",\\\"DatasourceType\\\":2,\\\"SectionColumn\\\":null,\\\"Spec\\\":{\\\"IsRequired\\\":false,\\\"IsPrimaryIdentifier\\\":false,\\\"IsUnique\\\":false,\\\"IsEditable\\\":false,\\\"IsNotEditable\\\":false,\\\"FixedLength\\\":0,\\\"MinimumLength\\\":0,\\\"MaximumLength\\\":0,\\\"Format\\\":{\\\"Name\\\":\\\"Account Type\\\",\\\"RegexFormat\\\":null,\\\"Validation\\\":{\\\"Name\\\":\\\"Options Validation\\\",\\\"FullLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.OptionsValidation, DejaVu.Automation.ValidationModule\\\",\\\"LibraryPath\\\":null,\\\"ShortLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.OptionsValidation\\\",\\\"ID\\\":3,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ValidationFields\\\":\\\"{\\\\\\\"Options\\\\\\\":\\\\\\\"Default,Savings,Current\\\\\\\"}\\\",\\\"ID\\\":35,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ParameterMode\\\":\\\"Custom\\\",\\\"ID\\\":16211,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"IsCustom\\\":false,\\\"SourceContent\\\":\\\"Savings,Current\\\",\\\"RowNumber\\\":2,\\\"CustomSpec\\\":null,\\\"EntitySource\\\":null,\\\"TheMatchMode\\\":0,\\\"ID\\\":2366,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null}],\\\"ID\\\":1099,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},{\\\"Name\\\":\\\"sectiond772\\\",\\\"NumberOfColumn\\\":0,\\\"Parent\\\":{\\\"Name\\\":\\\"Account Info\\\",\\\"NumberOfColumn\\\":2,\\\"Parent\\\":null,\\\"RowNumber\\\":1,\\\"IsColumn\\\":false,\\\"Buttons\\\":[],\\\"Fields\\\":[],\\\"ID\\\":1098,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"RowNumber\\\":1,\\\"IsColumn\\\":true,\\\"Buttons\\\":[],\\\"Fields\\\":[{\\\"Name\\\":\\\"Number\\\",\\\"Type\\\":\\\"SingleLineField\\\",\\\"DatasourceType\\\":1,\\\"SectionColumn\\\":null,\\\"Spec\\\":{\\\"IsRequired\\\":true,\\\"IsPrimaryIdentifier\\\":true,\\\"IsUnique\\\":false,\\\"IsEditable\\\":false,\\\"IsNotEditable\\\":false,\\\"FixedLength\\\":0,\\\"MinimumLength\\\":0,\\\"MaximumLength\\\":0,\\\"Format\\\":{\\\"Name\\\":\\\"10-Digit Code\\\",\\\"RegexFormat\\\":null,\\\"Validation\\\":{\\\"Name\\\":\\\"Regular Expression Validator\\\",\\\"FullLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.ValidateRegularExpression, DejaVu.Automation.ValidationModule\\\",\\\"LibraryPath\\\":\\\"DejaVu.ParameterValidation.RegEx.Module.dll\\\",\\\"ShortLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.ValidateRegularExpression\\\",\\\"ID\\\":1,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ValidationFields\\\":\\\"{\\\\\\\"RegularExpression\\\\\\\":\\\\\\\"^\\\\\\\\\\\\\\\\d{10}$\\\\\\\"}\\\",\\\"ID\\\":23,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ParameterMode\\\":\\\"Custom\\\",\\\"ID\\\":16209,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"IsCustom\\\":false,\\\"SourceContent\\\":\\\"\\\",\\\"RowNumber\\\":1,\\\"CustomSpec\\\":null,\\\"EntitySource\\\":null,\\\"TheMatchMode\\\":0,\\\"ID\\\":2367,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},{\\\"Name\\\":\\\"Account Category\\\",\\\"Type\\\":\\\"SingleSelection\\\",\\\"DatasourceType\\\":2,\\\"SectionColumn\\\":null,\\\"Spec\\\":{\\\"IsRequired\\\":false,\\\"IsPrimaryIdentifier\\\":false,\\\"IsUnique\\\":false,\\\"IsEditable\\\":false,\\\"IsNotEditable\\\":false,\\\"FixedLength\\\":0,\\\"MinimumLength\\\":0,\\\"MaximumLength\\\":0,\\\"Format\\\":{\\\"Name\\\":\\\"Account Category\\\",\\\"RegexFormat\\\":null,\\\"Validation\\\":{\\\"Name\\\":\\\"Options Validation\\\",\\\"FullLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.OptionsValidation, DejaVu.Automation.ValidationModule\\\",\\\"LibraryPath\\\":null,\\\"ShortLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.OptionsValidation\\\",\\\"ID\\\":3,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ValidationFields\\\":\\\"{\\\\\\\"Options\\\\\\\":\\\\\\\"Null,Receivable,Payable\\\\\\\"}\\\",\\\"ID\\\":36,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ParameterMode\\\":\\\"Custom\\\",\\\"ID\\\":16212,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"IsCustom\\\":false,\\\"SourceContent\\\":\\\"Receivable,Payable\\\",\\\"RowNumber\\\":2,\\\"CustomSpec\\\":null,\\\"EntitySource\\\":null,\\\"TheMatchMode\\\":0,\\\"ID\\\":2368,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null}],\\\"ID\\\":1100,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null}],\\\"Buttons\\\":[{\\\"ButtonName\\\":\\\"Next\\\",\\\"Name\\\":\\\"Save Clicked\\\",\\\"SectionColumn\\\":null,\\\"ID\\\":128,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null}],\\\"AllFields\\\":[{\\\"Name\\\":\\\"Name\\\",\\\"Type\\\":\\\"SingleLineField\\\",\\\"DatasourceType\\\":1,\\\"SectionColumn\\\":null,\\\"Spec\\\":{\\\"IsRequired\\\":true,\\\"IsPrimaryIdentifier\\\":false,\\\"IsUnique\\\":false,\\\"IsEditable\\\":false,\\\"IsNotEditable\\\":false,\\\"FixedLength\\\":0,\\\"MinimumLength\\\":0,\\\"MaximumLength\\\":0,\\\"Format\\\":{\\\"Name\\\":\\\"String\\\",\\\"RegexFormat\\\":null,\\\"Validation\\\":{\\\"Name\\\":\\\"Regular Expression Validator\\\",\\\"FullLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.ValidateRegularExpression, DejaVu.Automation.ValidationModule\\\",\\\"LibraryPath\\\":\\\"DejaVu.ParameterValidation.RegEx.Module.dll\\\",\\\"ShortLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.ValidateRegularExpression\\\",\\\"ID\\\":1,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ValidationFields\\\":\\\"{\\\\\\\"RegularExpression\\\\\\\":\\\\\\\"^.*$\\\\\\\"}\\\",\\\"ID\\\":49,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ParameterMode\\\":\\\"Custom\\\",\\\"ID\\\":16208,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"IsCustom\\\":false,\\\"SourceContent\\\":\\\"\\\",\\\"RowNumber\\\":1,\\\"CustomSpec\\\":null,\\\"EntitySource\\\":null,\\\"TheMatchMode\\\":0,\\\"ID\\\":2365,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},{\\\"Name\\\":\\\"Account Type\\\",\\\"Type\\\":\\\"SingleSelection\\\",\\\"DatasourceType\\\":2,\\\"SectionColumn\\\":null,\\\"Spec\\\":{\\\"IsRequired\\\":false,\\\"IsPrimaryIdentifier\\\":false,\\\"IsUnique\\\":false,\\\"IsEditable\\\":false,\\\"IsNotEditable\\\":false,\\\"FixedLength\\\":0,\\\"MinimumLength\\\":0,\\\"MaximumLength\\\":0,\\\"Format\\\":{\\\"Name\\\":\\\"Account Type\\\",\\\"RegexFormat\\\":null,\\\"Validation\\\":{\\\"Name\\\":\\\"Options Validation\\\",\\\"FullLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.OptionsValidation, DejaVu.Automation.ValidationModule\\\",\\\"LibraryPath\\\":null,\\\"ShortLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.OptionsValidation\\\",\\\"ID\\\":3,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ValidationFields\\\":\\\"{\\\\\\\"Options\\\\\\\":\\\\\\\"Default,Savings,Current\\\\\\\"}\\\",\\\"ID\\\":35,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ParameterMode\\\":\\\"Custom\\\",\\\"ID\\\":16211,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"IsCustom\\\":false,\\\"SourceContent\\\":\\\"Savings,Current\\\",\\\"RowNumber\\\":2,\\\"CustomSpec\\\":null,\\\"EntitySource\\\":null,\\\"TheMatchMode\\\":0,\\\"ID\\\":2366,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},{\\\"Name\\\":\\\"Number\\\",\\\"Type\\\":\\\"SingleLineField\\\",\\\"DatasourceType\\\":1,\\\"SectionColumn\\\":null,\\\"Spec\\\":{\\\"IsRequired\\\":true,\\\"IsPrimaryIdentifier\\\":true,\\\"IsUnique\\\":false,\\\"IsEditable\\\":false,\\\"IsNotEditable\\\":false,\\\"FixedLength\\\":0,\\\"MinimumLength\\\":0,\\\"MaximumLength\\\":0,\\\"Format\\\":{\\\"Name\\\":\\\"10-Digit Code\\\",\\\"RegexFormat\\\":null,\\\"Validation\\\":{\\\"Name\\\":\\\"Regular Expression Validator\\\",\\\"FullLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.ValidateRegularExpression, DejaVu.Automation.ValidationModule\\\",\\\"LibraryPath\\\":\\\"DejaVu.ParameterValidation.RegEx.Module.dll\\\",\\\"ShortLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.ValidateRegularExpression\\\",\\\"ID\\\":1,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ValidationFields\\\":\\\"{\\\\\\\"RegularExpression\\\\\\\":\\\\\\\"^\\\\\\\\\\\\\\\\d{10}$\\\\\\\"}\\\",\\\"ID\\\":23,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ParameterMode\\\":\\\"Custom\\\",\\\"ID\\\":16209,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"IsCustom\\\":false,\\\"SourceContent\\\":\\\"\\\",\\\"RowNumber\\\":1,\\\"CustomSpec\\\":null,\\\"EntitySource\\\":null,\\\"TheMatchMode\\\":0,\\\"ID\\\":2367,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},{\\\"Name\\\":\\\"Account Category\\\",\\\"Type\\\":\\\"SingleSelection\\\",\\\"DatasourceType\\\":2,\\\"SectionColumn\\\":null,\\\"Spec\\\":{\\\"IsRequired\\\":false,\\\"IsPrimaryIdentifier\\\":false,\\\"IsUnique\\\":false,\\\"IsEditable\\\":false,\\\"IsNotEditable\\\":false,\\\"FixedLength\\\":0,\\\"MinimumLength\\\":0,\\\"MaximumLength\\\":0,\\\"Format\\\":{\\\"Name\\\":\\\"Account Category\\\",\\\"RegexFormat\\\":null,\\\"Validation\\\":{\\\"Name\\\":\\\"Options Validation\\\",\\\"FullLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.OptionsValidation, DejaVu.Automation.ValidationModule\\\",\\\"LibraryPath\\\":null,\\\"ShortLibraryName\\\":\\\"DejaVu.Automation.ValidationModule.OptionsValidation\\\",\\\"ID\\\":3,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ValidationFields\\\":\\\"{\\\\\\\"Options\\\\\\\":\\\\\\\"Null,Receivable,Payable\\\\\\\"}\\\",\\\"ID\\\":36,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ParameterMode\\\":\\\"Custom\\\",\\\"ID\\\":16212,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"IsCustom\\\":false,\\\"SourceContent\\\":\\\"Receivable,Payable\\\",\\\"RowNumber\\\":2,\\\"CustomSpec\\\":null,\\\"EntitySource\\\":null,\\\"TheMatchMode\\\":0,\\\"ID\\\":2368,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null}],\\\"Implementation\\\":\\\"DejaVu.UI\\\",\\\"Category\\\":{\\\"Name\\\":\\\"Account Management\\\",\\\"Description\\\":\\\"This is for grouping accounts\\\",\\\"ID\\\":16,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null},\\\"ID\\\":240,\\\"IsToSave\\\":false,\\\"VersionNumber\\\":null,\\\"InstitutionCode\\\":null,\\\"SuccessMessage\\\":null,\\\"FailureMessage\\\":null}\",\"Events\":{\"UpdateCorporate Event\":{\"AttachedCommands\":[{\"CommandMappings\":[{\"Field\":\"Name\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":1,\"id\":11},{\"Field\":\"Address\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":12,\"id\":11},{\"Field\":\"Email\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":10,\"id\":11},{\"Field\":\"Phone\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":7,\"id\":11},{\"Field\":\"RC Number\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":18,\"id\":11},{\"Field\":\"Business Category\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":5,\"id\":11},{\"Field\":\"Code\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":3,\"id\":11},{\"Field\":\"Logo Path\",\"FieldType\":0,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":null,\"type\":16,\"id\":11}],\"StepID\":\"0ec0e414-9f49-4a0c-9f12-d1d7487c11bb\"}],\"Name\":\"UpdateCorporate Event\",\"WorkflowVariablesMapping\":[{\"Field\":\"Corporate\",\"FieldType\":2,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":\"Corporate\",\"type\":0,\"id\":0}]}},\"Id\":\"8d1deeb5-6581-4146-911c-ecf76501162c\",\"ServiceName\":\"DejaVu.UI\"},\"0ec0e414-9f49-4a0c-9f12-d1d7487c11bb\":{\"CommandName\":\"Create Bank\",\"Events\":{\"Corporate detail has been updated successfully.\":{\"AttachedCommands\":[{\"CommandMappings\":[{\"Field\":\"Message\",\"FieldType\":0,\"SourceType\":3,\"SubMappings\":null,\"ValueSource\":\"Corporate detail has been updated successfully.\",\"type\":3,\"id\":11}],\"StepID\":\"d119050d-1c21-427d-a187-56ccb98026d1\"}],\"Name\":\"Corporate detail has been updated successfully.\",\"WorkflowVariablesMapping\":[{\"Field\":null,\"FieldType\":2,\"SourceType\":1,\"SubMappings\":null,\"ValueSource\":\"Corporate\",\"type\":0,\"id\":0}]}},\"Id\":\"0ec0e414-9f49-4a0c-9f12-d1d7487c11bb\",\"ServiceName\":\"BBC.CorporateSetupServiceService.UpdateAddedCorporate\"},\"d119050d-1c21-427d-a187-56ccb98026d1\":{\"CommandName\":\"{\\r\\n  \\\"Name\\\": \\\"doSomething\\\",\\r\\n  \\\"Arguments\\\": \\\"\\\",\\r\\n  \\\"Script\\\": \\\"var a = 5, b = 6; var c = a * b; return c;\\\"\\r\\n}\",\"Events\":null,\"Id\":\"0ec0e414-9f49-4a0c-9f12-d1d7487c11bb\",\"ServiceName\":\"DejaVu.Script\"}}}";
//                ClientFlows f = ClientFlows.getFlowById(MainActivity.this, "70d22aa1-ca2c-4dbe-bedb-065428c74973");
//                if (f == null) {
//                    f = new ClientFlows();
//                    f.setFlows(data);
//                    f.setFlowsID("70d22aa1-ca2c-4dbe-bedb-065428c74973");
//                    f.save(this);
//                } else {
//                    f.setFlows(data);
//                    f.setFlowsID("70d22aa1-ca2c-4dbe-bedb-065428c74973");
//                    f.save(this);
//                    new ShowMessage(MainActivity.this, "saved flow", Toast.LENGTH_LONG);
//                }
            } else {
                new ShowMessage(MainActivity.this, "confirm failure", Toast.LENGTH_LONG);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}