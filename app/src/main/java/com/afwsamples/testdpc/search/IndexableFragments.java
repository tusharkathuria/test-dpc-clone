package com.afwsamples.testdpc.search;

import com.afwsamples.testdpc.R;
import com.afwsamples.testdpc.common.BaseSearchablePolicyPreferenceFragment;
import com.afwsamples.testdpc.comp.BindDeviceAdminFragment;
import com.afwsamples.testdpc.policy.OverrideApnFragment;
import com.afwsamples.testdpc.policy.PolicyManagementFragment;
import com.afwsamples.testdpc.policy.keyguard.LockScreenPolicyFragment;
import com.afwsamples.testdpc.policy.keyguard.PasswordConstraintsFragment;
import com.afwsamples.testdpc.profilepolicy.ProfilePolicyManagementFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores all the indexable fragments.
 *
 * <p>To index a newly added fragment, there are only two things needed to be done. Make you
 * fragment extends {@link BaseSearchablePolicyPreferenceFragment} and add it to this class.
 */
public class IndexableFragments {
  private static final List<BaseIndexableFragment> sIndexableFragments = new ArrayList<>();

  static {
    sIndexableFragments.add(
        new XmlIndexableFragment(PolicyManagementFragment.class, R.xml.device_policy_header));
    sIndexableFragments.add(
        new XmlIndexableFragment(
            ProfilePolicyManagementFragment.class, R.xml.profile_policy_header));
    sIndexableFragments.add(
        new XmlIndexableFragment(LockScreenPolicyFragment.class, R.xml.lock_screen_preferences));
    sIndexableFragments.add(
        new XmlIndexableFragment(
            PasswordConstraintsFragment.class, R.xml.password_constraint_preferences));
    sIndexableFragments.add(
        new XmlIndexableFragment(BindDeviceAdminFragment.class, R.xml.bind_device_admin_policies));
    sIndexableFragments.add(new UserRestrictionIndexableFragment());
    sIndexableFragments.add(
        new XmlIndexableFragment(OverrideApnFragment.class, R.xml.override_apn_preferences));
  }

  public static List<BaseIndexableFragment> values() {
    return new ArrayList<>(sIndexableFragments);
  }
}
