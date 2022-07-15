package com.erif.alarmmanager.view.receiver

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.erif.alarmmanager.R
import com.erif.alarmmanager.databinding.ActReceiverBinding
import com.erif.alarmmanager.model.receiver.ModelActReceiver
import com.erif.alarmmanager.utils.database.DatabaseAlarm
import com.erif.alarmmanager.utils.services.AlarmRingtoneServices
import com.erif.alarmmanager.view_model.VMActReceiver
import com.liuguangqiang.swipeback.SwipeBackLayout

class ActReceiver : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stayAwake()
        val binding = ActReceiverBinding.inflate(layoutInflater)
        val model = ModelActReceiver()
        binding.model = model
        val viewModel = VMActReceiver(
            model, DatabaseAlarm(this),
            AnimationUtils.loadAnimation(this, R.anim.anim_slide_up)
        )
        binding.viewModel = viewModel
        setContentView(binding.root)
        binding.swipeBack.setDragEdge(SwipeBackLayout.DragEdge.BOTTOM)
        binding.rippleBg.startRippleAnimation()

    }

    private fun stayAwake() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            )
        }
        with(getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                requestDismissKeyguard(this@ActReceiver, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarmMusic()
    }

    private fun stopAlarmMusic(){
        stopService(Intent(this, AlarmRingtoneServices::class.java))
    }

}