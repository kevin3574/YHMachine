package kzy.com.gyyengineer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 创建人：吴聪聪
 * 邮箱:cc112837@163.com
 * 导航页适配器
*/

public class GuideAdapter extends FragmentPagerAdapter {

    private List<Fragment> guideFragment;
    public GuideAdapter(FragmentManager fm, List<Fragment> guideFragment) {
        super(fm);
        this.guideFragment = guideFragment;
    }

    @Override
    public Fragment getItem(int i) {
        return guideFragment.get(i);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (guideFragment != null) {
            ret = guideFragment.size();
        }

        return ret;
    }
}
