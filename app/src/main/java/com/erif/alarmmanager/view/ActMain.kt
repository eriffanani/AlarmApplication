package com.erif.alarmmanager.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.erif.alarmmanager.R
import com.erif.alarmmanager.databinding.ActMainBinding
import com.erif.alarmmanager.model.ModelActMain
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.view_model.VMActMain

class ActMain : AppCompatActivity() {

    private var viewModel: VMActMain? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding(){
        val binding: ActMainBinding = DataBindingUtil.setContentView(this, R.layout.act_main)
        val model = ModelActMain()
        binding.model = model
        viewModel = VMActMain(this, model, supportFragmentManager)
        binding.viewModel = viewModel
        setSupportActionBar(binding.actMainToolbar)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ANSWER_PHONE_CALLS) ==
                                    PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                        //AutoStartHelper.instance.getAutoStartPermission(this)
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    /*private fun showNotif(){
        val mBuilder = NotificationCompat.Builder(this, "notify_001")
        val fullScreenIntent = Intent(this, ActivityReceiver::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
                this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText("BIG")
        bigText.setBigContentTitle("Today's Bible Verse")
        bigText.setSummaryText("Text in detail")

        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background)
        mBuilder.setContentTitle("Your Title")
        mBuilder.setContentText("Your text")
        mBuilder.priority = NotificationCompat.PRIORITY_HIGH
        mBuilder.setCategory(NotificationCompat.CATEGORY_CALL)
        mBuilder.setFullScreenIntent(fullScreenPendingIntent, true)
        mBuilder.setStyle(bigText)

        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "Your_channel_id"
            val channel = NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }

        mNotificationManager.notify(0, mBuilder.build())
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constant.REQUEST_CODE_RINGTONE -> {
                data?.let { mData->
                    var ringtoneName: String? = null
                    var ringtoneUri: String? = null
                    if (mData.hasExtra("ringtoneName"))
                        ringtoneName = mData.getStringExtra("ringtoneName")
                    if (mData.hasExtra("ringtoneUri"))
                        ringtoneUri = mData.getStringExtra("ringtoneUri")
                    viewModel?.let { mViewModel->
                        ringtoneName?.let { mRingtoneName->
                            ringtoneUri?.let { mRingToneUri->
                                mViewModel.updateRingtone(mRingtoneName, mRingToneUri)
                            }
                        }
                    }
                }
            }
            Constant.REQUEST_CODE_TITLE -> {
                data?.let { mData->
                    if (mData.hasExtra("alarmTitle")){
                        viewModel?.let { mViewModel->
                            val alarmTitle = mData.getStringExtra("alarmTitle")
                            alarmTitle?.let { mDesc->
                                mViewModel.updateTitle(mDesc)
                            }
                        }
                    }
                }
            }
            Constant.REQUEST_CODE_DESC -> {
                data?.let { mData->
                    if (mData.hasExtra("alarmDesc")){
                        viewModel?.let { mViewModel->
                            val alarmDesc = mData.getStringExtra("alarmDesc")
                            alarmDesc?.let { mDesc->
                                mViewModel.updateDesc(mDesc)
                            }
                        }
                    }
                }
            }
        }
    }

}