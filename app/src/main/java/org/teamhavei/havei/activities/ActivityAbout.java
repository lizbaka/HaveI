package org.teamhavei.havei.activities;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.drakeet.about.AbsAboutActivity;
import com.drakeet.about.Card;
import com.drakeet.about.Category;
import com.drakeet.about.Contributor;
import com.drakeet.about.License;

import org.teamhavei.havei.BuildConfig;
import org.teamhavei.havei.R;

import java.util.List;

public class ActivityAbout extends AbsAboutActivity {

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityAbout.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreateHeader(@NonNull ImageView icon, @NonNull TextView slogan, @NonNull TextView version) {
        icon.setImageResource(R.mipmap.havei_launcher);
        slogan.setText(getString(R.string.app_introduction));
        version.setText("v" + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onItemsCreated(@NonNull List<Object> items) {
        items.add(new Category("介绍"));
        items.add(new Card(getString(R.string.app_introduction_detailed)));

        items.add(new Category("设计与开发"));
        items.add(new Contributor(R.drawable.img_ava_lizbaka, "lizbaka", "开发", "https://www.github.com/lizbaka"));
        items.add(new Contributor(R.drawable.img_ava_wufisher, "Wu_Fisher", "开发"));
        items.add(new Contributor(R.drawable.img_ava_tsuki, "tsuki", "开发，界面设计和应用图标设计","https://gitee.com/linkingsweety"));
        items.add(new Contributor(R.drawable.img_ava_eternal, "ethereal", "开发，界面设计", "https://gitee.com/etherealtsq"));

        items.add(new Category("联系我们"));
        items.add(new Card("email: lizbaka123@outlook.com"));

        items.add(new Category("感谢"));
        items.add(new Card("图标来自于iconfont: https://www.iconfont.cn/"));

        items.add(new Category("开源许可证"));
        items.add(new License("androidx", "Google", License.APACHE_2, "https://source.android.com"));
        items.add(new License("ThreeTenABP", "Jake Wharton", License.APACHE_2, "https://github.com/JakeWharton/ThreeTenABP"));
        items.add(new License("Material Calendar View", "Prolific Interactive", License.MIT, "https://github.com/prolificinteractive/material-calendarview"));
        items.add(new License("MultiType", "drakeet", License.APACHE_2, "https://github.com/drakeet/MultiType"));
        items.add(new License("about-page", "drakeet", License.APACHE_2, "https://github.com/drakeet/about-page"));
        items.add(new License("okhttp", "square", License.APACHE_2, "https://github.com/square/okhttp"));
        items.add(new License("MPAndroidChart", "PhilJay", License.APACHE_2, "https://github.com/PhilJay/MPAndroidChart"));
        items.add(new License("HITSZ Assistant", "StupidTree", License.MIT, "https://github.com/StupidTrees/HITSZ-Assistant"));
    }
}
