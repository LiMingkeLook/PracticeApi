package net.lzzy.practiceapi.activities.student;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.fragments.student.QuestionFragment;
import net.lzzy.practiceapi.models.student.SQuestion;
import net.lzzy.practiceapi.utils.AppUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class QuestionActivity extends AppCompatActivity {
    public static final int WHAT_RESPONSE_CODE = 0;
    public static final int WHAT_EXCEPTION = 1;
    public static final String EXTRA_PRACTICE_ID = "extraPracticeId";
    private static final int REQUEST_CODE_RESULT = 0;
    public static final String EXTRA_RESULT = "extraResult";
    private String practiceId;
    private int apiId;
    private List<SQuestion> questions;
    private TextView tvView;
    private TextView tvCommit;
    private ViewPager pager;
    private boolean isCommitted = false;
    private TextView tvHint;
    private FragmentStatePagerAdapter adapter;
    private int pos;
    private View[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_question);
        AppUtils.addActivity(this);
        questions=AppUtils.getsQuestions();
        retrieveData();
        initViews();
        initDots();
        setListeners();
    }

    private void setListeners() {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                refreshDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tvCommit.setOnClickListener(v -> commitPractice());
        tvView.setOnClickListener(v -> redirect());
    }

    private void redirect() {
        /*List<QuestionResult> results = UserCookies.getInstance().getResultFromCookies(questions);
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(EXTRA_PRACTICE_ID, practiceId);
        intent.putParcelableArrayListExtra(EXTRA_RESULT, (ArrayList<? extends Parcelable>) results);
        startActivityForResult(intent, REQUEST_CODE_RESULT);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_RESULT) {
            if (data.getBooleanExtra(ResultActivity.EXTRA_VIEW_FAVORITE, false)) {
                filterFavorite();
            }
            int page = data.getIntExtra(ResultActivity.EXTRA_POSITION, 0);
            if (page >= 0) {
                pos = page;
                pager.setCurrentItem(pos, true);
            }
        }*/
    }

    private void filterFavorite() {
        /*List<Question> favorites = new ArrayList<>();
        for (Question question : questions){
            if (FavoriteFactory.getInstance().isQuestionStarred(question.getId().toString())){
                favorites.add(question);
            }
        }
        questions.clear();
        questions.addAll(favorites);
        adapter.notifyDataSetChanged();
        initDots();
        refreshDots(0);*/
    }

    //region 提交成绩

    String info;

    private void commitPractice() {
       /* List<QuestionResult> results = UserCookies.getInstance().getResultFromCookies(questions);
        List<String> macs = AppUtils.getMacAddress();
        String[] items = new String[macs.size()];
        macs.toArray(items);
        info = items[0];
        new AlertDialog.Builder(this)
                .setTitle("选择Mac地址")
                .setSingleChoiceItems(items, 0, (dialog, which) -> info = items[which])
                .setNegativeButton("取消", null)
                .setPositiveButton("提交", (dialog, which) -> {
                    PracticeResult result = new PracticeResult(results, apiId, "gxy，" + info);
                    postResult(result);
                }).show();*/
    }

    /*private static class ResultHandler extends AbstractStaticHandler<QuestionActivity> {
        static final int RESPONSE_OK_MIN = 200;
        static final int RESPONSE_OK_MAX = 220;

        ResultHandler(QuestionActivity context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg, QuestionActivity activity) {
            ViewUtils.dismissProgress();
            if (msg.what == WHAT_RESPONSE_CODE) {
                int code = (int) msg.obj;
                if (code >= RESPONSE_OK_MIN && code <= RESPONSE_OK_MAX) {
                    Toast.makeText(activity, "提交成功", Toast.LENGTH_SHORT).show();
                    UserCookies.getInstance().commitPractice(activity.practiceId);
                    activity.redirect();
                } else {
                    Toast.makeText(activity, "提交失败，请重试", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == WHAT_EXCEPTION) {
                Toast.makeText(activity, "提交失败，请重试\n" + msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ResultHandler handler = new ResultHandler(this);

    private void postResult(PracticeResult result) {
        ViewUtils.showProgress(this, "正在提交成绩……");
        AppUtils.getExecutor().execute(() -> {
            try {
                int code = PracticeService.postResult(result);
                handler.sendMessage(handler.obtainMessage(WHAT_RESPONSE_CODE, code));
            } catch (JSONException | IOException e) {
                handler.sendMessage(handler.obtainMessage(WHAT_EXCEPTION, e.getMessage()));
            }
        });
    }*/
    //endregion

    private void initDots() {
        /*int count = questions.size();
        dots = new View[count];
        LinearLayout container = findViewById(R.id.activity_question_dots);
        container.removeAllViews();
        int px = ViewUtils.dp2px(16, this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(px, px);
        px = ViewUtils.dp2px(5, this);
        params.setMargins(px, px, px, px);
        for (int i = 0; i < count; i++) {
            TextView tvDot = new TextView(this);
            tvDot.setLayoutParams(params);
            tvDot.setBackgroundResource(R.drawable.dot_style);
            tvDot.setTag(i);
            tvDot.setOnClickListener(v -> pager.setCurrentItem((Integer) v.getTag()));
            container.addView(tvDot);
            dots[i] = tvDot;
        }*/
    }

    private void refreshDots(int pos) {
        /*for (int i = 0; i < dots.length; i++) {
            int drawable = i == pos ? R.drawable.dot_fill_style : R.drawable.dot_style;
            dots[i].setBackgroundResource(drawable);
        }*/
    }

    private void initViews() {
        tvView = findViewById(R.id.activity_question_tv_view);
        tvCommit = findViewById(R.id.activity_question_tv_commit);
        tvHint = findViewById(R.id.activity_question_tv_hint);
        pager = findViewById(R.id.activity_question_pager);
        if (isCommitted) {
            tvCommit.setVisibility(View.GONE);
            tvView.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.VISIBLE);
        } else {
            tvView.setVisibility(View.GONE);
            tvCommit.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.GONE);
        }
        adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            private int count = 0;
            @Override
            public Fragment getItem(int position) {
                SQuestion question = questions.get(position);
                return QuestionFragment.newInstance(question.getId().toString(), position, isCommitted);
            }

            @Override
            public int getCount() {
                return questions.size();
            }

            @Override
            public void notifyDataSetChanged() {
                count = getCount();
                super.notifyDataSetChanged();
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                if (count > 0){
                    count--;
                    return POSITION_NONE;
                }
                return super.getItemPosition(object);
            }
        };
        pager.setAdapter(adapter);
    }

    private void retrieveData() {
       /* practiceId = getIntent().getStringExtra(PracticesActivity.EXTRA_PRACTICE_ID);
        apiId = getIntent().getIntExtra(PracticesActivity.EXTRA_API_ID, -1);
        questions = QuestionFactory.getInstance().getByPractice(practiceId);
        isCommitted = UserCookies.getInstance().isPracticeCommitted(practiceId);
        if (apiId < 0 || questions == null || questions.size() == 0) {
            Toast.makeText(this, "no questions", Toast.LENGTH_SHORT).show();
            finish();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.remoreActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.setRunningActivity(getLocalClassName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppUtils.setStopped(getLocalClassName());
    }
}
