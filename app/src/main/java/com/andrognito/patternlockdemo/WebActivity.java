package com.andrognito.patternlockdemo;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebDownloader;
import com.just.agentweb.AgentWebSettings;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.DownloadListener;
import com.just.agentweb.DownloadingService;
import com.just.agentweb.MiddlewareWebChromeBase;
import com.just.agentweb.MiddlewareWebClientBase;
import com.just.agentweb.PermissionInterceptor;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by edz on 2018/2/12.
 */

public class WebActivity extends BaseActivity {
    private static final String TAG = WebActivity.class.getSimpleName();
    private AgentWeb mAgentWeb;

    private WebChromeClient mWebChromeClient = new WebChromeClient(){
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.d(TAG, "onConsoleMessage: ");
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            Log.d(TAG, "onCreateWindow: ");
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d(TAG, "onJsAlert: ");
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            Log.d(TAG, "onJsBeforeUnload: ");
            return super.onJsBeforeUnload(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Log.d(TAG, "onJsConfirm: ");
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.d(TAG, "onJsPrompt: ");
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            Log.d(TAG, "onShowFileChooser: ");
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
            Log.d(TAG, "onCloseWindow: ");
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
            Log.d(TAG, "onGeolocationPermissionsHidePrompt: ");
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            Log.d(TAG, "onGeolocationPermissionsShowPrompt: ");
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            Log.d(TAG, "onHideCustomView: ");
        }

        @Override
        public void onPermissionRequest(PermissionRequest request) {
            super.onPermissionRequest(request);
            Log.d(TAG, "onPermissionRequest: ");
        }

        @Override
        public void onPermissionRequestCanceled(PermissionRequest request) {
            super.onPermissionRequestCanceled(request);
            Log.d(TAG, "onPermissionRequestCanceled: ");
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.d(TAG, "onProgressChanged: ");
        }

        
    };

    private WebViewClient mWebViewClient= new WebViewClient(){
        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            super.onReceivedClientCertRequest(view, request);
            Log.d(TAG, "onReceivedClientCertRequest: ");
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            super.onFormResubmission(view, dontResend, resend);
            Log.d(TAG, "onFormResubmission: ");
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.d(TAG, "onLoadResource: ");
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
            Log.d(TAG, "onPageCommitVisible: ");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished: ");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "onPageStarted: ");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.d(TAG, "onReceivedError: ");
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
            Log.d(TAG, "onReceivedHttpAuthRequest: ");
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Log.d(TAG, "onReceivedHttpError: ");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d(TAG, "shouldOverrideUrlLoading: ");
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Log.d(TAG, "shouldInterceptRequest: ");
            return super.shouldInterceptRequest(view, request);
        }
    };
    private PermissionInterceptor mPermissionInterceptor;
    private DownloadListener mDownloadListener = new DownloadListener() {
        @Override
        public boolean start(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            Log.d(TAG, "start: url"+url);
            Toast.makeText(WebActivity.this,"开始下载",Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public void progress(String url, long downloaded, long length, long usedTime, DownloadingService downloadingService) {
            Log.d(TAG, "progress: url"+url);
            Log.d(TAG, "progress: downloaded"+downloaded);
            Log.d(TAG, "progress: length"+length);
            Log.d(TAG, "progress: usedTime"+usedTime);
        }

        @Override
        public boolean result(String path, String url, Throwable throwable) {
            Log.d(TAG, "result: path"+path);
            if (throwable == null) { //下载成功
                //do you work
                Toast.makeText(WebActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
            } else {//下载失败
                Toast.makeText(WebActivity.this,"下载失败",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    };
    private Dialog mProgressDialog;
    private ProgressBar mProgressBar;
    private TextView mTextViewProgress;
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WebActivity", "onCreate: ");

        mUrl = getIntent().getStringExtra("url");
        LinearLayout l = new LinearLayout(this);



        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(l, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .setIndicatorColorWithHeight(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(getSettings())//设置 AgentWebSettings。
                .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                .setWebChromeClient(mWebChromeClient) //WebChromeClient
                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .setDownloadListener(mDownloadListener) //下载回调
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownScheme() //拦截找不到相关页面的Scheme AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(mUrl); //WebView载入该url地址的页面并显示。
        WebView webView = mAgentWeb.getWebCreator().getWebView();
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webView.setDownloadListener(new android.webkit.DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                Log.d(TAG, "webView onDownloadStart: url"+url);
//                final RxPermissions rxPermissions = new RxPermissions(WebActivity.this);
//
//                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                        .subscribe(new Observer<Boolean>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//
//                            }
//
//                            @Override
//                            public void onNext(Boolean aBoolean) {
//                                if (aBoolean) {
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });

                downloadApk(url);

            }
        });
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        setContentView(l);

    }

    private void downloadApk(String url) {
        OkGo.<File>get(url).tag(this).execute(new FileCallback(getCacheDir().getAbsolutePath(),"downloaded.apk") {
            @Override
            public void onSuccess(Response<File> response) {
                AppUtils.installApp(response.body(),"com.andrognito.patternlockdemo");
            }

            @Override
            public void onStart(Request<File, ? extends Request> request) {
                super.onStart(request);
                initDialog();
                mProgressDialog.show();
            }


            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);
                mProgressBar.setProgress((int) ((progress.currentSize / (float) progress.totalSize) * 100));
                mTextViewProgress.setText((int) ((progress.currentSize / (float) progress.totalSize) * 100)+"%");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
                mProgressBar.setProgress(0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    private void initDialog() {
        mProgressDialog = new Dialog(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.layout_progress, null);
        mProgressDialog.setContentView(view);
        Window dialogWindow = mProgressDialog.getWindow();
        mProgressDialog.setCanceledOnTouchOutside(false);
        final WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = getResources().getDisplayMetrics().widthPixels;
        view.measure(0, 0);
        view.post(new Runnable() {
            @Override
            public void run() {
                lp.height = SizeUtils.dp2px(60);
            }
        });

        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        mTextViewProgress = (TextView) view.findViewById(R.id.tv_progress);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                OkGo.getInstance().cancelTag(this);
            }
        });
        mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0;
            }
        });
    }

    private MiddlewareWebClientBase getMiddlewareWebClient() {
        return null;
    }

    private MiddlewareWebChromeBase getMiddlewareWebChrome() {
        return null;
    }

    private AgentWebSettings getSettings() {
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }

        return super.onKeyDown(keyCode, event);

    }
}
