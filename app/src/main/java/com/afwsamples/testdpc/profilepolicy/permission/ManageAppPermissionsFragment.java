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

package com.afwsamples.testdpc.profilepolicy.permission;

import android.annotation.TargetApi;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.afwsamples.testdpc.DeviceAdminReceiver;
import com.afwsamples.testdpc.R;
import com.afwsamples.testdpc.common.ManageAppFragment;
import com.afwsamples.testdpc.common.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * This fragment shows all installed apps and allows viewing and editing the dangerous application
 * permissions for those apps.
 */
@TargetApi(VERSION_CODES.M)
public class ManageAppPermissionsFragment extends ManageAppFragment {
  private static final String TAG = "ManageAppPermissions";

  private DevicePolicyManager mDpm;
  private List<AppPermissionsArrayAdapter.AppPermission> mAppPermissions = new ArrayList<>();
  private TextView mAppPermissionsView;
  private ComponentName mAdminComponent;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDpm = (DevicePolicyManager) getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
    if (Util.hasDelegation(getActivity(), DevicePolicyManager.DELEGATION_PERMISSION_GRANT)) {
      mAdminComponent = null;
    } else {
      mAdminComponent = DeviceAdminReceiver.getComponentName(getContext());
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
    View view = super.onCreateView(layoutInflater, container, savedInstanceState);
    view.findViewById(R.id.load_default_button).setVisibility(View.GONE);
    view.findViewById(R.id.add_new_row).setVisibility(View.GONE);
    view.findViewById(R.id.manage_app_button_container).setVisibility(View.GONE);
    mAppPermissionsView = view.findViewById(R.id.error_message);
    mAppPermissionsView.setText(R.string.app_permissions_empty);
    return view;
  }

  @Override
  protected void onSpinnerItemSelected(ApplicationInfo appInfo) {
    String pkgName = appInfo.packageName;
    if (!TextUtils.isEmpty(pkgName)) {
      List<String> permissions = new ArrayList<String>();

      PackageInfo info = null;
      try {
        info = mPackageManager.getPackageInfo(pkgName, PackageManager.GET_PERMISSIONS);
      } catch (PackageManager.NameNotFoundException e) {
        Log.e(TAG, "Could not retrieve info about the package: " + pkgName, e);
        return;
      }

      if (info != null && info.requestedPermissions != null) {
        for (String requestedPerm : info.requestedPermissions) {
          try {
            PermissionInfo pInfo = mPackageManager.getPermissionInfo(requestedPerm, 0);
            if (pInfo != null) {
              if ((pInfo.protectionLevel & PermissionInfo.PROTECTION_MASK_BASE)
                  == PermissionInfo.PROTECTION_DANGEROUS) {
                permissions.add(pInfo.name);
              }
            }
          } catch (PackageManager.NameNotFoundException e) {
            Log.i(TAG, "Could not retrieve info about the permission: " + requestedPerm);
          }
        }
      }

      List<AppPermissionsArrayAdapter.AppPermission> populatedPermissions = new ArrayList<>();
      for (String permission : permissions) {
        int permissionState = mDpm.getPermissionGrantState(mAdminComponent, pkgName, permission);
        AppPermissionsArrayAdapter.AppPermission populatedPerm =
            new AppPermissionsArrayAdapter.AppPermission(pkgName, permission, permissionState);
        populatedPermissions.add(populatedPerm);
      }
      displayAppPermissions(populatedPermissions);
    }
  }

  @Override
  protected void resetConfig() {}

  @Override
  protected void saveConfig() {}

  @Override
  protected void addNewRow() {}

  @Override
  protected void loadDefault() {}

  @Override
  protected BaseAdapter createListAdapter() {
    return new AppPermissionsArrayAdapter(getActivity(), 0, mAppPermissions, mAdminComponent);
  }

  /**
   * Displays the list of permissions for the selected app. If there are no permissions to be
   * displayed, a text view displays a message about this.
   */
  private void displayAppPermissions(List<AppPermissionsArrayAdapter.AppPermission> permissions) {
    mAppPermissions.clear();
    if (permissions.isEmpty()) {
      mAppPermissionsView.setVisibility(View.VISIBLE);
    } else {
      mAppPermissionsView.setVisibility(View.GONE);
      mAppPermissions.addAll(permissions);
    }
    mAdapter.notifyDataSetChanged();
  }
}
