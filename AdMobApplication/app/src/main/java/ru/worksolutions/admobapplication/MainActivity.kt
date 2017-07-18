package ru.worksolutions.admobapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.NativeExpressAdView
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener

class MainActivity : AppCompatActivity(), RewardedVideoAdListener {

    private var mAdView: AdView? = null

    private var mInterstitialAd: InterstitialAd? = null

    private var mAd: RewardedVideoAd? = null
    private lateinit var mVideoController: VideoController


    private val btnShowAd: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this, "ca-app-pub-8069798296755807~7376247173")

        val adView = findViewById(R.id.nativeAdView) as NativeExpressAdView

        // Set its video options.
        adView.videoOptions = VideoOptions.Builder()
                .setStartMuted(true)
                .build()

        // The VideoController can be used to get lifecycle events and info about an ad's video
        // asset. One will always be returned by getVideoController, even if the ad has no video
        // asset.
        mVideoController = adView.videoController
        mVideoController.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
            override fun onVideoEnd() {
                Log.d("LOG_TAG", "Video playback is finished.")
                super.onVideoEnd()
            }
        }

        // Set an AdListener for the AdView, so the Activity can take action when an ad has finished
        // loading.
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (mVideoController.hasVideoContent()) {
                    Log.d("LOG_TAG", "Received an ad that contains a video asset.")
                } else {
                    Log.d("LOG_TAG", "Received an ad that does not contain a video asset.")
                }
            }
        }

        adView.loadAd(AdRequest.Builder()
                .addTestDevice("EFF440B875C9E6EAF2E5C741A05AD871")
                .build())

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd!!.adUnitId = "ca-app-pub-8069798296755807/5620312370"
        mInterstitialAd!!.loadAd(AdRequest.Builder()
                .addTestDevice("EFF440B875C9E6EAF2E5C741A05AD871")
                .build())

        mAd = MobileAds.getRewardedVideoAdInstance(this)
        mAd!!.rewardedVideoAdListener = this



        mAdView = findViewById(R.id.adView) as AdView
        val adRequest = AdRequest.Builder()
                .addTestDevice("EFF440B875C9E6EAF2E5C741A05AD871")
                .build()
        mAdView!!.loadAd(adRequest)

        (findViewById(R.id.btn_show_ad) as Button).setOnClickListener {
            if (mInterstitialAd!!.isLoaded) {
                mInterstitialAd!!.show()
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.")
            }
        }

        (findViewById(R.id.btn_show_video) as Button).setOnClickListener {
            if (mAd!!.isLoaded) {
                mAd!!.show()
            } else {
                Log.d("TAG", "video wasn't loaded yet.")
            }
        }

        loadRewardedVideoAd()

    }

    private fun loadRewardedVideoAd() {
        mAd!!.loadAd("ca-app-pub-3940256099942544/5224354917", AdRequest.Builder().build())
    }

    override fun onRewardedVideoAdLoaded() {

    }

    override fun onRewardedVideoAdOpened() {

    }

    override fun onRewardedVideoStarted() {

    }

    override fun onRewardedVideoAdClosed() {

    }

    override fun onRewarded(rewardItem: RewardItem) {

    }

    override fun onRewardedVideoAdLeftApplication() {

    }

    override fun onRewardedVideoAdFailedToLoad(i: Int) {

    }

    public override fun onPause() {
        if (mAdView != null) {
            mAdView!!.pause()
        }
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        if (mAdView != null) {
            mAdView!!.resume()
        }
    }

    public override fun onDestroy() {
        if (mAdView != null) {
            mAdView!!.destroy()
        }
        super.onDestroy()
    }

}
