package net.lzzy.practiceapi.fragments.student;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import net.lzzy.practiceapi.R;
import net.lzzy.practiceapi.fragments.BaseFragment;
import net.lzzy.practiceapi.models.QuestionType;
import net.lzzy.practiceapi.models.UserCookies;
import net.lzzy.practiceapi.models.student.SOption;
import net.lzzy.practiceapi.models.student.SQuestion;
import net.lzzy.practiceapi.utils.AppUtils;

import java.util.List;

/**
 * @author lzzy_gxy on 2019/4/30.
 * Description:
 */
public class QuestionFragment extends BaseFragment {
    private static final String ARG_QUESTION_ID = "argQuestionId";
    private static final String ARG_POS = "argPos";
    private static final String ARG_IS_COMMITTED = "argIsCommitted";
    private SQuestion question;
    private int pos;
    private boolean isCommitted;
    private TextView tvType;
    private ImageButton imgFavorite;
    private TextView tvContent;
    private RadioGroup rgOptions;
    private boolean isMulti = false;

    public static QuestionFragment newInstance(String questionId, int pos, boolean isCommitted) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION_ID, questionId);
        args.putInt(ARG_POS, pos);
        args.putBoolean(ARG_IS_COMMITTED, isCommitted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt(ARG_POS);
            isCommitted = getArguments().getBoolean(ARG_IS_COMMITTED);
            question = AppUtils.getsQuestions().get(pos);
        }
    }

    @Override
    protected void populate() {
        //find views
        initViews();
        //显示题目内容
        displayQuestion();
        //生成选项
        generateOptions();
    }

    private void generateOptions() {
        List<SOption> options = question.getOptions();
        for (SOption option : options) {
            CompoundButton btn = isMulti ? new CheckBox(getContext()) : new RadioButton(getContext());
            String content = option.getLabel() + "." + option.getContent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btn.setButtonTintList(ColorStateList.valueOf(Color.GRAY));
            }
            btn.setText(content);
            btn.setEnabled(!isCommitted);
            btn.setOnCheckedChangeListener((buttonView, isChecked) ->
                    UserCookies.getInstance().changeOptionState(option,isChecked,isMulti));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //btn.setTextAppearance(R.style.NormalText);
            }
            rgOptions.addView(btn);
            //勾选，到文件中找是否存在该选项的id，存在则勾选
            boolean shouldCheck = UserCookies.getInstance().isOptionSelected(option);
            if (isMulti){
                btn.setChecked(shouldCheck);
            } else if (shouldCheck){
                rgOptions.check(btn.getId());
            }
            if (isCommitted && option.getIsAnswer()==1){
               // btn.setTextColor(ContextCompat.getColor(getContext(),R.color.colorGreen));
            }
        }
    }

    private void displayQuestion() {
        isMulti = QuestionType.getInstance(question.getQuestionType()) == QuestionType.MULTI_CHOICE;
        //int label = pos + 1;
        String qType = question.getNumber() + "." + QuestionType.getInstance(question.getQuestionType()).toString();
        tvType.setText(qType);
        tvContent.setText(question.getContent());
        /*int starId = FavoriteFactory.getInstance().isQuestionStarred(question.getId().toString()) ?
                android.R.drawable.star_on : android.R.drawable.star_off;
        imgFavorite.setImageResource(starId);
        imgFavorite.setOnClickListener(v -> switchStar());*/
    }

    private void switchStar() {
       /* FavoriteFactory factory = FavoriteFactory.getInstance();
        if (factory.isQuestionStarred(question.getId().toString())) {
            factory.cancelStarQuestion(question.getId());
            imgFavorite.setImageResource(android.R.drawable.star_off);
        } else {
            factory.starQuestion(question.getId());
            imgFavorite.setImageResource(android.R.drawable.star_on);
        }*/
    }

    private void initViews() {
        tvType = find(R.id.fragment_question_tv_type);
        imgFavorite = find(R.id.fragment_question_img_favorite);
        tvContent = find(R.id.fragment_question_tv_content);
        rgOptions = find(R.id.fragment_question_option_container);
        if (isCommitted){
            rgOptions.setOnClickListener(v ->
                    new AlertDialog.Builder(getContext())
                            .setMessage(question.getAnalysis())
                            .show());
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_question;
    }

}
