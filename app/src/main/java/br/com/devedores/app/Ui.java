package br.com.devedores.app;
import android.content.Context;import android.graphics.Color;import android.graphics.drawable.GradientDrawable;import android.view.View;import android.widget.*;
public class Ui {
 public static int dp(Context c,int n){return (int)(n*c.getResources().getDisplayMetrics().density+.5f);} 
 public static TextView text(Context c,String s,int sp){TextView v=new TextView(c);v.setText(s);v.setTextSize(sp);v.setTextColor(Color.WHITE);v.setPadding(dp(c,10),dp(c,8),dp(c,10),dp(c,8));return v;}
 public static Button btn(Context c,String s){Button b=new Button(c);b.setText(s);b.setTextColor(Color.BLACK);b.setAllCaps(false);b.setTextSize(15);GradientDrawable g=new GradientDrawable();g.setColor(Color.rgb(244,197,66));g.setCornerRadius(dp(c,14));b.setBackground(g);b.setPadding(dp(c,12),0,dp(c,12),0);return b;}
 public static EditText field(Context c,String hint){EditText e=new EditText(c);e.setHint(hint);e.setTextColor(Color.WHITE);e.setHintTextColor(Color.rgb(170,170,170));e.setSingleLine(false);e.setPadding(dp(c,12),dp(c,10),dp(c,12),dp(c,10));GradientDrawable g=new GradientDrawable();g.setColor(Color.rgb(34,34,34));g.setCornerRadius(dp(c,12));g.setStroke(dp(c,1),Color.rgb(70,70,70));e.setBackground(g);return e;}
 public static LinearLayout col(Context c){LinearLayout l=new LinearLayout(c);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(dp(c,16),dp(c,10),dp(c,16),dp(c,20));l.setBackgroundColor(Color.rgb(17,17,17));return l;}
 public static void gap(Context c,LinearLayout p,int h){Space s=new Space(c);p.addView(s,new LinearLayout.LayoutParams(1,dp(c,h)));}
}
