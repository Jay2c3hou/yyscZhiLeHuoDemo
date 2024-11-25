package com.example.yyscdemo.demo3

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * @author : yysc
 * @date : 2024/11/23 17:01.
 * @description :
 */
class AudioPlayer(private var mediaPlayer: MediaPlayer? = null) {

    private var listener: AudioPlayerListener? = null
    private var finishListener: AudioPlayerFinishListener? = null

    interface AudioPlayerListener {
        fun onAudioProgressUpdate(currentFrame: Int)
    }

    interface AudioPlayerFinishListener {
        fun onAudioFinish()
    }

    fun setAudioPlayerListener(listener: AudioPlayerListener) {
        this.listener = listener
    }

    fun setAudioPlayerFinishListener(listener: AudioPlayerFinishListener) {
        this.finishListener = listener
    }

    fun startAudio() {
        mediaPlayer?.setOnCompletionListener {
            // 播放完成后的逻辑 判断页数 跳转下一页,并重新开始播放
            Log.e("yysc", "setOnCompletionListener")
            finishListener?.onAudioFinish()
        }
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                trackAudioProgress()
            }
        }
    }

    fun pauseAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    fun releaseAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    private fun trackAudioProgress() {
        // 使用 Handler 定时更新播放进度
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentFrame = mediaPlayer?.currentPosition ?: return // 获取当前播放进度，单位毫秒
                listener?.onAudioProgressUpdate(currentFrame)
                handler.postDelayed(this, 50) // 每100ms更新一次
            }
        }
        handler.post(runnable)
    }
}
