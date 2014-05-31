package org.teiath.teiesc;

import java.util.Calendar;
import java.util.Collection;

import javax.security.auth.login.LoginException;

import org.teiath.teiesc.dataitems.DataItemReference;
import org.teiath.teiesc.options.SharedSettingsTransaction;
import org.teiath.teiesc.options.ViewOptions;
import org.teiath.teiesc.options.ViewOptionsTransaction;
import org.teiath.teiesc.provider.dbmetadata.FullTablesPack;
import org.teiath.teiesc.provider.dbtransaction.FullDbTranPack;
import org.teiath.teiesc.ui.dialogs.OptionsDialog;
import org.teiath.teiesc.updater.Updater;
import org.teiath.teiesc.updater.Updater.UpdateResult;
import org.teiath.teiesc.ws.RestWsHandlerFactory;
import org.teiath.teiesc.ws.WsTableNamesProj;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ActionBar.TabListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity
{
    private DayFragmentAdapter mSectionsPagerAdapter;
    private ViewPager            mViewPager;
    private ActionBar            mActionBar;
    private DaysSolver           mDays;
    private RestWsHandlerFactory mConGet;
    private DataPack             mDataPack;
    private FullTablesPack       mTblsPack;
    private FullDbTranPack       mTranPack;
    private AdaptersHolder       mAdapters;
    private Updater              mUpdater;
    private ProgressDialog       mProgressDialog;
    
    private ViewOptions          mOptions;
    
    public static final String SETTING_JUST_LOGED_IN = "JustLogedIn"; 
    public static final String USERNAME              = "USERNAME";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initObjs();
        
        mAdapters = new AdaptersHolder(getLayoutInflater());
        
        initActionBarAndViewPager();
        
        refresh();
        if(null != savedInstanceState)
        {
            int p = savedInstanceState.getInt("PAGE");
            mActionBar.setSelectedNavigationItem(p);
        }
        else
        {
            focusOnDay();
            updateCheck(getIntent().getBooleanExtra(SETTING_JUST_LOGED_IN, false));
        }
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    private void readDb()
    {
        mDataPack.clearAll();
        DataToDataPack.fillAll(mDataPack, mTranPack, this);
    }
    private void fillAdapters()
    {
        mAdapters.clearAll(false);
        
        for (int i = 0; i<DayFragmentAdapter.FRAGMENTS_COUNT; i++)
        {
            mAdapters.get(i).addAll(mDataPack.getLectureLessonPack(mOptions, positionToDay(i)));
        }
        
        mAdapters.notifyChanges();
    }
    private void refresh()
    {
        readDb();
        fillAdapters();
    }
    
    private void initActionBarAndViewPager()
    {
        mActionBar = this.getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setTitle(R.string.main_name);
        //mActionBar.setDisplayShowTitleEnabled(false);
        //mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setIcon(R.drawable.ic_schedule);
        // ViewPager
        // Δημιουργία του adapter που θα επιστρέφει τα fragments
        mSectionsPagerAdapter = new DayFragmentAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
        final ViewPager.OnPageChangeListener onpcl = new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                mActionBar.setSelectedNavigationItem(position);
            }
        };
        mViewPager.setOnPageChangeListener(onpcl);
        
        
        final TabListener tabListener = new TabListener()
        {
            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft)
            { }

            @Override
            public void onTabSelected(Tab tab, FragmentTransaction ft)
            {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft)
            { }
        };
        
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
        {
            Tab tb = mActionBar.newTab();
            tb.setText(mSectionsPagerAdapter.getPageTitle(i));
            tb.setTabListener(tabListener);
            mActionBar.addTab(tb);
        }
    }

    private void initObjs()
    {
        String username = SharedSettingsTransaction.getUsername(this);
        String password = SharedSettingsTransaction.getPassword(this);
        
        this.mOptions  = new ViewOptions();
        this.mDays     = new DaysSolver(this);
        this.mConGet   = new RestWsHandlerFactory(this);
        this.mDataPack = new DataPack();
        this.mTblsPack = new FullTablesPack();
        this.mTranPack = new FullDbTranPack(mTblsPack);
        this.mUpdater  = new Updater(
                getContentResolver(), mTranPack, mConGet,
                new WsTableNamesProj(), 
                username, password);
        
        //set mUpdater onUpdate
        this.mUpdater.setOnUpdateFinishListener( new Updater.OnUpdateListener()
        {
            @Override
            public void onJobFinished(UpdateResult result)
            {
                if(result.hasError() && (result.getException() instanceof LoginException))
                {
                    signOut(true);
                }
                if(result.hasError())
                {
                    Toast.makeText(MainActivity.this, R.string.error_on_update, Toast.LENGTH_LONG)
                    .show();
                }
                refresh();
                popProgressDialogOff();
            }
        });
        
        this.mUpdater.setOnUpdateCheckListener(new Updater.OnUpdateCheckListener()
        {
            
            @Override
            public void onCheckFinished(Collection<DataItemReference> tables)
            {
                popProgressDialogOff();
                if(null == tables) return;
                if(tables.size() == 0)
                {
                   //popToast(R.string.update_not_found);
                   return;
                }
                popUpdateYesNo(tables);
            }
        });
    }
    
    private void goToLoginScreen(String userName)
    {
        Intent goToLogin = new Intent(this, LoginActivity.class);
        goToLogin.putExtra(USERNAME, userName);
        startActivity(goToLogin);
        finish();
    }

    private void updateCheck(boolean displayPop)
    {
        if (ConnectionChecker.isConnected(this))
        {
            this.mUpdater.checkForUpdate();
            if(displayPop)
            {
                popProgressDialog(R.string.update_checking_title, R.string.update_checking_message);
            }
        }
        else
        {
            if(displayPop) popToast(R.string.no_connection);
        }
    }
    private void updateProcedure(Collection<DataItemReference> tablesToBeUpdated)
    {
        if (ConnectionChecker.isConnected(this))
        {
            this.mUpdater.startUpdate(tablesToBeUpdated.toArray(new DataItemReference[tablesToBeUpdated.size()]));
            popProgressDialog(R.string.update_title, R.string.update_message);
        }
        else
        {
            popToast(R.string.no_connection);
        }
    } 
    
    private void popUpdateYesNo(final Collection<DataItemReference> tables)
    {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                case DialogInterface.BUTTON_POSITIVE:
                    updateProcedure(tables);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    
                    break;
                default:
                    break;
                }
            }
        };
        
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(R.string.update_question_message);
        b.setPositiveButton(android.R.string.yes, listener);
        b.setNegativeButton(android.R.string.no,  listener);
        b.show();
    }
    private void popToast(int messageId)
    {
        Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
    }
    private void popProgressDialog(int titleID, int messageID)
    {
        if(null == mProgressDialog)
        {
            mProgressDialog = ProgressDialog.show(this, getString(titleID), getString(messageID));
        }
        else
        {
            mProgressDialog.setTitle(getString(titleID));
            mProgressDialog.setMessage(getString(messageID));
        }
    }
    private void popProgressDialogOff()
    {
        if(null == mProgressDialog) return;
        else
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    
    private void focusOnDay()
    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK); 
        mViewPager.setCurrentItem(limitsTest(dayToPosition(day)));
    }
    private int limitsTest(int tab)
    {
        tab = tab < 0 ? 0 : tab;
        return tab > 4 ? 0 : tab;
    }
    
    private static final int DAY_POS_DIFF = 2;
    private static int positionToDay(int position)
    {
        return position + DAY_POS_DIFF;
    }
    private static int dayToPosition(int day)
    {
        return day - DAY_POS_DIFF;
    }
    
    
    /* 
     * Options Menu...
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.menu_update:
            this.updateCheck(true);
            return true;
        case R.id.menu_settings:
            showOptionsDialog();
            return true;
        case R.id.menu_sign_out:
            signOut(false);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void signOut(boolean sendUserName)
    {
        String toSend = "";
        if(sendUserName)
        {
            toSend = SharedSettingsTransaction.getUsername(this);
        }
        mTranPack.deleteAll(getContentResolver());
        SharedSettingsTransaction.reset(MainActivity.this);
        goToLoginScreen(toSend);
    }
    
    private void showOptionsDialog()
    {
        OptionsDialog opts = new OptionsDialog();
        Bundle b = new Bundle();
        ViewOptionsTransaction.putInBundle(b, mOptions);
        opts.setArguments(b);
        
        opts.setOnPositiveButtonListener(new OptionsDialog.onPositiveButtonListener()
        {
            @Override
            public void onClicked(ViewOptions options)
            {
                mOptions.copy(options);
                fillAdapters();
            }
        });
        
        opts.show(getSupportFragmentManager(), "OptionsMenu");
        
    }
    

    public class DayFragmentAdapter extends FragmentStatePagerAdapter
    {
        public static final int FRAGMENTS_COUNT = 5;
        
        public DayFragmentAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return DayFragment.getInstance(position, mAdapters.get(position));
        }

        @Override
        public int getCount()
        {
            return FRAGMENTS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mDays.getDay(positionToDay(position));
        }
        
        @Override
        public int getItemPosition(Object item)
        {
            return POSITION_NONE;
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) 
    {
        super.onSaveInstanceState(outState);
        //  Ugly-small hack
        outState.remove("android:viewHierarchyState");
        outState.putInt("PAGE", mActionBar.getSelectedNavigationIndex());
    }

}
