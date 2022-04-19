package org.liang.test.ui.doc;

import android.webkit.WebView;

import org.liang.test.Pref;
import org.liang.test.R;
import org.liang.test.ui.BaseActivity;
import org.liang.test.ui.widget.EWebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Stardust on 2017/10/24.
 */
@EActivity(R.layout.activity_documentation)
public class DocumentationActivity extends BaseActivity {

    public static final String EXTRA_URL = "url";

    @ViewById(R.id.eweb_view)
    EWebView mEWebView;

    WebView mWebView;

    @AfterViews
    void setUpViews() {
        setToolbarAsBack(getString(R.string.text_tutorial));
        mWebView = mEWebView.getWebView();
        String url = getIntent().getStringExtra(EXTRA_URL);
        if (url == null) {
            url = Pref.getDocumentationUrl() + "index.html";
        }
        mWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
