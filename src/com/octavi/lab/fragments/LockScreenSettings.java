/*
 *  Copyright (C) 2015 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.octavi.lab.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.SwitchPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.octavi.lab.preferences.SystemSettingSwitchPreference;
import com.octavi.lab.preferences.SystemSettingListPreference;
import com.octavi.lab.preferences.CustomSeekBarPreference;

import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.util.octavi.OctaviUtils;

public class LockScreenSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_FOD_RECOGNIZING_ANIMATION = "fod_recognizing_animation";
    private static final String KEY_FOD_RECOGNIZING_ANIMATION_LIST = "fod_recognizing_animation_list";
    private static final String FOD_GESTURE = "fod_gesture";
    private static final String FOD_ANIMATION_CATEGORY = "fod_animations";
    private static final String LOCKSCREEN_MAX_NOTIF_CONFIG = "lockscreen_max_notif_cofig";

    private CustomSeekBarPreference mMaxKeyguardNotifConfig;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.octavi_lab_lockscreen);

        ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();
        final PreferenceCategory fodCat = (PreferenceCategory) prefScreen
                .findPreference(FOD_ANIMATION_CATEGORY);

        final boolean isFodAnimationResources = OctaviUtils.isPackageInstalled(getContext(),
                      getResources().getString(com.android.internal.R.string.config_fodAnimationPackage));

        Resources resources = getResources();

        SystemSettingSwitchPreference mFODSwitchPref = (SystemSettingSwitchPreference) findPreference(KEY_FOD_RECOGNIZING_ANIMATION);
	SystemSettingListPreference mFODListViewPref = (SystemSettingListPreference) findPreference(KEY_FOD_RECOGNIZING_ANIMATION_LIST);
        SystemSettingSwitchPreference mScreenOffFODSwitchPref = (SystemSettingSwitchPreference) findPreference(FOD_GESTURE);

        mMaxKeyguardNotifConfig = (CustomSeekBarPreference) findPreference(LOCKSCREEN_MAX_NOTIF_CONFIG);
        int kgconf = Settings.System.getInt(getContentResolver(),
                Settings.System.LOCKSCREEN_MAX_NOTIF_CONFIG, 3);
        mMaxKeyguardNotifConfig.setValue(kgconf);
        mMaxKeyguardNotifConfig.setOnPreferenceChangeListener(this);

	if (!resources.getBoolean(R.bool.config_showFODAnimationSettings) && !resources.getBoolean(R.bool.config_showScreenoffFOD)){
	    prefScreen.removePreference(mFODSwitchPref);
            prefScreen.removePreference(mFODListViewPref);
            prefScreen.removePreference(mScreenOffFODSwitchPref);
	}
        if (!isFodAnimationResources) {
            prefScreen.removePreference(fodCat);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mMaxKeyguardNotifConfig) {
            int kgconf = (Integer) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.LOCKSCREEN_MAX_NOTIF_CONFIG, kgconf);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.OCTAVI;
    }

}
