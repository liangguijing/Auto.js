package org.liang.test.ui.explorer;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

import com.stardust.autojs.project.ProjectConfig;
import com.stardust.autojs.project.ProjectLauncher;
import com.stardust.pio.PFile;

import org.liang.test.R;
import org.liang.test.autojs.AutoJs;
import org.liang.test.model.explorer.ExplorerChangeEvent;
import org.liang.test.model.explorer.ExplorerItem;
import org.liang.test.model.explorer.Explorers;
import org.liang.test.ui.project.BuildActivity;
import org.liang.test.ui.project.BuildActivity_;
import org.liang.test.ui.project.ProjectConfigActivity;
import org.liang.test.ui.project.ProjectConfigActivity_;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExplorerProjectToolbar extends CardView {

    private ProjectConfig mProjectConfig;
    private PFile mDirectory;

    @BindView(R.id.project_name)
    TextView mProjectName;

    public ExplorerProjectToolbar(Context context) {
        super(context);
        init();
    }

    public ExplorerProjectToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExplorerProjectToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.explorer_project_toolbar, this);
        ButterKnife.bind(this);
        setOnClickListener(view -> edit());
    }

    public void setProject(PFile dir) {
        mProjectConfig = ProjectConfig.fromProjectDir(dir.getPath());
        if(mProjectConfig == null){
            setVisibility(GONE);
            return;
        }
        mDirectory = dir;
        mProjectName.setText(mProjectConfig.getName());
    }

    public void refresh() {
        if (mDirectory != null) {
            setProject(mDirectory);
        }
    }

    @OnClick(R.id.run)
    void run() {
        try {
            new ProjectLauncher(mDirectory.getPath())
                    .launch(AutoJs.getInstance().getScriptEngineService());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.build)
    void build() {
        BuildActivity_.intent(getContext())
                .extra(BuildActivity.EXTRA_SOURCE, mDirectory.getPath())
                .start();
    }

    @OnClick(R.id.sync)
    void sync() {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Explorers.workspace().registerChangeListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Explorers.workspace().unregisterChangeListener(this);
    }

    @Subscribe
    public void onExplorerChange(ExplorerChangeEvent event) {
        if (mDirectory == null) {
            return;
        }
        ExplorerItem item = event.getItem();
        if ((event.getAction() == ExplorerChangeEvent.ALL)
                || (item != null && mDirectory.getPath().equals(item.getPath()))) {
            refresh();
        }
    }

    void edit() {
        ProjectConfigActivity_.intent(getContext())
                .extra(ProjectConfigActivity.EXTRA_DIRECTORY, mDirectory.getPath())
                .start();
    }

}
