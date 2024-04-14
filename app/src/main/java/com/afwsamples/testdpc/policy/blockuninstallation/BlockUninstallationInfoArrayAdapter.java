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

package com.afwsamples.testdpc.policy.blockuninstallation;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.afwsamples.testdpc.R;
import com.afwsamples.testdpc.common.ToggleComponentsArrayAdapter;
import java.util.ArrayList;
import java.util.List;

/** Displays a list of installed apps with a checkbox for disabling the uninstallation. */
public class BlockUninstallationInfoArrayAdapter extends ToggleComponentsArrayAdapter {

  private final ComponentName mAdminComponent;

  public BlockUninstallationInfoArrayAdapter(
      Context context, int resource, List<ResolveInfo> resolveInfoList, ComponentName admin) {
    super(context, resource, resolveInfoList);
    mAdminComponent = admin;
    setIsComponentEnabledList(createIsComponentEnabledList());
  }

  @Override
  public boolean isComponentEnabled(ResolveInfo resolveInfo) {
    return mDevicePolicyManager.isUninstallBlocked(mAdminComponent, resolveInfo.resolvePackageName);
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    View view = super.getView(position, convertView, parent);
    view.findViewById(R.id.enable_component_checkbox)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                boolean isBlocked = ((CheckBox) v).isChecked();
                mIsComponentCheckedList.set(position, isBlocked);
                mDevicePolicyManager.setUninstallBlocked(
                    mAdminComponent, getItem(position).resolvePackageName, isBlocked);
              }
            });
    return view;
  }

  @Override
  protected ApplicationInfo getApplicationInfo(int position) {
    try {
      return mPackageManager.getApplicationInfo(
          getItem(position).resolvePackageName, 0 /* Default flags */);
    } catch (PackageManager.NameNotFoundException e) {
      Log.e(TAG, "getApplicationInfo: ", e);
    }
    return null;
  }

  private List<Boolean> createIsComponentEnabledList() {
    List<Boolean> isComponentEnabledList = new ArrayList<>();
    int size = getCount();
    for (int i = 0; i < size; i++) {
      isComponentEnabledList.add(isComponentEnabled(getItem(i)));
    }
    return isComponentEnabledList;
  }

  @Override
  protected boolean canModifyComponent(int position) {
    // Uninstallation of all apps can be blocked.
    return true;
  }

  /** Stores whether installation is blocked for a package. */
  public class BlockUninstallationAppInfo {
    public boolean isUninstallationBlocked;
    public String pkgName;
  }

  @Override
  public CharSequence getDisplayName(int position) {
    return mPackageManager.getApplicationLabel(getApplicationInfo(position));
  }
}
