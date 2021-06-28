package com.erif.alarmmanager.view_model.add_alarm.form

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.erif.alarmmanager.model.add_alarm.form.ringtone.ModelActRingtoneChooser
import com.erif.alarmmanager.model.add_alarm.form.ringtone.ModelItemRingtone
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.utils.MyHelper
import com.erif.alarmmanager.utils.adapter_holder.ringtone.AdapterRingtoneList


@SuppressLint("StaticFieldLeak")
class VMActRingtoneChooser constructor(context: Context, model: ModelActRingtoneChooser) : ViewModel() {

    private var context: Context? = null
    private var adapter: AdapterRingtoneList? = null
    private var list: MutableList<ModelItemRingtone>? = null
    private var helper: MyHelper? = null
    private var mediaPlayer: MediaPlayer? = null
    private var lastPosition: Int = -1
    private var model: ModelActRingtoneChooser? = null
    private var activity: Activity? = null

    init {
        this.context = context
        this.model = model
        activity = context as Activity
        helper = MyHelper(context)
        list = ArrayList()
        adapter = AdapterRingtoneList(this)
        loadRingtone()
    }

    private fun loadRingtone() {
        val manager = RingtoneManager(context)
        manager.setType(RingtoneManager.TYPE_RINGTONE)
        val cursor: Cursor = manager.cursor
        while (cursor.moveToNext()) {
            val title: String = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val ringtoneURI: Uri = manager.getRingtoneUri(cursor.position)
            val item = ModelItemRingtone()
            item.title = title
            item.fileUri = ringtoneURI
            list?.add(item)
        }
        cursor.close()
        createList()
    }

    private fun createList(){
        list?.let {
            adapter?.setList(it)
        }
    }

    fun onClickItem(item: ModelItemRingtone, position: Int){
        toggleItem(position)
        model?.selectedTitle = item.title
        model?.selectedUri = item.fileUri
        if (mediaPlayer != null)
            mediaPlayer?.let {
                if (it.isPlaying){
                    it.stop()
                }
            }
        item.fileUri?.let { playSound(it) }
    }

    fun onClickFabDone(){
        val intent = Intent()
        intent.putExtra("ringtoneName", model?.selectedTitle)
        intent.putExtra("ringtoneUri", model?.selectedUri.toString())
        activity?.setResult(Constant.REQUEST_CODE_RINGTONE, intent)
        activity?.finish()
    }

    private fun toggleItem(position: Int){
        if (lastPosition == -1){
            lastPosition = position
            list?.get(position)?.selected = true
        }else if (lastPosition != position){
            list?.get(lastPosition)?.selected = false
            list?.get(position)?.selected = true
            lastPosition = position
        }
    }

    fun getAdapter(): AdapterRingtoneList?{
        return adapter
    }

    private fun playSound(mUri: Uri){
        mediaPlayer = MediaPlayer.create(context, mUri)
        mediaPlayer?.setOnCompletionListener(mediaListener())
        mediaPlayer?.start()
    }

    private fun mediaListener(): MediaPlayer.OnCompletionListener{
        return MediaPlayer.OnCompletionListener {
            //helper?.toast("Finish")
        }
    }

    fun releasePlayer(){
        if (mediaPlayer != null)
            mediaPlayer?.let {
                if (it.isPlaying)
                    it.stop()
            }
    }

}