package com.example.yyscdemo.demo3

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.yyscdemo.click
import com.example.yyscdemo.databinding.FragmentReadBinding
import com.example.yyscdemo.model.ContentList


private const val ARG_POSITION = "arg_position"
private const val ARG_CONTENT_LIST = "arg_content_list"

class ReadFragment : Fragment(), AudioPlayer.AudioPlayerListener {
    private var position: Int? = null
    private var contentList: ArrayList<ContentList>? = null
    private var wordAdapter: WordAdapter? = null
    private var audioPlayer: AudioPlayer? = null
    private var _binding: FragmentReadBinding? = null
    private val binding get() = requireNotNull(_binding) { "The property of binding has been destroyed." }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            contentList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList(ARG_CONTENT_LIST, ContentList::class.java)
            } else {
                it.getParcelableArrayList(ARG_CONTENT_LIST)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        val contentList = contentList ?: return
        val position = position ?: return
        binding.apply {
            val animationDrawable = icPlayAudio.getDrawable() as AnimationDrawable
            Glide.with(requireContext()).load(contentList[position].imgUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        // 弄个加载失败的图片就行
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        animationDrawable.start()
                        audioPlayer?.startAudio()
                        return false
                    }
                }).into(ivLesson)
            icStart.click {
                // 开始播放
                animationDrawable.start()
                it.isVisible = false
                icStop.isVisible = true
                audioPlayer?.startAudio()
            }
            icStop.click {
                // 暂停播放
                animationDrawable.stop()
                it.isVisible = false
                icStart.isVisible = true
                audioPlayer?.pauseAudio()
            }
        }
//        contentList[position].sentenceByXFList.first().needSpace = true
        wordAdapter = WordAdapter(contentList[position].sentenceByXFList)
        binding.rvContent.layoutManager = FlowLayoutManager()
        binding.rvContent.adapter = wordAdapter
        val mediaPlayer =
            MediaPlayer.create(requireContext(), contentList[position].audioUrl.toUri())
        audioPlayer = AudioPlayer(mediaPlayer)
        audioPlayer?.setAudioPlayerListener(this)
    }

    override fun onAudioProgressUpdate(currentFrame: Int) {
        val contentList = contentList ?: return
        val position = position ?: return
        val sentenceData = contentList[position].sentenceByXFList
        for (i in sentenceData.indices) {
            val wordData = sentenceData[i]
            if (currentFrame in wordData.wb..wordData.we) {
                // 高亮当前字
                wordAdapter!!.setHighlightedWord(i)
            } else {
                // 取消高亮
                wordAdapter!!.setUnhighlightedWord(i)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val isPlaying = audioPlayer?.isPlaying() ?: return
        if (!isPlaying) {
            audioPlayer?.startAudio()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.e("yyscfrag", "onPause")
        audioPlayer?.pauseAudio()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("yyscfrag", "onDestroy")
        _binding = null
        audioPlayer?.releaseAudio()
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int, contentList: ArrayList<ContentList>) = ReadFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_POSITION, position)
                putParcelableArrayList(ARG_CONTENT_LIST, contentList)
            }
        }
    }

}