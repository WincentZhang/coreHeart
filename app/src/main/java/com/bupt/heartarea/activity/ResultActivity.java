package com.bupt.heartarea.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bupt.heartarea.R;
import com.bupt.heartarea.bean.ResponseBean;
import com.bupt.heartarea.ui.CircleIndicator;
import com.bupt.heartarea.ui.IndicatorItem;
import com.bupt.heartarea.ui.LineIndicator;
import com.bupt.heartarea.ui.NumberAnimTextView;
import com.bupt.heartarea.utils.GlobalData;
import com.google.gson.Gson;
import com.zhouyou.view.segmentedbar.RectThumbSegmentedBarView;
import com.zhouyou.view.segmentedbar.Segment;

import net.lemonsoft.lemonbubble.LemonBubble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends Activity implements View.OnClickListener {

    CircleIndicator ci1;
    LineIndicator mLiHeartRateProgress;
    LineIndicator mLiBloodOxygenProgress;
    NumberAnimTextView mAnimTvBloodPressureHigh;
    NumberAnimTextView mAnimTvBloodPressureLow;
    ImageView mCameraView;
    @BindView(R.id.tvHeartRateTip)
    TextView mTvHeartRateTip;

    @BindView(R.id.tvAfTip)
    TextView mTvAfTip;

    @BindView(R.id.tvBloodOxyTip)
    TextView mTvBloodOxyTip;

    @BindView(R.id.tvFatigueTip)
    TextView mTvFatigueTip;

    @BindView(R.id.rtsbv_af)
    RectThumbSegmentedBarView mSbvAf;


    private static final String TAG = "ResultActivity";

    private int mHeartRate = 0;
    private int mFatigue = 0;
    private int mBloodOxygen = 0;
    private int mBloodPressureHigh = 0;
    private int mBloodPressureLow = 0;
    private double mAf = 0;
    private int mBloodPressureLowFeedBack = 0;
    private int mBloodPressureHighFeedBack = 0;
    int mMiddleColor;
    int mLowColor;
    int mHighColor;
    int mFeedBackValue = 1;
    int mSuccess = 0;
    String mAlert;
    Button mBtnFeedBackYes;
    Button mBtnFeedBackNo;
    LinearLayout mLlFeedBackAll;
    private static final String URL_FEEDBACK = GlobalData.URL_HEAD + "/detect3/SelfstatusServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cicle_view);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        mHeartRate = bundle.getInt("heart_rate");
        mFatigue = bundle.getInt("pressure");
        mBloodOxygen = bundle.getInt("blood_oxygen");
        mBloodPressureHigh = bundle.getInt("blood_pressure_high");
        mBloodPressureLow = bundle.getInt("blood_pressure_low");
        mAf = bundle.getDouble("af");
        Log.d(TAG + "af", mAf + "");
        mAlert = bundle.getString("alert");

        initColor();
        initView();
        initIndicator();
        setHeartRateProgress(mHeartRate);
        setBloodOxygenProgress(mBloodOxygen);
        initAfSegmentBarView(mAf);
        mCameraView = (ImageView) findViewById(R.id.ivCamera);
        mCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,questionnaireSurveyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("measure_data", getIntent().getSerializableExtra("measure_data"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mLiHeartRateProgress = (LineIndicator) findViewById(R.id.li_progress_heart_rate);
        mLiBloodOxygenProgress = (LineIndicator) findViewById(R.id.li_progress_blood_oxygen);
        ci1 = (CircleIndicator) findViewById(R.id.ci_1);

        mBtnFeedBackYes = (Button) findViewById(R.id.btn_result_yes);
        mBtnFeedBackYes.setOnClickListener(this);

        mBtnFeedBackNo = (Button) findViewById(R.id.btn_result_no);
        mBtnFeedBackNo.setOnClickListener(this);

        mLlFeedBackAll = (LinearLayout) findViewById(R.id.ll_result_feedback_all);

        mAnimTvBloodPressureHigh = (NumberAnimTextView) findViewById(R.id.animtv_blood_presssure_high);
        mAnimTvBloodPressureLow = (NumberAnimTextView) findViewById(R.id.animtv_blood_presssure_low);
        mAnimTvBloodPressureHigh.setNumberString(String.valueOf(mBloodPressureHigh));
        mAnimTvBloodPressureLow.setNumberString(String.valueOf(mBloodPressureLow));

    }

    private void initColor() {
        mMiddleColor = getResources().getColor(R.color.pressure_middle);
        mLowColor = getResources().getColor(R.color.pressure_low);
        mHighColor = getResources().getColor(R.color.pressure_high);
    }


    private void setHeartRateProgress(int value) {
        String leftAlert = "慢";
        String leftContent = "0";
        String rightAlert = "快";
        String rightContent = "150";
        if (value > 100) {
            mLiHeartRateProgress.setProgressColor(mHighColor);
            mLiHeartRateProgress.setIndicatorBackground(mHighColor);
        } else {
            if (value > 65) {
                mLiHeartRateProgress.setProgressColor(mMiddleColor);
                mLiHeartRateProgress.setIndicatorBackground(mMiddleColor);

            } else {
                mLiHeartRateProgress.setProgressColor(mLowColor);
                mLiHeartRateProgress.setIndicatorBackground(mLowColor);

            }
        }
        mLiHeartRateProgress.setContent(leftAlert, leftContent, rightAlert, rightContent);
        mLiHeartRateProgress.setIndicator(40, 150, value, value + " BMP");
    }

    /**
     * 绘制 血氧值 进度条
     *
     * @param value
     */
    private void setBloodOxygenProgress(int value) {
        String leftAlert = "低";
        String leftContent = "90";
        String rightAlert = "高";
        String rightContent = "100";
        if (value > 97) {
            mLiBloodOxygenProgress.setProgressColor(mHighColor);
            mLiBloodOxygenProgress.setIndicatorBackground(mHighColor);
        } else {
            if (value > 94) {
                mLiBloodOxygenProgress.setProgressColor(mMiddleColor);
                mLiBloodOxygenProgress.setIndicatorBackground(mMiddleColor);

            } else {
                mLiBloodOxygenProgress.setProgressColor(mLowColor);
                mLiBloodOxygenProgress.setIndicatorBackground(mLowColor);

            }
        }
        mLiBloodOxygenProgress.setContent(leftAlert, leftContent, rightAlert, rightContent);
        mLiBloodOxygenProgress.setIndicator(90, 100, value, value + " %");
    }


    /**
     * 疲劳度仪表盘
     */
    private void initIndicator() {

        List<IndicatorItem> dividerIndicator = new ArrayList<>();
        IndicatorItem item1 = new IndicatorItem();
        item1.start = 0;
        item1.end = 30;
        item1.value = "过低";
        item1.color = mLowColor;
        dividerIndicator.add(item1);

        IndicatorItem item2 = new IndicatorItem();
        item2.start = 30;
        item2.end = 70;
        item2.value = "正常";
        item2.color = mMiddleColor;
        dividerIndicator.add(item2);

        IndicatorItem item3 = new IndicatorItem();
        item3.start = 70;
        item3.end = 100;
        item3.value = "过高";
        item3.color = mHighColor;
        dividerIndicator.add(item3);


        String title = "疲劳度";
        String content = mFatigue + "";
        String unit = "";
        String alert = "愉快的心情";
        if (mFatigue < 30) {

            ci1.setContentColor(mLowColor, mLowColor);
            ci1.setmAlertColor(mLowColor);

        } else {
            if (mFatigue < 70) {
                ci1.setContentColor(mMiddleColor, mMiddleColor);
                ci1.setmAlertColor(mMiddleColor);
            } else {
                ci1.setContentColor(mHighColor, mHighColor);
                ci1.setmAlertColor(mHighColor);
            }
        }
        ci1.setContent(title, content, unit, mAlert);
        ci1.setIndicatorValue(dividerIndicator, mFatigue);
    }

    /**
     * 房颤结果控件
     */
    private void initAfSegmentBarView(double value) {
        int afValue = (int) value;
        if (afValue >= 2) {
            afValue = 2;
        }
        ArrayList<Segment> segments = new ArrayList<>();
        Segment segment = new Segment("", "低风险", getResources().getColor(R.color.normal_green));
        segments.add(segment);
        Segment segment2 = new Segment("", "中风险", getResources().getColor(R.color.normal_yellow));
        segments.add(segment2);
        Segment segment3 = new Segment("", "高风险", getResources().getColor(R.color.normal_red));
        segments.add(segment3);
        mSbvAf.setValueSegment(afValue);
        mSbvAf.setShowDescriptionText(true);
        mSbvAf.setSegments(segments);
    }

    /**
     * 显示带有radiobutton的对话框
     */
    private void showRadioButtonDialog() {
        View radiobuttonview;       //使用view来接入方法写出的dialog，方便相关初始化
        LayoutInflater inflater;        //引用自定义dialog布局
        inflater = LayoutInflater.from(ResultActivity.this);
        radiobuttonview = (LinearLayout) inflater.inflate(R.layout.radiogroup_feedback, null);
        final RadioGroup radiogroup = (RadioGroup) radiobuttonview.findViewById(R.id.rg_feedback);
        final RadioButton radioButton1 = (RadioButton) radiobuttonview.findViewById(R.id.rb_pressure_1);
        final RadioButton radioButton2 = (RadioButton) radiobuttonview.findViewById(R.id.rb_pressure_2);
        final RadioButton radioButton3 = (RadioButton) radiobuttonview.findViewById(R.id.rb_pressure_3);
        final RadioButton radioButton4 = (RadioButton) radiobuttonview.findViewById(R.id.rb_pressure_4);
        final RadioButton radioButton5 = (RadioButton) radiobuttonview.findViewById(R.id.rb_pressure_5);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_pressure_1:
                        mFeedBackValue = 1;
                        break;
                    case R.id.rb_pressure_2:
                        mFeedBackValue = 2;
                        break;
                    case R.id.rb_pressure_3:
                        mFeedBackValue = 3;
                        break;
                    case R.id.rb_pressure_4:
                        mFeedBackValue = 4;
                        break;
                    case R.id.rb_pressure_5:
                        mFeedBackValue = 5;
                        break;
                }

            }
        });
        final EditText etBloodPressureHigh = (EditText) radiobuttonview.findViewById(R.id.et_feedback_bp_high);
        final EditText etBloodPressureLow = (EditText) radiobuttonview.findViewById(R.id.et_feedback_bp_low);


        radiogroup.check(R.id.rb_pressure_3);

        new AlertDialog.Builder(ResultActivity.this)
                .setView(radiobuttonview)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String high_value = etBloodPressureHigh.getText().toString().trim();
                        String low_value = etBloodPressureLow.getText().toString().trim();
                        if (!high_value.isEmpty())
                            mBloodPressureHighFeedBack = Integer.valueOf(high_value);
                        if (!low_value.isEmpty())
                            mBloodPressureLowFeedBack = Integer.valueOf(low_value);
                        if (mBloodPressureHighFeedBack > 300 || mBloodPressureHighFeedBack < 0
                                || mBloodPressureLowFeedBack > 300 || mBloodPressureLowFeedBack < 0
                                || mBloodPressureLowFeedBack >= mBloodPressureHighFeedBack) {
                            Toast.makeText(ResultActivity.this, "请输入合法的数值", Toast.LENGTH_SHORT).show();
                            showRadioButtonDialog();
                        } else {
                            feedBack(0, mFeedBackValue);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @OnClick({R.id.ivHeartRateTip, R.id.ivBloodOxyTip, R.id.ivAfTip, R.id.ivFatigueTip})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_result_yes:
                feedBack(1, 0);
                break;
            case R.id.btn_result_no:
                showRadioButtonDialog();
                break;
            case R.id.ivHeartRateTip:
                if (mTvHeartRateTip.getVisibility() == View.GONE) {
                    mTvHeartRateTip.setVisibility(View.VISIBLE);
                } else {
                    mTvHeartRateTip.setVisibility(View.GONE);
                }
                break;
            case R.id.ivBloodOxyTip:
                if (mTvBloodOxyTip.getVisibility() == View.GONE) {
                    mTvBloodOxyTip.setVisibility(View.VISIBLE);
                } else {
                    mTvBloodOxyTip.setVisibility(View.GONE);
                }
                break;
            case R.id.ivAfTip:
                if (mTvAfTip.getVisibility() == View.GONE) {
                    mTvAfTip.setVisibility(View.VISIBLE);
                } else {
                    mTvAfTip.setVisibility(View.GONE);
                }
                break;
            case R.id.ivFatigueTip:
                if (mTvFatigueTip.getVisibility() == View.GONE) {
                    mTvFatigueTip.setVisibility(View.VISIBLE);
                } else {
                    mTvFatigueTip.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void setAllTipGone() {
        mTvAfTip.setVisibility(View.GONE);
        mTvBloodOxyTip.setVisibility(View.GONE);
        mTvHeartRateTip.setVisibility(View.GONE);
    }

    /**
     * 向服务器发送用户反馈信息
     *
     * @param success
     * @param status
     */
    private void feedBack(final int success, final int status) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, URL_FEEDBACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();

                        ResponseBean responseBean = gson.fromJson(s, ResponseBean.class);


                        if (responseBean != null) {
                            if (responseBean.getCode() == 0) {
//                                Toast.makeText(ResultActivity.this, responseBean.getMsg(), Toast.LENGTH_LONG).show();

                                LemonBubble.getRightBubbleInfo()// 增加无限点语法修改bubbleInfo的特性
                                        .setTitle("提交成功")
                                        .setTitleFontSize(12)// 修改字体大小
                                        .setTitleColor(Color.parseColor("#a269af73"))
                                        .setMaskColor(Color.argb(100, 0, 0, 0))// 修改蒙版颜色
                                        .show(ResultActivity.this, 2000);
                                mLlFeedBackAll.setVisibility(View.GONE);


                            } else {
                                Toast.makeText(ResultActivity.this, responseBean.getMsg(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(ResultActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("请求失败", volleyError.getMessage(), volleyError);
                Toast.makeText(ResultActivity.this, "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<>();
                map.put("userid", GlobalData.userid);
                map.put("success", String.valueOf(success));
                map.put("status", String.valueOf(status));
                map.put("blood_pressure_high_feedback", String.valueOf(mBloodPressureHighFeedBack));
                map.put("blood_pressure_low_feedback", String.valueOf(mBloodPressureLowFeedBack));
                System.out.println(map.toString());

                return map;
            }
        };
        requestQueue.add(request);

    }
}
