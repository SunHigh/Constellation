package com.example.sh_pc.constellation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mcontext;
    private TextView birthday,tv_summary;
    private TextView tv_health;
    private TextView tv_luckyCol;
    private TextView tv_luckyNum;
    private TextView tv_Qfriend;
    private TextView tv_money;
    private TextView tv_all;
    private TextView tv_work;
    private TextView tv_love;
    private Button btn_query;
    private ConsteApi consteApi;
    private ConsteService consteService;
    private Button btn_birth,btn_confirm;
    private RelativeLayout layout_background;
    private TableLayout layout_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext = MainActivity.this;
        btn_birth = (Button)findViewById(R.id.btn_birth);
        tv_summary = (TextView) findViewById(R.id.consSummary);
        birthday = (TextView)findViewById(R.id.birthday);
        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_love = (TextView) findViewById(R.id.tv_love);
        tv_work = (TextView) findViewById(R.id.tv_work);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_health = (TextView) findViewById(R.id.jiankangtext);
        tv_luckyCol = (TextView) findViewById(R.id.xingyuncolor);
        tv_luckyNum = (TextView) findViewById(R.id.xingyunnumber);
        tv_Qfriend = (TextView) findViewById(R.id.Qfriend);
        layout_background = (RelativeLayout) findViewById(R.id.layout_background);
        layout_result = (TableLayout) findViewById(R.id.ll_result);
        btn_birth.setOnClickListener(this);

        consteApi = ConsteApi.getApi();
        consteService = consteApi.getService();

        layout_result.setVisibility(View.GONE);

    }

    //查询星座运势返回json并解析
    private void query(String conste) {
        String constellation = conste;
        if (constellation.isEmpty()){
            Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<ConsteResult> call = consteService.
                getResult(ConsteApi.Api_Key, constellation, "today");
        call.enqueue(new Callback<ConsteResult>() {
            @Override
            public void onResponse(Call<ConsteResult> call, Response<ConsteResult> response) {
                if (response.isSuccessful()){}
                ConsteResult result = response.body();
                if (result != null && result.getError_code() == 0){
                    tv_summary.setText("星座运势"+result.getSummary());
                    tv_health.setText(result.getHealth());
                    tv_luckyCol.setText(result.getColor());
                    tv_luckyNum.setText(result.getNumber()+"");
                    tv_Qfriend.setText(result.getQFriend());
                    tv_money.setText(result.getMoney());
                    tv_all.setText(result.getAll());
                    tv_work.setText(result.getWork());
                    tv_love.setText(result.getLove());
                }
            }

            @Override
            public void onFailure(Call<ConsteResult> call, Throwable t) {
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_birth:
                View view = LayoutInflater.from(mcontext)
                        .inflate(R.layout.pop_calendar,null,false);
                //因为已经载入了另一个Layout所以一定要用这里的view来初始化控件
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_selector);
                Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
                //定义一个popupwindow
                final PopupWindow popupWindow = new PopupWindow(view,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,true);
                //设置动画效果
                popupWindow.setAnimationStyle(R.anim.anim_pop);
                popupWindow.setTouchable(true);
                //如果返回true，则touch事件不被响应
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                popupWindow.setBackgroundDrawable(new ColorDrawable(0xaaaaddff));
                popupWindow.showAtLocation(view, Gravity.BOTTOM,0,170);

                //aquarius:
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
//                        layout_background.setVisibility(View.VISIBLE);
                        layout_result.setVisibility(View.VISIBLE);
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth()+1;
                        int day = datePicker.getDayOfMonth();
                        birthday.setText(year+"年 "+month+"月 "+day+"日");
                        String cons = star(month, day);
                        birthday.append(" 星座："+star(month,day));
                        switch (cons){
                            case "水瓶座":
                                Resources res1 = getResources();
                                Drawable drawable1 = res1.getDrawable(R.mipmap.aquarius);
                                layout_background.setBackgroundDrawable(drawable1);
                                break;

                            case "双鱼座":
                                Resources res2 = getResources();
                                Drawable drawable2 = res2.getDrawable(R.mipmap.pisces);
                                layout_background.setBackgroundDrawable(drawable2);
                                break;

                            case "白羊座":
                                Resources res3 = getResources();
                                Drawable drawable3 = res3.getDrawable(R.mipmap.aries);
                                layout_background.setBackgroundDrawable(drawable3);
                                break;

                            case "金牛座":
                                Resources res4 = getResources();
                                Drawable drawable4 = res4.getDrawable(R.mipmap.taurus);
                                layout_background.setBackgroundDrawable(drawable4);
                                break;

                            case "双子座":
                                Resources res5 = getResources();
                                Drawable drawable5 = res5.getDrawable(R.mipmap.gemini);
                                layout_background.setBackgroundDrawable(drawable5);
                                break;

                            case "巨蟹座":
                                Resources res6 = getResources();
                                Drawable drawable6 = res6.getDrawable(R.mipmap.cacer);
                                layout_background.setBackgroundDrawable(drawable6);
                                break;

                            case "狮子座":
                                Resources res7 = getResources();
                                Drawable drawable7 = res7.getDrawable(R.mipmap.leo);
                                layout_background.setBackgroundDrawable(drawable7);
                                break;

                            case "处女座":
                                Resources res8 = getResources();
                                Drawable drawable8 = res8.getDrawable(R.mipmap.virgo);
                                layout_background.setBackgroundDrawable(drawable8);
                                break;

                            case "天秤座":
                                Resources res9 = getResources();
                                Drawable drawable9 = res9.getDrawable(R.mipmap.libra);
                                layout_background.setBackgroundDrawable(drawable9);
                                break;

                            case "天蝎座":
                                Resources res10 = getResources();
                                Drawable drawable10 = res10.getDrawable(R.mipmap.scorpio);
                                layout_background.setBackgroundDrawable(drawable10);
                                break;

                            case "射手座":
                                Resources res11 = getResources();
                                Drawable drawable11 = res11.getDrawable(R.mipmap.sagittarius);
                                layout_background.setBackgroundDrawable(drawable11);
                                break;

                            case "摩羯座":
                                Resources res12 = getResources();
                                Drawable drawable12 = res12.getDrawable(R.mipmap.capricornus);
                                layout_background.setBackgroundDrawable(drawable12);
                                break;
                        }
                        query(cons);
                    }
                });
                //设置按钮点击后原先的背景不显示
//                layout_background.setVisibility(View.GONE);
                break;
        }
    }

    //由日期到星座的算法
    public static String star(int month, int day){
            String star = "";
            if (month == 1 && day >= 20 || month == 2 && day <= 18) {
                star = "水瓶座";
            }
            if (month == 2 && day >= 19 || month == 3 && day <= 20) {
                star = "双鱼座";
            }
            if (month == 3 && day >= 21 || month == 4 && day <= 19) {
                star = "白羊座";
            }
            if (month == 4 && day >= 20 || month == 5 && day <= 20) {
                star = "金牛座";
            }
            if (month == 5 && day >= 21 || month == 6 && day <= 21) {
                star = "双子座";
            }
            if (month == 6 && day >= 22 || month == 7 && day <= 22) {
                star = "巨蟹座";
            }
            if (month == 7 && day >= 23 || month == 8 && day <= 22) {
                star = "狮子座";
            }
            if (month == 8 && day >= 23 || month == 9 && day <= 22) {
                star = "处女座";
            }
            if (month == 9 && day >= 23 || month == 10 && day <= 22) {
                star = "天秤座";
            }
            if (month == 10 && day >= 23 || month == 11 && day <= 21) {
                star = "天蝎座";
            }
            if (month == 11 && day >= 22 || month == 12 && day <= 21) {
                star = "射手座";
            }
            if (month == 12 && day >= 22 || month == 1 && day <= 19) {
                star = "摩羯座";
            }

            return star;
    }
}
