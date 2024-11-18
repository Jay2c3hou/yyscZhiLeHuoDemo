package com.example.yyscdemo.demo1

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.yyscdemo.R
import com.example.yyscdemo.click
import com.example.yyscdemo.databinding.ActivityEditInfoBinding
import com.example.yyscdemo.databinding.LayoutDateSelectorBinding
import com.example.yyscdemo.databinding.LayoutDialogBottomBinding
import com.example.yyscdemo.demo1.utils.CameraUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File

/**
 * @author : yysc
 * @date : 2024/11/17 14:32
 * @description :第一个 demo 编辑信息页
 * 暂时没有做图片缓存,需要的话就 sp 存一下路径就行(需要判断一下)
 */
class EditInfoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEditInfoBinding.inflate(layoutInflater)
    }
    private var lastSelect = 0

    //Glide请求图片选项配置
    private val requestOptions =
        RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE) //不做磁盘缓存
            .skipMemoryCache(true) //不做内存缓存

    //存储拍完照后的图片
    private val outputImagePath by lazy {
        File(getExternalFilesDir(null), "test.jpg")
    }

    private val bottomSheetDialog by lazy {
        BottomSheetDialog(this@EditInfoActivity).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    private val bottomViewBinding by lazy {
        LayoutDateSelectorBinding.inflate(layoutInflater).apply {
            var selectedDate = ""
            datePickerView.listener = {
                selectedDate = it.toList().joinToString("/")
            }
            datePickerView.isNestedScrollingEnabled = true
            tvCancel.click {
                startArrowAnimation()
                bottomSheetDialog.cancel()
            }
            tvSure.click {
                binding.tvSelectedBirthday.text = selectedDate
                startArrowAnimation()
                bottomSheetDialog.cancel()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        initUI()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (allPermissionsGranted()) {
            changeAvatar()
        } else {
            showMsg("权限未开启")
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == RESULT_OK) {
                //显示图片
                outputImagePath.absolutePath.let {
//                    Log.e("yyscpath", it)
                    displayImage(it)
                }
            }

            SELECT_PHOTO -> if (resultCode == RESULT_OK) {
                //显示图片
                displayImage(CameraUtils.getImageOnKitKatPath(data, this))
            }

            else -> {}
        }
    }

    private fun initUI() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btn)) { v, insets ->
            val navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(
                navigationBars.left, navigationBars.top, navigationBars.right, navigationBars.bottom
            )
            insets
        }

        binding.apply {
            titleView.getLeftArrow().click {
                finish()
            }

            fun toggleSelection(isBoySelected: Boolean) {
                if ((isBoySelected && lastSelect == 0) || (!isBoySelected && lastSelect == 1)) return

                selectBoy.isSelected = isBoySelected
                selectGirl.isSelected = !isBoySelected
                lastSelect = if (isBoySelected) 0 else 1
            }

            selectBoy.click {
                toggleSelection(isBoySelected = true)
            }

            selectGirl.click {
                toggleSelection(isBoySelected = false)
            }

            ivAvatar.click {
                if (allPermissionsGranted()) {
                    changeAvatar()
                } else {
                    ActivityCompat.requestPermissions(
                        this@EditInfoActivity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                    )
                }
            }

            tvSelectedBirthday.click {
                bottomSheetDialog.setContentView(bottomViewBinding.root)
                startArrowAnimation()
                bottomSheetDialog.show()
            }
        }
    }

    /**
     * Toast提示
     * @param msg
     */
    private fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun startArrowAnimation() {
        val currentRotation = binding.ivArrow.rotation
        val targetRotation = if (currentRotation == 0f) 90f else 0f
        val rotationAnimator = ObjectAnimator.ofFloat(
            binding.ivArrow, "rotation", currentRotation, targetRotation
        )
        rotationAnimator.duration = 500
        rotationAnimator.start()
    }

    /**
     * 更换头像
     */
    private fun changeAvatar() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomViewBinding = LayoutDialogBottomBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomViewBinding.root)
        //拍照
        bottomViewBinding.apply {
            tvTakePhoto.click {
                takePhoto()
                bottomSheetDialog.cancel()
            }
            //打开相册
            tvChoosePhoto.click {
                openAlbum()
                bottomSheetDialog.cancel()
            }
            //取消
            tvCancel.click {
                bottomSheetDialog.cancel()
            }
        }
        bottomSheetDialog.show()
    }

    /**
     * 打开相册
     * 偷点懒直接写了这个 startActivityForResult
     * 其实不应该这样的,这个即将要被废弃,所以应该按官方那样,自己写个回调
     */
    private fun openAlbum() {
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO)
    }

    /**
     * 拍照
     * 同上
     */
    private fun takePhoto() {
        val takePhotoIntent = CameraUtils.getTakePhotoIntent(this, outputImagePath)
        // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
        startActivityForResult(takePhotoIntent, TAKE_PHOTO)
    }

    /**
     * 通过图片路径显示图片
     */
    private fun displayImage(imagePath: String) {
        if (!TextUtils.isEmpty(imagePath)) {
            //显示图片
            Glide.with(this).load(imagePath).apply(requestOptions)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("yyscGlide", "Load failed: ${e?.message}")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }).into(binding.ivAvatar)
        } else {
            showMsg("图片获取失败")
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        //启动相机标识
        const val TAKE_PHOTO = 1

        //启动相册标识
        const val SELECT_PHOTO = 2

        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()

        fun createIntent(content: Context): Intent {
            val intent = Intent(content, EditInfoActivity::class.java)
            return intent
        }
    }
}