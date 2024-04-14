/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.afwsamples.testdpc.policy.systemupdatepolicy;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.admin.DevicePolicyManager;
import android.app.admin.FreezePeriod;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.afwsamples.testdpc.DeviceAdminReceiver;
import com.afwsamples.testdpc.R;
import com.afwsamples.testdpc.common.Util;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This fragment provides functionalities related to managed system update that are available in a
 * device owner. These includes 1) {@link DevicePolicyManager#setSystemUpdatePolicy} 2) {@link
 * DevicePolicyManager#getSystemUpdatePolicy} 3) {@link SystemUpdatePolicy}
 */
@TargetApi(VERSION_CODES.M)
public class SystemUpdatePolicyFragment extends Fragment
    implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

  @RequiresApi(api = VERSION_CODES.O)
  @SuppressLint("JavaTimeDefaultTimeZone")
  static class Period {
    MonthDay mStart;
    MonthDay mEnd;

    public Period() {}

    public Period(MonthDay start, MonthDay end) {
      mStart = start;
      mEnd = end;
    }

    public void set(LocalDate startDate, LocalDate endDate) {
      mStart = MonthDay.of(startDate.getMonth(), startDate.getDayOfMonth());
      mEnd = MonthDay.of(endDate.getMonth(), endDate.getDayOfMonth());
    }

    @Override
    @SuppressWarnings("SimpleDateFormat")
    public String toString() {
      SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");
      StringBuffer stringBuffer = new StringBuffer();
      formatter
          .format(
              new Date(0, mStart.getMonthValue() - 1, mStart.getDayOfMonth()),
              stringBuffer,
              new FieldPosition(0))
          .append(" - ");
      return formatter
          .format(
              new Date(0, mEnd.getMonthValue() - 1, mEnd.getDayOfMonth()),
              stringBuffer,
              new FieldPosition(0))
          .toString();
    }

    public LocalDate getStartDate() {
      return mStart.atYear(LocalDate.now().getYear());
    }

    public LocalDate getEndDate() {
      return mEnd.atYear(LocalDate.now().getYear());
    }

    @TargetApi(VERSION_CODES.P)
    public FreezePeriod toFreezePeriod() {
      return new FreezePeriod(mStart, mEnd);
    }
  }

  private EditText mCurrentSystemUpdatePolicy;
  private RadioGroup mSystemUpdatePolicySelection;
  private LinearLayout mMaintenanceWindowDetails;
  private Button mSetMaintenanceWindowStart;
  private Button mSetMaintenanceWindowEnd;
  private LinearLayout mFreezePeriodPanel;
  private ListView mFreezePeriodList;

  private DevicePolicyManager mDpm;
  private int mMaintenanceStart;
  private int mMaintenanceEnd;
  private ArrayList<Period> mFreezePeriods = new ArrayList<>();
  private FreezePeriodAdapter mFreezePeriodAdapter;

  @Override
  public void onResume() {
    super.onResume();
    getActivity().getActionBar().setTitle(R.string.system_update_policy);
    reloadSystemUpdatePolicy();
  }

  class FreezePeriodAdapter extends ArrayAdapter<Period> {
    public ArrayList<Period> mData;

    public FreezePeriodAdapter(Context context, ArrayList<Period> periods) {
      super(context, 0, periods);
      this.mData = periods;
    }

    @RequiresApi(api = VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      Period currentPeriod = getItem(position);
      if (convertView == null) {
        convertView =
            LayoutInflater.from(getContext()).inflate(R.layout.freeze_period_row, parent, false);
      }
      Button textView = convertView.findViewById(R.id.string_period);
      textView.setText(currentPeriod.toString());
      textView.setTag(mData.get(position));
      textView.setOnClickListener(
          view -> {
            final Period period = (Period) view.getTag();
            promptToSetFreezePeriod(
                (LocalDate startDate, LocalDate endDate) -> {
                  period.set(startDate, endDate);
                  mFreezePeriodAdapter.notifyDataSetChanged();
                },
                period.getStartDate(),
                period.getEndDate());
          });
      View deleteButton = convertView.findViewById(R.id.delete_period);
      deleteButton.setTag(mData.get(position));
      deleteButton.setOnClickListener(
          view -> {
            Period period = (Period) view.getTag();
            mData.remove(period);
            FreezePeriodAdapter.this.notifyDataSetChanged();
          });
      return convertView;
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDpm = (DevicePolicyManager) getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
  }

  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
    View view = layoutInflater.inflate(R.layout.system_update_policy, container, false);

    mCurrentSystemUpdatePolicy = view.findViewById(R.id.system_update_policy_current);
    mSystemUpdatePolicySelection = view.findViewById(R.id.system_update_policy_selection);
    mMaintenanceWindowDetails = view.findViewById(R.id.system_update_policy_windowed_details);
    mSetMaintenanceWindowStart = view.findViewById(R.id.system_update_policy_window_start);
    mSetMaintenanceWindowEnd = view.findViewById(R.id.system_update_policy_window_end);
    mFreezePeriodPanel = view.findViewById(R.id.system_update_policy_blackout_periods);
    mFreezePeriodList = view.findViewById(R.id.system_update_policy_blackout_period_list);

    mFreezePeriodAdapter = new FreezePeriodAdapter(getContext(), mFreezePeriods);
    mFreezePeriodList.setAdapter(mFreezePeriodAdapter);

    mSetMaintenanceWindowStart.setOnClickListener(this);
    mSetMaintenanceWindowEnd.setOnClickListener(this);
    view.findViewById(R.id.system_update_policy_set).setOnClickListener(this);
    view.findViewById(R.id.system_update_policy_btn_add_period).setOnClickListener(this);

    mSystemUpdatePolicySelection.setOnCheckedChangeListener(this);

    mFreezePeriodPanel.setVisibility(Util.SDK_INT >= VERSION_CODES.P ? View.VISIBLE : View.GONE);
    return view;
  }

  private void selectTime(final boolean isWindowStart) {
    int defaultMinutes = isWindowStart ? mMaintenanceStart : mMaintenanceEnd;
    TimePickerDialog timePicker =
        new TimePickerDialog(
            getActivity(),
            (picker, hour, minutes) -> {
              if (isWindowStart) {
                mMaintenanceStart = hour * 60 + minutes;
              } else {
                mMaintenanceEnd = hour * 60 + minutes;
              }
              updateMaintenanceWindowDisplay();
            },
            defaultMinutes / 60,
            defaultMinutes % 60,
            true);
    timePicker.show();
  }

  @RequiresApi(api = VERSION_CODES.O)
  @Override
  @SuppressLint("JavaTimeDefaultTimeZone")
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.system_update_policy_window_start) {
      selectTime(true);
    } else if (id == R.id.system_update_policy_window_end) {
      selectTime(false);
    } else if (id == R.id.system_update_policy_set) {
      if (setSystemUpdatePolicy()) {
        reloadSystemUpdatePolicy();
      }
    } else if (id == R.id.system_update_policy_btn_add_period) {
      promptToSetFreezePeriod(
          (LocalDate startDate, LocalDate endDate) -> {
            Period period = new Period();
            period.set(startDate, endDate);
            mFreezePeriods.add(period);
            mFreezePeriodAdapter.notifyDataSetChanged();
          },
          LocalDate.now(),
          LocalDate.now());
    }
  }

  interface FreezePeriodPickResult {
    void onResult(LocalDate startDate, LocalDate endDate);
  }

  interface DatePickResult {
    void onResult(LocalDate pickedDate);
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void showDatePicker(LocalDate hint, int titleResId, DatePickResult resultCallback) {
    DatePickerDialog picker =
        new DatePickerDialog(
            getActivity(),
            (pickerObj, year, month, day) -> {
              final LocalDate pickedDate = LocalDate.of(year, month + 1, day);
              resultCallback.onResult(pickedDate);
            },
            hint.getYear(),
            hint.getMonth().getValue() - 1,
            hint.getDayOfMonth());
    picker.setTitle(getString(titleResId));
    picker.show();
  }

  @RequiresApi(api = VERSION_CODES.O)
  private void promptToSetFreezePeriod(
      FreezePeriodPickResult callback, final LocalDate startDate, final LocalDate endDate) {
    showDatePicker(
        startDate,
        R.string.system_update_policy_pick_start_free_period_title,
        pickedStartDate -> {
          LocalDate proposedEndDate = endDate;
          if (proposedEndDate.compareTo(pickedStartDate) < 0) {
            proposedEndDate = pickedStartDate;
          }
          showDatePicker(
              proposedEndDate,
              R.string.system_update_policy_pick_end_free_period_title,
              pickedEndDate -> callback.onResult(pickedStartDate, pickedEndDate));
        });
  }

  @TargetApi(VERSION_CODES.P)
  private boolean setSystemUpdatePolicy() {
    SystemUpdatePolicy newPolicy;
    int checkedRadioButtonId = mSystemUpdatePolicySelection.getCheckedRadioButtonId();
    if (checkedRadioButtonId == R.id.system_update_policy_automatic) {
      newPolicy = SystemUpdatePolicy.createAutomaticInstallPolicy();
    } else if (checkedRadioButtonId == R.id.system_update_policy_Windowed) {
      newPolicy =
          SystemUpdatePolicy.createWindowedInstallPolicy(mMaintenanceStart, mMaintenanceEnd);
    } else if (checkedRadioButtonId == R.id.system_update_policy_postpone) {
      newPolicy = SystemUpdatePolicy.createPostponeInstallPolicy();
    } else {
      newPolicy = null;
    }

    try {
      if (Util.SDK_INT >= VERSION_CODES.P && newPolicy != null && mFreezePeriods.size() != 0) {
        final List<FreezePeriod> periods = new ArrayList<>(mFreezePeriods.size());
        for (Period p : mFreezePeriods) {
          periods.add(p.toFreezePeriod());
        }
        newPolicy.setFreezePeriods(periods);
      }
      mDpm.setSystemUpdatePolicy(DeviceAdminReceiver.getComponentName(getActivity()), newPolicy);
      Toast.makeText(getContext(), "Policy set successfully", Toast.LENGTH_LONG).show();
      return true;
    } catch (IllegalArgumentException e) {
      Toast.makeText(
              getContext(),
              "Failed to set system update policy: " + e.getMessage(),
              Toast.LENGTH_LONG)
          .show();
    }
    return false;
  }

  private String formatMinutes(int minutes) {
    return String.format(Locale.getDefault(), "%02d:%02d", minutes / 60, minutes % 60);
  }

  private void updateMaintenanceWindowDisplay() {
    mSetMaintenanceWindowStart.setText(formatMinutes(mMaintenanceStart));
    mSetMaintenanceWindowEnd.setText(formatMinutes(mMaintenanceEnd));
  }

  @TargetApi(VERSION_CODES.P)
  private void reloadSystemUpdatePolicy() {
    SystemUpdatePolicy policy = mDpm.getSystemUpdatePolicy();
    String policyDescription = "Unknown";

    if (policy == null) {
      policyDescription = "None";
      mSystemUpdatePolicySelection.check(R.id.system_update_policy_none);
      mMaintenanceWindowDetails.setVisibility(View.INVISIBLE);
      mFreezePeriodPanel.setVisibility(View.GONE);
    } else {
      switch (policy.getPolicyType()) {
        case SystemUpdatePolicy.TYPE_INSTALL_AUTOMATIC:
          policyDescription = "Automatic";
          mSystemUpdatePolicySelection.check(R.id.system_update_policy_automatic);
          mMaintenanceWindowDetails.setVisibility(View.INVISIBLE);
          break;
        case SystemUpdatePolicy.TYPE_INSTALL_WINDOWED:
          {
            mMaintenanceStart = policy.getInstallWindowStart();
            mMaintenanceEnd = policy.getInstallWindowEnd();
            policyDescription =
                String.format(
                    Locale.getDefault(),
                    "Windowed: %s-%s",
                    formatMinutes(mMaintenanceStart),
                    formatMinutes(mMaintenanceEnd));
            updateMaintenanceWindowDisplay();

            mSystemUpdatePolicySelection.check(R.id.system_update_policy_Windowed);
            mMaintenanceWindowDetails.setVisibility(View.VISIBLE);
            break;
          }
        case SystemUpdatePolicy.TYPE_POSTPONE:
          policyDescription = "Postpone";
          mSystemUpdatePolicySelection.check(R.id.system_update_policy_postpone);
          mMaintenanceWindowDetails.setVisibility(View.INVISIBLE);
          break;
      }
      if (Util.SDK_INT >= VERSION_CODES.P) {
        List<FreezePeriod> freezePeriods = policy.getFreezePeriods();
        mFreezePeriods.clear();
        for (FreezePeriod period : freezePeriods) {
          Period p = new Period(period.getStart(), period.getEnd());
          mFreezePeriods.add(p);
        }
        mFreezePeriodAdapter.notifyDataSetChanged();
        mFreezePeriodPanel.setVisibility(View.VISIBLE);
      }
    }
    mCurrentSystemUpdatePolicy.setText(policyDescription);
  }

  @Override
  public void onCheckedChanged(RadioGroup view, int checkedId) {
    if (checkedId == R.id.system_update_policy_Windowed) {
      updateMaintenanceWindowDisplay();
      mMaintenanceWindowDetails.setVisibility(View.VISIBLE);
    } else {
      mMaintenanceWindowDetails.setVisibility(View.INVISIBLE);
    }
    if (checkedId == R.id.system_update_policy_none || Util.SDK_INT < VERSION_CODES.P) {
      mFreezePeriodPanel.setVisibility(View.GONE);
    } else {
      mFreezePeriodPanel.setVisibility(View.VISIBLE);
    }
  }
}
