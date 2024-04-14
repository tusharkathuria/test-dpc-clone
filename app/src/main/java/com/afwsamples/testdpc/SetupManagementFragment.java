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

package com.afwsamples.testdpc;

import static android.app.admin.DevicePolicyManager.ACTION_PROVISION_MANAGED_DEVICE;
import static android.app.admin.DevicePolicyManager.ACTION_PROVISION_MANAGED_PROFILE;
import static android.app.admin.DevicePolicyManager.EXTRA_PROVISIONING_ACCOUNT_TO_MIGRATE;
import static android.app.admin.DevicePolicyManager.EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE;
import static android.app.admin.DevicePolicyManager.EXTRA_PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME;
import static android.app.admin.DevicePolicyManager.EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME;
import static android.app.admin.DevicePolicyManager.EXTRA_PROVISIONING_LOGO_URI;
import static android.app.admin.DevicePolicyManager.EXTRA_PROVISIONING_MAIN_COLOR;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.admin.DevicePolicyManager;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.afwsamples.testdpc.common.ColorPicker;
import com.afwsamples.testdpc.common.LaunchIntentUtil;
import com.afwsamples.testdpc.common.ProvisioningStateUtil;
import com.afwsamples.testdpc.common.Util;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Set;

/**
 * This {@link Fragment} shows the UI that allows the user to start the setup of a managed profile
 * or configuration of a device-owner if the device is in an appropriate state.
 */
public class SetupManagementFragment extends Fragment
    implements View.OnClickListener,
        ColorPicker.OnColorSelectListener,
        RadioGroup.OnCheckedChangeListener {
  // Tag for creating this fragment. This tag can be used to retrieve this fragment.
  public static final String FRAGMENT_TAG = "SetupManagementFragment";

  private static final int REQUEST_PROVISION_MANAGED_PROFILE = 1;
  private static final int REQUEST_PROVISION_DEVICE_OWNER = 2;
  private static final int REQUEST_GET_LOGO = 3;

  private static final int NO_COLOR_SPECIFIED = -1;

  private TextView mSetupManagementMessage;
  private RadioGroup mSetupOptions;
  private FooterButton mNavigationNextButton;
  private CheckBox mSkipUserConsent;
  private CheckBox mSkipEncryption;
  private CheckBox mKeepAccountMigrated;
  private ImageButton mParamsIndicator;
  private View mParamsView;
  private static final int[] STATE_EXPANDED = new int[] {R.attr.state_expanded};
  private static final int[] STATE_COLLAPSED = new int[] {-R.attr.state_expanded};

  private ImageView mColorPreviewView;
  private TextView mColorValue;
  private ImageView mLogoPreviewView;
  private TextView mLogoValue;

  private int mCurrentColor = NO_COLOR_SPECIFIED;
  private Uri mLogoUri = null;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      mLogoUri = savedInstanceState.getParcelable(EXTRA_PROVISIONING_LOGO_URI);
      mCurrentColor = savedInstanceState.getInt(EXTRA_PROVISIONING_MAIN_COLOR);
    }

    View view = inflater.inflate(R.layout.setup_management_fragment, container, false);
    GlifLayout layout = view.findViewById(R.id.setup_wizard_layout);

    FooterBarMixin mixin = layout.getMixin(FooterBarMixin.class);

    Context context = getActivity();

    FooterButton nextButton =
        new FooterButton.Builder(context)
            .setText(R.string.next_button_label)
            .setListener(this::onNavigateNext)
            .setButtonType(FooterButton.ButtonType.NEXT)
//            .setTheme(R.style.SudGlifButton_Primary)
            .build();
    FooterButton exitButton =
        new FooterButton.Builder(context)
            .setText(R.string.exit)
            .setListener(this::onNavigateBack)
            .setButtonType(FooterButton.ButtonType.OTHER)
