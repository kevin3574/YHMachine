package kzy.com.gyyengineer.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
	/**
	 * 首字母转换
	 * 创建人：吴聪聪
	 * 邮箱:cc112837@163.com
	 * 创建时间：2017/3/3 13:12
	*/
	public static String tofirstLowerCase(String str) {

		if (str != null && str.length() > 0) {
			return str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
		} else if (str != null && str.length() == 0) {
			return str.toLowerCase();
		} else {
			return str;
		}

	}

	// Toast
	public static void initToast(Context c, String title) {
		try {
			Toast.makeText(c, title, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	
	/**
	 * 验证输入的邮箱格式是否符合
	 * 
	 * @param email
	 * @return 是否合法
	 */
	public static boolean emailFormat(String email) {
		boolean tag = true;
		final String pattern1 = "^([a-zA-Z0-9]){1}([a-zA-Z0-9_\\.-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}
}

