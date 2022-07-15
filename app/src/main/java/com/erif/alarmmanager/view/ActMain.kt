package com.erif.alarmmanager.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.erif.alarmmanager.databinding.ActMainBinding
import com.erif.alarmmanager.model.ModelActMain
import com.erif.alarmmanager.model.ModelItemAlarm
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.utils.MyHelper
import com.erif.alarmmanager.utils.callback.CallbackAlarmForm
import com.erif.alarmmanager.utils.callback.CallbackConfirmation
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import com.erif.alarmmanager.view.add_alarm.FrgAddAlarmBottomSheet
import com.erif.alarmmanager.view.add_alarm.form.FrgConfirmationBottomSheet
import com.erif.alarmmanager.view_model.VMActMain

class ActMain : AppCompatActivity(), CallbackAlarmForm, CallbackConfirmation {

    private var viewModel: VMActMain? = null
    private var helper: MyHelper? = null

    private var dialogAdd: FrgAddAlarmBottomSheet? = null
    private var dialogDelete: FrgConfirmationBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        helper = MyHelper(this)
        dialogDelete = FrgConfirmationBottomSheet(this)

        val binding = ActMainBinding.inflate(layoutInflater)
        val toolbar = binding.actMainToolbar

        val model = ModelActMain()
        binding.model = model

        viewModel = VMActMain(model, DatabaseAlarm(this), helper)
        binding.viewModel = viewModel
        setContentView(binding.root)
        setSupportActionBar(toolbar)

        toolbar.post {
            requestPermission()
            viewModel?.setupAlarmList()
        }

        viewModel?.mutableClick()?.observe(this) {
            if (it is Int) {
                if (it == 0) {
                    showFormDialog()
                } else if (it == 1) {
                    showDeleteConfirmation()
                }
            } else if (it is ModelItemAlarm) {
                showFormDialog(it.id)
            }
        }
    }

    private fun showFormDialog(){
        dialogAdd = FrgAddAlarmBottomSheet(this, resultLauncher)
        showForm()
    }

    private fun showFormDialog(getAlarmId: Int){
        dialogAdd = FrgAddAlarmBottomSheet(this, true, getAlarmId, resultLauncher)
        showForm()
    }

    private fun showForm(){
        dialogAdd?.show(supportFragmentManager, dialogAdd?.tag)
    }

    // Request Permission
    private fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ANSWER_PHONE_CALLS) !=
                PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ANSWER_PHONE_CALLS)) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.ANSWER_PHONE_CALLS), 1)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.ANSWER_PHONE_CALLS), 1)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ANSWER_PHONE_CALLS) ==
                                    PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    /*private fun showNotification(){
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

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val requestCode = it.resultCode
        val data = it.data
        when (requestCode) {
            Constant.REQUEST_CODE_RINGTONE -> {
                data?.let { mData->
                    var ringtoneName: String? = null
                    var ringtoneUri: String? = null
                    if (mData.hasExtra("ringtoneName"))
                        ringtoneName = mData.getStringExtra("ringtoneName")
                    if (mData.hasExtra("ringtoneUri"))
                        ringtoneUri = mData.getStringExtra("ringtoneUri")
                    ringtoneName?.let { mRingtoneName->
                        ringtoneUri?.let { mRingToneUri->
                            dialogAdd?.updateRingtone(mRingtoneName, mRingToneUri)
                        }
                    }
                }
            }
            Constant.REQUEST_CODE_TITLE -> {
                data?.let { mData->
                    if (mData.hasExtra("alarmTitle")){
                        val alarmTitle = mData.getStringExtra("alarmTitle")
                        alarmTitle?.let { mDesc->
                            dialogAdd?.updateTitle(mDesc)
                        }
                    }
                }
            }
            Constant.REQUEST_CODE_DESC -> {
                data?.let { mData->
                    if (mData.hasExtra("alarmDesc")){
                        val alarmDesc = mData.getStringExtra("alarmDesc")
                        alarmDesc?.let { mDesc->
                            dialogAdd?.updateDesc(mDesc)
                        }
                    }
                }
            }
        }
    }

    // Show Delete Confirmation
    private fun showDeleteConfirmation(){
        dialogDelete?.show(supportFragmentManager, dialogDelete?.tag)
    }

    // Database On Insert New Alarm
    override fun onInsert(id: Int, duration: Int) {
        viewModel?.refreshList()
        val millis = helper?.getMillis(0, 1) ?: 0
        helper?.triggerAlarm(id, millis, duration, true)
    }

    // Database On Update Alarm
    override fun onUpdate(id: Int, isUpdateTime: Boolean, duration: Int) {
        viewModel?.refreshList()
        if (isUpdateTime){
            if (helper?.isAlarmActive(id) == true){
                helper?.cancelAlarm(id, false)
                val millis = helper?.getMillis(1, 0) ?: 0
                helper?.triggerAlarm(id, millis, duration, true)
                helper?.updateIndex(id, 0)
            }
        }
    }

    // On Click Confirmation Cancel
    override fun onConfirmationCancel() {

    }

    // On Click Confirmation Yes
    override fun onConfirmationYes() {
        helper?.deleteAlarmFromDatabase(viewModel?.currentID ?: 0)
        Log.d("ActMain", "Id: ${viewModel?.currentID ?: -1}")
        viewModel?.getAdapter()?.deleteItem(viewModel?.currentPosition ?: -1)
        if ((viewModel?.list?.size ?: 0) < 1)
            viewModel?.showEmpty()
        viewModel?.currentID = -1
        viewModel?.currentPosition = -1
        helper?.cancelAlarm(viewModel?.currentID ?: -1, false)
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
    }*/

}