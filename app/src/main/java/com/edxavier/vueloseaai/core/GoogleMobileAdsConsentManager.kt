package com.edxavier.vueloseaai.core

import android.app.Activity
import android.content.Context
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

/**
 * The Google Mobile Ads SDK provides the User Messaging Platform (Google's
 * Consent Management Platform) as the primary tool to help you manage user
 * privacy choices. This class is a wrapper for the User Messaging Platform
 * (UMP) SDK which simplifies the implementation of the Google-mandated
 * consent flow.
 */
class GoogleMobileAdsConsentManager(private val context: Context) {
    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

    /** Helper variable to determine if the app can request ads. */
    val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()

    /** Helper variable to determine if the privacy options form is required. */
    val isPrivacyOptionsRequired: Boolean
        get() = consentInformation.privacyOptionsRequirementStatus ==
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    /**
     * Helper method to call the UMP SDK methods to request consent information
     * and load/show a consent form if necessary.
     */
    fun gatherConsent(
        activity: Activity,
        onConsentGatheringFinished: (error: com.google.android.ump.FormError?) -> Unit
    ) {
        val params = ConsentRequestParameters.Builder().build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    onConsentGatheringFinished(formError)
                }
            },
            { requestConsentError ->
                onConsentGatheringFinished(requestConsentError)
            }
        )
    }

    /**
     * Helper method to call the UMP SDK method to show the privacy options form.
     */
    fun showPrivacyOptionsForm(
        activity: Activity,
        onConsentFormDismissedListener: ConsentForm.OnConsentFormDismissedListener
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener)
    }
}
