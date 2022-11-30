package com.example.androidqr_java;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import com.example.androidqr_java.databinding.ViewCameraBinding;

//カメラ関係
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import java.util.Collections;
import java.util.List;

public class CameraView extends CameraActivity implements CvCameraViewListener2 {

    private ViewCameraBinding binding;
    private CameraBridgeViewBase mOpenCvCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCvCameraView = binding.cameraView;
        //カメラviewの可視化設定
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        //フレーム処理や描画等の、カメラとOpenCVライブラリとのやり取りを実装
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    //BaseLoaderCallbackクラスのインスタンス生成
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        /*OpenCVライブラリの初期化後に呼び出されるコールバックメソッド
          引数は初期化ステータス*/
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //成功でカメラプレビュー開始
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    //ユーザーがActivityを離れたら呼び出される。処理は続く
    @Override
    public void onPause()
    {
        super.onPause();
        //カメラviewが表示されていなければtrue
        if (mOpenCvCameraView != null)
            //viewを無効にする
            mOpenCvCameraView.disableView();
    }

    //ユーザーがActivityに戻ってきたら呼び出される
    @Override
    public void onResume()
    {
        super.onResume();
        /*OpenCVのデバッグ:成功でtrueを返す
                         失敗でfalseを返す*/
        if (!OpenCVLoader.initDebug()) {
            //失敗:OpenCVライブラリの読み込みと初期化を行う
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        }
        else {
            //成功:カメラプレビュー開始
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    //アクテビティが破棄されるときに呼び出される
    @Override
    public void onDestroy() {
        super.onDestroy();
        //カメラviewが表示されていなければtrue
        if (mOpenCvCameraView != null)
            //viewを無効にする
            mOpenCvCameraView.disableView();
    }

    //カメラのプレビューが開始されたときに呼び出される
    public void onCameraViewStarted(int width, int height) {
    }

    //カメラプレビューが停止した時に呼び出される
    public void onCameraViewStopped() {
    }

    //毎フレーム呼ばれる
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }
}