//            .setTheme(R.style.SudGlifButtonBar_Stackable)
            .build();
    mixin.setPrimaryButton(nextButton);
    mixin.setSecondaryButton(exitButton);

    mNavigationNextButton = nextButton;

    mSetupManagementMessage = (TextView) view.findViewById(R.id.setup_management_message_id);
    mSetupOptions = (RadioGroup) view.findViewById(R.id.setup_options);
    mSetupOptions.setOnCheckedChangeListener(this);
    mSkipUserConsent = (CheckBox) view.findViewById(R.id.skip_user_consent);
    mKeepAccountMigrated = (CheckBox) view.findViewById(R.id.keep_account_migrated);
    mSkipEncryption = (CheckBox) view.findViewById(R.id.skip_encryption);

    mParamsView = view.findViewById(R.id.params);
    mParamsIndicator = (ImageButton) view.findViewById(R.id.params_indicator);
    mParamsIndicator.setOnClickListener(this);

    view.findViewById(R.id.color_select_button).setOnClickListener(this);
    mColorValue = (TextView) view.findViewById(R.id.selected_color_value);
    mColorPreviewView = (ImageView) view.findViewById(R.id.preview_color);

    updateColorUi();

    Intent launchIntent = getActivity().getIntent();
    if (LaunchIntentUtil.isSynchronousAuthLaunch(launchIntent)) {
      Account addedAccount = LaunchIntentUtil.getAddedAccount(launchIntent);
      if (addedAccount != null) {
        view.findViewById(R.id.managed_account_desc).setVisibility(View.VISIBLE);
        // Show the user which account needs management.
        TextView managedAccountName = (TextView) view.findViewById(R.id.managed_account_name);
        managedAccountName.setVisibility(View.VISIBLE);
        managedAccountName.setText(addedAccount.name);
      } else {
        // This is not an expected case, sync-auth is triggered by an account being added so
        // we expect to be told which account to migrate in the launch intent. We don't
        // finish() here as it's still technically feasible to continue.
        Toast.makeText(getActivity(), R.string.invalid_launch_intent_no_account, Toast.LENGTH_LONG)
            .show();
      }
    }
    return view;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putParcelable(EXTRA_PROVISIONING_LOGO_URI, mLogoUri);
    outState.putInt(EXTRA_PROVISIONING_MAIN_COLOR, mCurrentColor);
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onResume() {
    super.onResume();

    if (setProvisioningMethodsVisibility()) {
      // The extra logo uri and color are supported only from N
      if (Util.SDK_INT >= VERSION_CODES.N) {
        getView().findViewById(R.id.params_title).setVisibility(View.VISIBLE);
        if (canAnAppHandleGetContent()) {
          getView().findViewById(R.id.choose_logo_item_layout).setVisibility(View.VISIBLE);
          getView().findViewById(R.id.logo_select_button).setOnClickListener(this);
          mLogoValue = (TextView) getView().findViewById(R.id.selected_logo_value);
          mLogoPreviewView = (ImageView) getView().findViewById(R.id.preview_logo);
        }
        setProvisioningModeSpecificUI();
      }
    } else {
      showNoProvisioningPossibleUI();
    }
  }

  /** On R.id.setup_options are changed */
  @Override
  public void onCheckedChanged(RadioGroup radioGroup, int i) {
    setProvisioningModeSpecificUI();
  }

  private void setProvisioningModeSpecificUI() {
    final int setUpOptionId = mSetupOptions.getCheckedRadioButtonId();
    final boolean isManagedProfileAction = setUpOptionId == R.id.setup_managed_profile;
    final boolean isManagedDeviceAction = setUpOptionId == R.id.setup_device_owner;
    mSkipUserConsent.setVisibility(
        Util.SDK_INT >= VERSION_CODES.O
                && isManagedProfileAction
                && Util.isDeviceOwner(getActivity())
            ? View.VISIBLE
            : View.GONE);
    mKeepAccountMigrated.setVisibility(
        Util.SDK_INT >= VERSION_CODES.O && isManagedProfileAction ? View.VISIBLE : View.GONE);
    mSkipEncryption.setVisibility(
        (isManagedProfileAction && Util.SDK_INT >= VERSION_CODES.N)
                || (isManagedDeviceAction && Util.SDK_INT >= VERSION_CODES.M)
            ? View.VISIBLE
            : View.GONE);

    // If TestDpc is already a device owner, but can create a managed profile, show a different
    // message.
    if (Util.isDeviceOwner(getActivity())) {
      mSetupManagementMessage.setText(R.string.setup_management_message_for_do);
    }
  }

  private void maybeLaunchProvisioning(String intentAction, int requestCode) {
    Activity activity = getActivity();

    Intent intent = new Intent(intentAction);
    if (Util.SDK_INT >= VERSION_CODES.M) {
      intent.putExtra(
          EXTRA_PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME,
          DeviceAdminReceiver.getReceiverComponentName(getActivity()));
    } else {
      intent.putExtra(EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME, getActivity().getPackageName());
    }

    if (!maybeSpecifyNExtras(intent)) {
      // Unable to handle user-input - can't continue.
      return;
    }
    PersistableBundle adminExtras = new PersistableBundle();
    maybeSpecifySyncAuthExtras(intent, adminExtras);
    maybePassAffiliationIds(intent, adminExtras);
    specifySkipUserConsent(intent);
    specifyKeepAccountMigrated(intent);
    specifySkipEncryption(intent);
    specifyDefaultDisclaimers(intent);

    // allow offline provisioning
    if (Util.SDK_INT >= VERSION_CODES.TIRAMISU) {
      intent.putExtra(DevicePolicyManager.EXTRA_PROVISIONING_ALLOW_OFFLINE, true);
    }

    if (adminExtras.size() > 0) {
      intent.putExtra(EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE, adminExtras);
    }
    if (intent.resolveActivity(activity.getPackageManager()) != null) {
      startActivityForResult(intent, requestCode);
    } else {
      Toast.makeText(activity, R.string.provisioning_not_supported, Toast.LENGTH_SHORT).show();
    }
  }

  private void maybeSpecifySyncAuthExtras(Intent intent, PersistableBundle adminExtras) {
    Activity activity = getActivity();
    Intent launchIntent = activity.getIntent();

    if (!LaunchIntentUtil.isSynchronousAuthLaunch(launchIntent)) {
      // Don't do anything if this isn't a sync-auth flow.
      return;
    }

    Account accountToMigrate = LaunchIntentUtil.getAddedAccount(launchIntent);
    if (accountToMigrate != null) {
      // EXTRA_PROVISIONING_ACCOUNT_TO_MIGRATE only supported in API 22+.
      if (Util.SDK_INT >= VERSION_CODES.LOLLIPOP_MR1) {
        // Configure the account to migrate into the managed profile if setup
        // completes.
        intent.putExtra(EXTRA_PROVISIONING_ACCOUNT_TO_MIGRATE, accountToMigrate);
      } else {
        Toast.makeText(activity, R.string.migration_not_supported, Toast.LENGTH_SHORT).show();
      }
    }

    // Perculate launch intent extras through to DeviceAdminReceiver so they can be used there.
    LaunchIntentUtil.prepareDeviceAdminExtras(launchIntent, adminExtras);
  }

  private void maybePassAffiliationIds(Intent intent, PersistableBundle adminExtras) {
    if (Util.isDeviceOwner(getActivity())
        && ACTION_PROVISION_MANAGED_PROFILE.equals(intent.getAction())
        && Util.SDK_INT >= VERSION_CODES.O) {
      passAffiliationIds(intent, adminExtras);
    }
  }

  @TargetApi(VERSION_CODES.O)
  private void passAffiliationIds(Intent intent, PersistableBundle adminExtras) {
    DevicePolicyManagerGateway dpmg = new DevicePolicyManagerGatewayImpl(getActivity());
    Set<String> ids = dpmg.getAffiliationIds();
    String affiliationId = null;
    if (ids.size() == 0) {
      SecureRandom randomGenerator = new SecureRandom();
      affiliationId = Integer.toString(randomGenerator.nextInt(1000000));
      dpmg.setAffiliationIds(Collections.singleton(affiliationId));
    } else {
      affiliationId = ids.iterator().next();
    }
    adminExtras.putString(LaunchIntentUtil.EXTRA_AFFILIATION_ID, affiliationId);
  }

  /** @return true if we can launch the intent */
  private boolean maybeSpecifyNExtras(Intent intent) {
    if (Util.SDK_INT >= VERSION_CODES.N) {
      specifyLogoUri(intent);
      specifyColor(intent);
    }
    return true;
  }

  private void specifyDefaultDisclaimers(Intent intent) {
    if (Util.SDK_INT >= VERSION_CODES.O) {
      Bundle emmBundle = new Bundle();
      emmBundle.putString(
          DevicePolicyManager.EXTRA_PROVISIONING_DISCLAIMER_HEADER,
          getString(R.string.default_disclaimer_emm_name));
      emmBundle.putParcelable(
          DevicePolicyManager.EXTRA_PROVISIONING_DISCLAIMER_CONTENT,
          resourceToUri(getActivity(), R.raw.emm_disclaimer));
      Bundle companyBundle = new Bundle();
      companyBundle.putString(
          DevicePolicyManager.EXTRA_PROVISIONING_DISCLAIMER_HEADER,
          getString(R.string.default_disclaimer_company_name));
      companyBundle.putParcelable(
          DevicePolicyManager.EXTRA_PROVISIONING_DISCLAIMER_CONTENT,
          resourceToUri(getActivity(), R.raw.company_disclaimer));
      intent.putExtra(
          DevicePolicyManager.EXTRA_PROVISIONING_DISCLAIMERS,
          new Bundle[] {emmBundle, companyBundle});
    }
  }

  private static Uri resourceToUri(Context context, int resID) {
    return Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE
            + "://"
            + context.getResources().getResourcePackageName(resID)
            + '/'
            + context.getResources().getResourceTypeName(resID)
            + '/'
            + context.getResources().getResourceEntryName(resID));
  }

  private void specifySkipUserConsent(Intent intent) {
    if (Util.SDK_INT >= VERSION_CODES.O
        && ACTION_PROVISION_MANAGED_PROFILE.equals(intent.getAction())
        && mSkipUserConsent.getVisibility() == View.VISIBLE) {
      intent.putExtra(
          DevicePolicyManager.EXTRA_PROVISIONING_SKIP_USER_CONSENT, mSkipUserConsent.isChecked());
    }
  }

  private void specifyKeepAccountMigrated(Intent intent) {
    if (Util.SDK_INT >= VERSION_CODES.O
        && ACTION_PROVISION_MANAGED_PROFILE.equals(intent.getAction())
        && mKeepAccountMigrated.getVisibility() == View.VISIBLE) {
      intent.putExtra(
          DevicePolicyManager.EXTRA_PROVISIONING_KEEP_ACCOUNT_ON_MIGRATION,
          mKeepAccountMigrated.isChecked());
    }
  }

  private void specifySkipEncryption(Intent intent) {
    if (mSkipEncryption.getVisibility() == View.VISIBLE) {
      intent.putExtra(
          DevicePolicyManager.EXTRA_PROVISIONING_SKIP_ENCRYPTION, mSkipEncryption.isChecked());
    }
  }

  private void specifyLogoUri(Intent intent) {
    if (mLogoUri == null) {
      return;
    }
    intent.putExtra(EXTRA_PROVISIONING_LOGO_URI, mLogoUri);
    if (mLogoUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
      intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      intent.setClipData(ClipData.newUri(getActivity().getContentResolver(), "", mLogoUri));
    }
  }

  private void specifyColor(Intent intent) {
    if (mCurrentColor == NO_COLOR_SPECIFIED) {
      return;
    }
    intent.putExtra(EXTRA_PROVISIONING_MAIN_COLOR, mCurrentColor);
  }

  private void showNoProvisioningPossibleUI() {
    mNavigationNextButton.setVisibility(View.GONE);
    TextView textView = (TextView) getView().findViewById(R.id.setup_management_message_id);
    textView.setText(
        Util.isDeviceOwner(getActivity())
            ? R.string.provisioning_not_possible_for_do
            : R.string.provisioning_not_possible);
  }

  /**
   * Set visibility of all provisioning methods
   *
   * @return false if none of the provisioning method is visible
   */
  private boolean setProvisioningMethodsVisibility() {
    boolean hasProvisioningOption = false;
    hasProvisioningOption |=
        setVisibility(ACTION_PROVISION_MANAGED_PROFILE, R.id.setup_managed_profile);
    hasProvisioningOption |=
        setVisibility(ACTION_PROVISION_MANAGED_DEVICE, R.id.setup_device_owner);
    return hasProvisioningOption;
  }

  private boolean setVisibility(String action, int radioButtonId) {
    final int visibility =
        ProvisioningStateUtil.isProvisioningAllowed(getActivity(), action)
            ? View.VISIBLE
            : View.GONE;
    getView().findViewById(radioButtonId).setVisibility(visibility);
    return visibility == View.VISIBLE;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Activity activity = getActivity();

    switch (requestCode) {
      case REQUEST_PROVISION_MANAGED_PROFILE:
      case REQUEST_PROVISION_DEVICE_OWNER:
        if (resultCode == Activity.RESULT_OK) {
          // Success, finish the enclosing activity. NOTE: Only finish once we're done
          // here, as in synchronous auth cases we don't want the user to return to the
          // Android setup wizard or add-account flow prematurely.
          activity.setResult(Activity.RESULT_OK);
          activity.finish();
        } else {
          // Something went wrong (either provisioning failed, or the user backed out).
          // Let the user decide how to proceed.
          Toast.makeText(activity, R.string.provisioning_failed_or_cancelled, Toast.LENGTH_SHORT)
              .show();
        }
        break;
      case REQUEST_GET_LOGO:
        if (data != null && data.getData() != null) {
          mLogoUri = data.getData();
          mLogoValue.setText(mLogoUri.getLastPathSegment());
          Util.updateImageView(getActivity(), mLogoPreviewView, mLogoUri);
        }
        break;
    }
  }

  @Override
  public void onClick(View view) {
    int id = view.getId();
    if (id == R.id.params_indicator) {
      if (mParamsView.getVisibility() == View.VISIBLE) {
        mParamsView.setVisibility(View.GONE);
        mParamsIndicator.setImageState(STATE_COLLAPSED, false);
      } else {
        mParamsIndicator.setImageState(STATE_EXPANDED, false);
        mParamsView.setVisibility(View.VISIBLE);
      }
    } else if (id == R.id.color_select_button) {
      ColorPicker.newInstance(mCurrentColor, FRAGMENT_TAG, "provisioningColor")
          .show(getFragmentManager(), "colorPicker");
    } else if (id == R.id.logo_select_button) {
      startActivityForResult(getGetContentIntent(), REQUEST_GET_LOGO);
    }
  }

  @Override
  public void onColorSelected(int colorValue, String id) {
    mCurrentColor = colorValue;
    updateColorUi();
  }

  public void onNavigateBack(View view) {
    getActivity().onBackPressed();
  }

  public void onNavigateNext(View view) {
    if (mSetupOptions.getCheckedRadioButtonId() == R.id.setup_managed_profile) {
      maybeLaunchProvisioning(ACTION_PROVISION_MANAGED_PROFILE, REQUEST_PROVISION_MANAGED_PROFILE);
    } else {
      maybeLaunchProvisioning(ACTION_PROVISION_MANAGED_DEVICE, REQUEST_PROVISION_DEVICE_OWNER);
    }
  }

  private Intent getGetContentIntent() {
    Intent getContent = new Intent(Intent.ACTION_GET_CONTENT);
    getContent.setType("image/*");
    return getContent;
  }

  private boolean canAnAppHandleGetContent() {
    return getGetContentIntent().resolveActivity(getActivity().getPackageManager()) != null;
  }

  private void updateColorUi() {
    if (mCurrentColor != NO_COLOR_SPECIFIED) {
      mColorValue.setText(String.format(ColorPicker.COLOR_STRING_FORMATTER, mCurrentColor));
      mColorPreviewView.setImageTintList(ColorStateList.valueOf(mCurrentColor));
      mColorPreviewView.setVisibility(View.VISIBLE);
    } else {
      mColorValue.setText("");
      mColorPreviewView.setVisibility(View.GONE);
    }
  }
}
