package org.teiath.teiesc.ui.dialogs;

import org.teiath.teiesc.R;
import org.teiath.teiesc.options.ViewOptions;
import org.teiath.teiesc.options.ViewOptionsTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.app.AlertDialog.Builder;

public class OptionsDialog extends DialogFragment
{
    public interface onPositiveButtonListener
    {
        public void onClicked(ViewOptions options);
    }
    
    private final ViewOptions mOptions;
    private onPositiveButtonListener mListener = null;
    
    public OptionsDialog()
    {
        super();
        this.mOptions = new ViewOptions();
    }
    
    
    public void setOnPositiveButtonListener(onPositiveButtonListener listener)
    {
        this.mListener = listener;
    }
    
    private void setEnable(Switch[] sw, boolean enabled)
    {
        for(int i=0; i<sw.length; i++)
        {
            sw[i].setEnabled(enabled);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        ViewOptionsTransaction.getFromBundle(this.getArguments(), mOptions);
        
        Builder builder = new Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_options, null);
        
        final Switch[] sw_eras = new Switch[ViewOptions.ERAS];
        sw_eras[0] = (Switch) rootView.findViewById(R.id.sw_a);
        sw_eras[1] = (Switch) rootView.findViewById(R.id.sw_b);
        sw_eras[2] = (Switch) rootView.findViewById(R.id.sw_c);
        sw_eras[3] = (Switch) rootView.findViewById(R.id.sw_d);
        sw_eras[4] = (Switch) rootView.findViewById(R.id.sw_e);
        sw_eras[5] = (Switch) rootView.findViewById(R.id.sw_f);
        sw_eras[6] = (Switch) rootView.findViewById(R.id.sw_g);
        
        CheckBox chRegisteredOnly = (CheckBox) rootView.findViewById(R.id.ch_onlyregistered);
        CheckBox chShowLabs       = (CheckBox) rootView.findViewById(R.id.ch_show_labs);
        CheckBox chShowTheories   = (CheckBox) rootView.findViewById(R.id.ch_show_theories);
        CheckBox chShowAllEras    = (CheckBox) rootView.findViewById(R.id.ch_all_eras);
        
        OnCheckedChangeListener chListener = new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked)
            {
                switch (buttonView.getId())
                {
                case R.id.ch_onlyregistered:
                    mOptions.setShowOnlyRegistered(isChecked);
                    break;
                case R.id.ch_show_labs:
                    mOptions.setShowLabs(isChecked);
                    break;
                case R.id.ch_show_theories:
                    mOptions.setShowTheories(isChecked);
                    break;
                case R.id.ch_all_eras:
                    mOptions.setShowAllEras(isChecked);
                    setEnable(sw_eras, !isChecked);
                    break;
                case R.id.sw_a:
                    mOptions.setEra(0, isChecked);
                    break;
                case R.id.sw_b:
                    mOptions.setEra(1, isChecked);
                    break;
                case R.id.sw_c:
                    mOptions.setEra(2, isChecked);
                    break;
                case R.id.sw_d:
                    mOptions.setEra(3, isChecked);
                    break;
                case R.id.sw_e:
                    mOptions.setEra(4, isChecked);
                    break;
                case R.id.sw_f:
                    mOptions.setEra(5, isChecked);
                    break;
                case R.id.sw_g:
                    mOptions.setEra(6, isChecked);
                    break;
                    
                default:
                    break;
                }
            }
        };
        
        chRegisteredOnly.setOnCheckedChangeListener(chListener);
        chShowLabs.setOnCheckedChangeListener(chListener);
        chShowTheories.setOnCheckedChangeListener(chListener);
        chShowAllEras.setOnCheckedChangeListener(chListener);
        sw_eras[0].setOnCheckedChangeListener(chListener);
        sw_eras[1].setOnCheckedChangeListener(chListener);
        sw_eras[2].setOnCheckedChangeListener(chListener);
        sw_eras[3].setOnCheckedChangeListener(chListener);
        sw_eras[4].setOnCheckedChangeListener(chListener);
        sw_eras[5].setOnCheckedChangeListener(chListener);
        sw_eras[6].setOnCheckedChangeListener(chListener);
        
        
        //Set values
        chRegisteredOnly.setChecked(mOptions.getShowOnlyRegistered());
        chShowLabs.setChecked(mOptions.getShowLabs());
        chShowTheories.setChecked(mOptions.getShowTheories());
        chShowAllEras.setChecked(mOptions.getShowAllEras());
        for(int i=0; i<ViewOptions.ERAS; i++)
        {
            sw_eras[i].setChecked(mOptions.getEra(i));
        }
        
        builder.setView(rootView);
        
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(mListener != null)
                {
                    mListener.onClicked(mOptions);
                }
            }
        });
        
        return builder.create();
    }
}
