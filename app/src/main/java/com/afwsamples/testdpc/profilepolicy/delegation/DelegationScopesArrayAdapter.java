package com.afwsamples.testdpc.profilepolicy.delegation;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import com.afwsamples.testdpc.R;
import java.util.List;

/** ArrayAdapter to assist on rendering the delegation scopes granted to an app. */
class DelegationScopesArrayAdapter extends ArrayAdapter<DelegationFragment.DelegationScope> {

  // Replace this with DevicePolicyManager.DELEGATION_SECURITY_LOGGING when it is a
  // part of the SDK.
  private static final String DELEGATION_SECURITY_LOGGING = "delegation-security-logging";

  public DelegationScopesArrayAdapter(
      Context context, int res, List<DelegationFragment.DelegationScope> objects) {
    super(context, res, objects);
  }

  @NonNull
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    CheckBox viewHolder;
    if (convertView == null || !(convertView.getTag() instanceof CheckBox)) {
      convertView =
          LayoutInflater.from(getContext()).inflate(R.layout.delegation_scope_row, parent, false);
      viewHolder = (CheckBox) convertView.findViewById(R.id.checkbox_delegation_scope);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (CheckBox) convertView.getTag();
    }

    DelegationFragment.DelegationScope delegationScope = getItem(position);
    viewHolder.setChecked(delegationScope.granted);
    switch (delegationScope.scope) {
      case DevicePolicyManager.DELEGATION_CERT_INSTALL:
        viewHolder.setText(R.string.delegation_scope_cert_install);
        break;
      case DevicePolicyManager.DELEGATION_APP_RESTRICTIONS:
        viewHolder.setText(R.string.delegation_scope_app_restrictions);
        break;
      case DevicePolicyManager.DELEGATION_BLOCK_UNINSTALL:
        viewHolder.setText(R.string.delegation_scope_block_uninstall);
        break;
      case DevicePolicyManager.DELEGATION_PERMISSION_GRANT:
        viewHolder.setText(R.string.delegation_scope_permission_grant);
        break;
      case DevicePolicyManager.DELEGATION_PACKAGE_ACCESS:
        viewHolder.setText(R.string.delegation_scope_package_access);
        break;
      case DevicePolicyManager.DELEGATION_ENABLE_SYSTEM_APP:
        viewHolder.setText(R.string.delegation_scope_enable_system_app);
        break;
      case DevicePolicyManager.DELEGATION_NETWORK_LOGGING:
        viewHolder.setText(R.string.delegation_scope_network_logging);
        break;
      case DELEGATION_SECURITY_LOGGING:
        viewHolder.setText(R.string.delegation_scope_security_logging);
        break;
      case DevicePolicyManager.DELEGATION_CERT_SELECTION:
        viewHolder.setText(R.string.delegation_scope_cert_selection);
        break;
    }

    return convertView;
  }
}
