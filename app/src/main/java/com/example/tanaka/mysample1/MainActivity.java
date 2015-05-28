package com.example.tanaka.mysample1;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends Activity {
    //前回入力された値
    //0:なし
    //1:数字
    //2:+,-
    //3:×,÷
    //4:×-,÷-
    static int judgeCnt = 0;
    static boolean numReset = false;
    List<String> num = new ArrayList<String>();
    List<String> operation = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("LifeCycle", "onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle putState){
        super.onSaveInstanceState(putState);
        Log.d("LifeCycle", "onSaveInstanceState()");

        TextView tv = (TextView)findViewById(R.id.textView);

        putState.putString("TextView", tv.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle setState){
        super.onRestoreInstanceState(setState);

        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText(setState.getString("TextView"));

    }

    public void toast(View view){
        Random random = new Random();
        int ran = random.nextInt(10);
        String toastStr = "";
        switch(ran % 10){
            case 0:
                toastStr ="よく俺を見つけたな";
                break;
            case 1:
                toastStr = "つhttps://www.hellowork.go.jp/\nようこそハローワークへ！ハハッ！";
                break;
            case 2:
                toastStr = "電卓の番人は激務薄給超絶ブラック";
                break;
            case 3:
                toastStr = "間違え探し\nアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズ" +
                        "アライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズ" +
                        "ヤキサバアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズアライズ";
                break;
            case 4:
                toastStr = "0で割るなよ！？絶対だぞ！！！";
                break;
            case 5:
                toastStr = "この電卓ｗｗ俺が処理してんすよｗｗｗｗ";
                break;
            case 6:
                toastStr = "定時退社？？？ゆとりかよ";
                break;
            case 7:
                toastStr = "電卓の処理だるすぎワロタｗｗｗ";
                break;
            case 8:
                toastStr = "atHomeで圧倒的成長";
                break;
            case 9:
                toastStr = "クソネミ";
                break;
        }
        Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show();
    }
    public void clickNum(View view){
        TextView tv = (TextView)findViewById(R.id.textView);
        Button b = (Button)view;

        String addNum = b.getText().toString();
        String oldText = tv.getText().toString();
        oldText = clickNumReset(errorChecker(oldText));
        //現在のテキスト内容が０でかつ入力が０ならそのまま
        String newText;
        if(oldText.length() > 0){
            divideString(oldText);
            if(num.get(num.size()-1).equals("0")) {
                newText = oldText.substring(0, oldText.length()-1) + addNum;
            }else{
                newText = oldText + addNum;
            }
            num.clear();
            operation.clear();
        } else {
            newText = oldText + addNum;
        }
            tv.setText(newText);
        judgeCnt = 1;
    }

    public void operation(View view) {
        TextView tv = (TextView) findViewById(R.id.textView);
        Button b = (Button) view;

        String operation = b.getText().toString();
        String oldText = tv.getText().toString();
        String newText;
        oldText = errorChecker(oldText);
        numReset = false;

        if(oldText.length() < 57) {
            newText = operationChecker(oldText, operation);
        }else{
            newText = oldText;
        }
        tv.setText(newText);
    }

    public void equals(View view){
        TextView tv = (TextView) findViewById(R.id.textView);
        Button b = (Button) view;
        String fixNum = tv.getText().toString();

        if(fixNum.length() > 0) {
            String regex = "[0-9]";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(fixNum.substring(fixNum.length() - 1));

            if (judgeCnt == 1 && m.find()) {
                String summary = equalsResult(tv.getText().toString());

                //ここでリセットしとかないと2回目以降の計算やばい
                num.clear();
                operation.clear();
                numReset = true;

                int cnt;
                fixNum = summary;
                if ((cnt = fixNum.indexOf(".")) != -1) {
                    for (int i = fixNum.length() - 1; i >= cnt; i--) {
                        char ch = fixNum.charAt(i);
                        if (ch == '0' || ch == '.') {
                            fixNum = fixNum.substring(0, fixNum.length() - 1);
                        } else {
                            break;
                        }
                    }
                }
                judgeCnt = 1;
            }
        }
        tv.setText(fixNum);

    }

    public void period(View view) {
        TextView tv = (TextView) findViewById(R.id.textView);
        Button b = (Button) view;
        String oldText = tv.getText().toString();
        oldText = clickNumReset(errorChecker(oldText));
        String newText = oldText;

        if(periodChecker(oldText)){
            if(oldText.length() > 0){
                divideString(oldText);
                String nowNum = num.get(num.size()-1);
                if(nowNum.length() > 0 && !nowNum.substring(nowNum.length()-1).equals("-")) {
                    newText = oldText + '.';
                }else{
                    newText = oldText + "0.";
                }
                num.clear();
                operation.clear();
            } else {
                newText = "0.";
            }

            judgeCnt = 5;
        }
        tv.setText(newText);
    }

    public void clear(View view){
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText("");
        num.clear();
        operation.clear();
        judgeCnt = 0;
    }

    public String errorChecker(String text){
        if(text.equals("ERROR")){
            text = "";
            judgeCnt = 0;
        }

        return text;
    }

    //=で計算直後(きちんと計算できた場合のみ)num押されると全削除
    public String clickNumReset(String text){
        text = (numReset)? "": text;
        numReset = false;

        return text;
    }

    public boolean periodChecker(String str){
        boolean judge = true;
        char ch;
        for(int i = str.length()-1; i >= 0; i--) {
            ch = str.charAt(i);
            if (ch == '+' || ch == '-' || ch == '×' || ch == '÷') {
                break;
            }
            if(ch == '.'){
                judge = false;
            }
        }
        return judge;
    }

    public String operationChecker(String text, String operation){
        String newText = "";
        operation = operation.replace("＋", "+");
        operation = operation.replace("－", "-");
        switch (judgeCnt){
            case 0:
                newText = text;
                //特殊　前に数字がない状態で－だけあり、文字が入力された場合
                if(text.length() > 0) {
                    newText = text.substring(0, text.length() -1 );
                }
                //数字がない場合に－が入力された場合
                if(operation.equals("-")){
                    newText += operation;
                }
                //あとで０に
                judgeCnt = 6;
                break;

            case 1:
                newText = text + operation;
                break;

            case 2:
                newText = text.substring(0, text.length() -1 ) +operation;
                break;

            case 3:
                if(operation.equals("-")){
                    newText = text + operation;
                    judgeCnt = 7; //あとで４に！
                }else{
                    newText = text.substring(0, text.length() -1 ) +operation;
                }
                break;

            case 4:
                newText = text.substring(0, text.length() -2 ) +operation;
                break;
            case 5:
                newText = text;
                break;
        }

        //judgeCnt4以下(通常ルート)の場合
        if(judgeCnt <=4) {
            if (operation.equals("+") || operation.equals("-")) {
                judgeCnt = 2;
            } else if (operation.equals("×") || operation.equals("÷")) {
                judgeCnt = 3;
            } else {
                Log.e("non", operation);
            }
        }else { //非正規ルート
            //×-,÷-
            judgeCnt = (judgeCnt != 7) ? judgeCnt : 4;
            //なし、-のみ
            judgeCnt = (judgeCnt != 6) ? judgeCnt : 0;
        }
        Log.v("judge", ""+judgeCnt);

        return newText;
    }


    //@brief		渡された計算式の結果を返す
    //
    //@param[String]	str 計算式
    //					例:－1.25＋2＋3.2×-4÷5
    //
    //@return[double]	計算結果
    //					例:-1.81


    public String equalsResult(String str){
        divideString(str);

        for ( int i = 0; i < operation.size(); i++ ) {
            if(operation.get(i).equals("*") || operation.get(i).equals("/")){
                BigDecimal num1 = new BigDecimal(num.get(i));
                BigDecimal num2 = new BigDecimal(num.get(i + 1));
                String result;
                if(operation.get(i).equals("*")){
                    result = num1.multiply(num2).toString();
                }else{
                    Pattern p = Pattern.compile("^[0](\\.)?0*");
                    Matcher m = p.matcher(num2.toString());
                    if(m.find()){
                        operation.clear();
                        result = "ERROR";
                        num.set(0, result);
                        break;

                    }else if(num1.toString().equals("0")) {
                        result = "0";
                    }else {
                        result = num1.divide(num2, 10, BigDecimal.ROUND_HALF_UP).toString();
                    }
                }
                num.remove(i+1);
                num.set(i, result);
                operation.remove(i);
                i--;
            }
        }

        for ( int i = 0; i < operation.size(); i++ ) {
            BigDecimal num1 = new BigDecimal(num.get(i));
            BigDecimal num2 = new BigDecimal(num.get(i + 1));
            BigDecimal result;
            if(operation.get(i).equals("+")){
                result = num1.add(num2);
            }else{
                result = num1.subtract(num2);
            }
            num.remove(i+1);
            num.set(i, result.toString());
            operation.remove(i);
            i--;
        }

        return num.get(0);
    }

    public void divideString(String str) {
        str = str.replace("＋", "+");
        str = str.replace("－", "-");
        str = str.replace("×", "*");
        str = str.replace("÷", "/");

        Pattern pattern = Pattern.compile("(-?[0-9|.]*)(\\+|-|\\*|/)?");
        Matcher m = pattern.matcher(str);

        while (m.find()) {
            if (null == m.group(2)) {
                num.add(m.group(1));
                break;
            }
            for (int i = 1; i <= m.groupCount(); i++) {
                if (i % 2 == 0) {
                    operation.add(m.group(i));
                } else {
                    num.add(m.group(i));
                }
            }
        }
    }
}
