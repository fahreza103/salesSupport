package id.co.myrepublic.salessupport.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.animation.Animation;

import id.co.myrepublic.salessupport.constant.AppConstant;

public class GlobalVariables {

    private static GlobalVariables instance = new GlobalVariables();
    private SharedPreferences sharedPreferences;
    private Animation fadeInAnim;
    private Animation fadeOutAnim;
    private Animation popupAnim;
    private Animation downTopAnim;
    private Animation TopDownAnim;
    private Animation expandAnim;
    private Animation LeftRightAnim;

    protected GlobalVariables() {}

    public static GlobalVariables getInstance() {
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setSharedPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(AppConstant.SESSION_KEY,Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key,value).commit();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key,defaultValue);
    }

    public String getSessionKey() {
        if(sharedPreferences != null) {
            return sharedPreferences.getString(AppConstant.COOKIE_SESSION_KEY,null);
        }
        return null;
    }

    public void clearSession() {
        sharedPreferences.edit().clear().commit();
    }

    public Animation getFadeInAnim() {
        return fadeInAnim;
    }

    public void setFadeInAnim(Animation fadeInAnim) {
        this.fadeInAnim = fadeInAnim;
    }

    public Animation getFadeOutAnim() {
        return fadeOutAnim;
    }

    public void setFadeOutAnim(Animation fadeOutAnim) {
        this.fadeOutAnim = fadeOutAnim;
    }

    public Animation getPopupAnim() {
        return popupAnim;
    }

    public void setPopupAnim(Animation popupAnim) {
        this.popupAnim = popupAnim;
    }

    public Animation getDownTopAnim() {
        return downTopAnim;
    }

    public void setDownTopAnim(Animation downTopAnim) {
        this.downTopAnim = downTopAnim;
    }

    public Animation getTopDownAnim() {
        return TopDownAnim;
    }

    public void setTopDownAnim(Animation topDownAnim) {
        TopDownAnim = topDownAnim;
    }

    public Animation getExpandAnim() {
        return expandAnim;
    }

    public void setExpandAnim(Animation expandAnim) {
        this.expandAnim = expandAnim;
    }

    public Animation getLeftRightAnim() {
        return LeftRightAnim;
    }

    public void setLeftRightAnim(Animation leftRightAnim) {
        LeftRightAnim = leftRightAnim;
    }
}
