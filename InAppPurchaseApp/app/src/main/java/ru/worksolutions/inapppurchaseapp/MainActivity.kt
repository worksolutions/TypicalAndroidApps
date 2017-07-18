package ru.worksolutions.inapppurchaseapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.TransactionDetails
import android.content.Intent
import android.support.v7.widget.AppCompatButton
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text


class MainActivity : AppCompatActivity(), BillingProcessor.IBillingHandler {

    private lateinit var billingProcessor: BillingProcessor


    private val inAppTesting1 = "myapp_testing_inapp1"
    private val buyCoins = "myapp_testing_inapp2"

    private val buyPremium = "wssuport_premium"

    private val subscribe = "premium_subscribe"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val base64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuuuRm1m9tZeoj2jo3wH9q+8Vh3Ffxjb8bhroMvS2oOTEz17AnK8tQT9Wwh2Mw5KjH0jOaHzls2NA2FVKeE03A9k3FFouWF1zMoJ4hjGhZ9TvgPf4xDtdaXmmw8R5D/Ib95ddZBrYsmlkEmGnhMvFO6JLZOdJkSTHxJP/S3P4n6/saQcJuva5cFASdeld96TqUiUFSSGXvY/Huo81ER9s49KUvwm4kCrYmkyjrOxox++MEyNUTEMid/urOTBFigSvrHMClYz42mPjlaAirTOz+Z4w6MP+yhKu99/YqjyeSGWtWHrHvExPo8nl2W9WfCxQQLp6lmVOEB9JDaPDVpOjzwIDAQAB"
        billingProcessor = BillingProcessor(this, base64, this)

        findViewById(R.id.btn_buy_premium).setOnClickListener {
            billingProcessor.purchase(this, buyPremium)
        }

        findViewById(R.id.btn_subscribe).setOnClickListener {
            billingProcessor.subscribe(this, subscribe)
        }

        findViewById(R.id.btn_buy_coins).setOnClickListener {
            billingProcessor.purchase(this, buyCoins)
        }

    }

    private fun hasPremium(has: Boolean) {
        (findViewById(R.id.tv_has_premium) as TextView).text = if (has) "Вы купили премиум аккаунт" else "У вас нет премиум аккаунта"
    }

    private fun hasSubscription(has: Boolean) {
        (findViewById(R.id.tv_has_subscribe) as TextView).text = if (has) "У вас есть подписка" else "У вас нет подписки"
    }

    private fun coins() {
        val v: TextView = (findViewById(R.id.tv_coins_count) as TextView)
        v.text = "Количество монет: ${coinsCount}"
    }

    private fun initializeUI() {
        hasPremium(buyPremium in billingProcessor.listOwnedProducts())
        hasSubscription(subscribe in billingProcessor.listOwnedSubscriptions())
        coins()
    }

    override fun onBillingInitialized() {
        Log.e("MainActivity", "onBillingInitialized()")
    }

    override fun onPurchaseHistoryRestored() {
        Log.e("MainActivity", "onPurchaseHistoryRestored()")
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails) {

        if (productId == buyCoins) {
            coinsCount += 10
            billingProcessor.consumePurchase(buyCoins)

        }
        initializeUI()

    }

    private var coinsCount = 0

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Log.e("MainActivity", "onBillingError()")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("MainActivity", "onResume")
        initializeUI()
    }

    override fun onPause() {
        super.onPause()
        Log.e("MainActivity", "onPause")
    }

    private fun handlePayment(productId: String, details: TransactionDetails) {
//        when (productId) {
//            buyPremium
//        }
    }
}
