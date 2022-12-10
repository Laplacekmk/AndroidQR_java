/* 参考url
* opencv公式:https://docs.opencv.org/4.x/d5/df8/tutorial_dev_with_OCV_on_Android.html
* CameraActivityクラスの参考url：http://www.java2s.com/example/java-src/pkg/org/opencv/android/cameraactivity-b8006.html
*参考にしたcode:https://coskxlabsite.stars.ne.jp/html/android/OpenCVpreview/OpenCVpreview_A.html
*opencvについて:https://qiita.com/ohwada/items/53305d29f33bd14e6683
* */
package com.example.androidqr_java;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import org.opencv.objdetect.QRCodeDetector;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.CvException;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;
import java.util.List;

public class CameraView extends CameraActivity implements CvCameraViewListener2 {
//
    private ViewCameraBinding binding;
    private CameraBridgeViewBase mOpenCvCameraView;

    private final String TAG = "CameraView";
    //Handlerの役割は、処理の順を決定したり、スレッド間のやり取りを可能にする
    //参考url：https://sankumee.hatenadiary.org/entry/20120329/1333021847
    private final Handler handler = new Handler();

    //検出されるQRcode四角形の初期値
    private final static Point[] QUAD = {
            new Point(0.0, 0.0) ,
            new Point(0.0, 2.0) ,
            new Point(2.0, 1.0),
            new Point(2.0, 2.0) };

    // QRcode周囲の線
    private final static Scalar    LINE_COLOR = new Scalar(0, 255, 0, 255);
    private final static  int LINE_THICKNESS = 3;
    //QRcodeで読み取ったテキスト
    private  String result = null;

    //Matは画像を配列で表したもの
    private Mat mRgba;
    private Mat mGray;

    private QRCodeDetector mQRCodeDetector;

    //MatOfPoint2のcode：https://github.com/naver/imagestabilizer/blob/master/android/ImageStabilizer/libraries/opencv/src/org/opencv/core/MatOfPoint2f.java)
    private MatOfPoint2f mPoints;

    private void setupQRCodeDetector() {
        mQRCodeDetector = new QRCodeDetector();

        // setup buffer area
        try {
            mPoints = new MatOfPoint2f(QUAD);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }

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

        Log_i("oncreate");
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        /*OpenCVライブラリの初期化後に呼び出されるコールバックメソッド
          引数は初期化ステータス*/
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //成功でQRcode読み取りの初期設定と、カメラプレビュー開始
                    setupQRCodeDetector();
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    //デフォルトでスーパークラスのメソッドを呼び出し
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

        Log_i("onpause");
    }

    //ユーザーがActivityに戻ってきたら呼び出される
    @Override
    public void onResume() {
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

        Log_i("onresume");
    }

    //MATをlistで返す(言い換えれば、画像を配列のような形で返している)
    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        //オーバーライドして、返り値をカメラviewに変換
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

        Log_i("onDestroy");
    }

    //カメラのプレビューが開始されたときに呼び出される
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mGray = new Mat();

        Log_i("onCameraViewStarted");
    }

    //カメラプレビューが停止した時に呼び出される
    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
        if (mPoints != null){
            mPoints.release();
        }

        Log_i("onCameraViewStopped");
    }

    //毎フレーム呼ばれる（返り値は、変更された表示する必要のあるフレーム）
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        mGray = inputFrame.rgba();

        //QRcodeの検出
        if (mQRCodeDetector.detect(mGray, mPoints)) {
            try {
                //QRcode解析
                result = mQRCodeDetector.decode(mGray, mPoints);

                //取得した文字をtextviewにセット
                if (result != null && result.length() > 0){
                    //結果が取得でき、かつ1文字以上の時に
                    //QRcode四角形の描画
                    //文字の描画
                    drawQuadrangle();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.getText.setText(result);
                        }
                    });
                }
                else{
                    //取得した文字列の初期化
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.getText.setText(null);
                        }
                    });
                }
            } catch (CvException e) {     //opencvのerrorクラス
                e.printStackTrace();
            }
        }

        return mRgba;
    }

    //QRcode四角形の描画
    private void drawQuadrangle() {

        int i  = 0;
        int i2 = 0;

        Point pt1;
        Point pt2;

        //各ポイントを取得
        Point[] pointsArray = mPoints.toArray();
        int length = pointsArray.length;

        //取得したポイントが4つ出なければ描画しない
        if(length != 4) return;

        //取得したポイントが4つであれば描画する
        for (i = 0; i < length; i++) {

            // next point
            i2 = i + 1;

            //ポイントが最後になったら最初につなげる
            if (i == 3 ) {
                i2 = 0;
            }

            pt1 = pointsArray[i];
            pt2 = pointsArray[i2];

            Imgproc.line(mRgba, pt1, pt2, LINE_COLOR, LINE_THICKNESS);
        }
    }

    void Log_i(String log){
        Log.i(TAG, log);
    }

}

