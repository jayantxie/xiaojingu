package com.jayantxie.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.jayantxie.R;

public class EvaluationDetailActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_detail);
        webView = (WebView) findViewById(R.id.webview);
        String url = "http://123.206.218.41:8080/web-ssm/evaluationData/showPage?id=" + getIntent().getIntExtra("id",0);
        webView.loadUrl(url);
    }
}
