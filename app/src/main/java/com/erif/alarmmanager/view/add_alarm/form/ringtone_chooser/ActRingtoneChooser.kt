package com.erif.alarmmanager.view.add_alarm.form.ringtone_chooser

import android.content.Intent
import android.database.Cursor
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.erif.alarmmanager.databinding.ActRingtoneChooserBinding
import com.erif.alarmmanager.model.add_alarm.form.ringtone.ModelActRingtoneChooser
import com.erif.alarmmanager.model.add_alarm.form.ringtone.ModelItemRingtone
import com.erif.alarmmanager.utils.Constant
import com.erif.alarmmanager.view_model.add_alarm.form.VMActRingtoneChooser

class ActRingtoneChooser : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var viewModel: VMActRingtoneChooser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActRingtoneChooserBinding.inflate(layoutInflater)
        val toolbar = binding.actRingtoneToolbar
        val model = ModelActRingtoneChooser()
        binding.model = model
        viewModel = VMActRingtoneChooser(model)
        binding.viewModel = viewModel
        setContentView(binding.root)
        setSupportActionBar(toolbar)
        toolbar.post { loadRingtone() }

        viewModel?.mutableClick()?.observe(this) {
            if (it is Int) {
                if (it == 0) {
                    val intent = Intent()
                    intent.putExtra("ringtoneName", model.selectedTitle)
                    intent.putExtra("ringtoneUri", model.selectedUri.toString())
                    setResult(Constant.REQUEST_CODE_RINGTONE, intent)
                    finish()
                }
            } else if (it is Uri?) {
                it?.let { uri -> playRingtone(uri) }
            }
        }
    }

    private fun loadRingtone() {
        val list: MutableList<ModelItemRingtone> = ArrayList()
        val manager = RingtoneManager(this)
        manager.setType(RingtoneManager.TYPE_RINGTONE)
        val cursor: Cursor = manager.cursor
        while (cursor.moveToNext()) {
            val title: String = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val ringtoneURI: Uri = manager.getRingtoneUri(cursor.position)
            val item = ModelItemRingtone()
            item.title = title
            item.fileUri = ringtoneURI
            list.add(item)
        }
        viewModel?.createList(list)
        cursor.close()
    }

    private fun playRingtone(uri: Uri) {
        stopPlayer()
        mediaPlayer = MediaPlayer.create(this, uri)
        //mediaPlayer?.setOnCompletionListener(mediaListener())
        mediaPlayer?.start()
    }

    /*private fun mediaListener(): MediaPlayer.OnCompletionListener{
        return MediaPlayer.OnCompletionListener {
            //helper?.toast("Finish")
        }
    }*/

    private fun stopPlayer(){
        mediaPlayer?.let {
            if (it.isPlaying) it.stop()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        stopPlayer()
    }

}