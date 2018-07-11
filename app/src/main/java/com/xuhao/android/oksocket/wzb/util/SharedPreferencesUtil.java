package com.xuhao.android.oksocket.wzb.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.xuhao.android.oksocket.MyApplication;


/**
 * @author wzb<wangzhibin_x@qq.com>
 * @date May 8, 2017 2:47:08 PM
 */
public class SharedPreferencesUtil {

	/** SharedPreferences对象 */
	private SharedPreferences sp;

	/** SharedPreferencesUtil权限：本程序读写 */
	public static final int PRIVATE = Context.MODE_PRIVATE;
	/** SharedPreferencesUtil权限：其他程序可读 */
	public static final int OTHER_READ = Context.MODE_WORLD_READABLE;
	/** SharedPreferencesUtil权限：其他程序可读写 */
	public static final int OTHER_READ_AND_WRITE = Context.MODE_WORLD_WRITEABLE;

	/**
	 * 构造方法,获得SharedPreferences对象.
	 * 
	 * @param name
	 *            存储文件名称.
	 * @param mode
	 *            存储权限.
	 * @param context
	 *            上下文环境.
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:35:42
	 * @updateTime 2014年7月21日,下午2:35:42
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public SharedPreferencesUtil(String name, int mode, Context context) {
		sp = context.getSharedPreferences(name, mode);
	}

	/**
	 * 获取sp对象
	 * 
	 * @param name
	 * @param mode
	 * @return
	 * @version 1.0
	 * @createTime 2015年2月4日,上午11:19:41
	 * @updateTime 2015年2月4日,上午11:19:41
	 * @createAuthor 王治粮
	 * @updateAuthor 王治粮
	 * @updateInfo
	 * 
	 */
	public static SharedPreferencesUtil getSpUtil(String name, int mode) {
		return new SharedPreferencesUtil(name, mode, MyApplication.CONTEXT);
	}

	/**
	 * 写入.
	 * 
	 * @param key
	 *            键.
	 * @param value
	 *            值(boolean).
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:37:18
	 * @updateTime 2014年7月21日,下午2:37:18
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public void set(String key, boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}

	/**
	 * 写入.
	 * 
	 * @param key
	 *            键.
	 * @param value
	 *            值(float).
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:39:00
	 * @updateTime 2014年7月21日,下午2:39:00
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public void set(String key, float value) {
		sp.edit().putFloat(key, value).commit();
	}

	/**
	 * 写入.
	 * 
	 * @param key
	 *            键.
	 * @param value
	 *            值(int).
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:39:14
	 * @updateTime 2014年7月21日,下午2:39:14
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public void set(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	/**
	 * 写入.
	 * 
	 * @param key
	 *            键.
	 * @param value
	 *            值(long).
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:39:24
	 * @updateTime 2014年7月21日,下午2:39:24
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public void set(String key, long value) {
		sp.edit().putLong(key, value).commit();
	}

	/**
	 * 写入.
	 * 
	 * @param key
	 *            键.
	 * @param value
	 *            值(String).
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:39:34
	 * @updateTime 2014年7月21日,下午2:39:34
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public void set(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	/**
	 * 移除指定键的值.
	 * 
	 * @param key
	 *            键.
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:40:40
	 * @updateTime 2014年7月21日,下午2:40:40
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public void remove(String key) {
		sp.edit().remove(key).commit();
	}

	/**
	 * 读取.
	 * 
	 * @param key
	 *            键.
	 * @param defValue
	 *            默认值(boolean).
	 * @return 获取相应键的值,若不存在此键则返回默认值.
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:43:43
	 * @updateTime 2014年7月21日,下午2:43:43
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public boolean get(String key, boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	/**
	 * 读取.
	 * 
	 * @param key
	 *            键.
	 * @param defValue
	 *            默认值(float).
	 * @return 获取相应键的值,若不存在此键则返回默认值.
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:45:04
	 * @updateTime 2014年7月21日,下午2:45:04
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public float get(String key, float defValue) {
		return sp.getFloat(key, defValue);
	}

	/**
	 * 读取.
	 * 
	 * @param key
	 *            键.
	 * @param defValue
	 *            默认值(int).
	 * @return 获取相应键的值,若不存在此键则返回默认值.
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:45:38
	 * @updateTime 2014年7月21日,下午2:45:38
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public int get(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	/**
	 * 读取.
	 * 
	 * @param key
	 *            键.
	 * @param defValue
	 *            默认值(long).
	 * @return 获取相应键的值,若不存在此键则返回默认值.
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:45:59
	 * @updateTime 2014年7月21日,下午2:45:59
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public long get(String key, long defValue) {
		return sp.getLong(key, defValue);
	}

	/**
	 * 读取.
	 * 
	 * @param key
	 *            键.
	 * @param defValue
	 *            默认值(String).
	 * @return 获取相应键的值,若不存在此键则返回默认值.
	 * @version 1.0
	 * @createTime 2014年7月21日,下午2:46:20
	 * @updateTime 2014年7月21日,下午2:46:20
	 * @createAuthor paladin
	 * @updateAuthor paladin
	 * @updateInfo
	 */
	public String get(String key, String defValue) {
		return sp.getString(key, defValue);
	}

	/**
	 * 保存一个对象到sp文件中
	 * 
	 * @param key
	 *            键
	 * @param object
	 *            保存的数据
	 * @version 1.0
	 * @createTime 2015年6月12日,下午2:42:21
	 * @updateTime 2015年6月12日,下午2:42:21
	 * @createAuthor 王治粮
	 * @updateAuthor 王治粮
	 * @updateInfo
	 * 
	 */
	public void setObject(String key, Object object) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objOut = null;
		try {
			objOut = new ObjectOutputStream(outputStream);
			objOut.writeObject(object);
			String objectVal = new String(Base64.encode(outputStream.toByteArray(), Base64.DEFAULT));
			sp.edit().putString(key, objectVal).commit();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != outputStream) {
					outputStream.close();
				}
				if (null != objOut) {
					objOut.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取
	 * 
	 * @param key
	 *            键
	 * @return
	 * @version 1.0
	 * @createTime 2015年6月12日,下午2:50:12
	 * @updateTime 2015年6月12日,下午2:50:12
	 * @createAuthor 王治粮
	 * @updateAuthor 王治粮
	 * @updateInfo
	 * 
	 */
	public Object getObject(String key) {
		if (sp.contains(key)) {
			String objectVal = sp.getString(key, null);
			byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
			ByteArrayInputStream byteIs = new ByteArrayInputStream(buffer);
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(byteIs);
				Object data = ois.readObject();
				return data;
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (byteIs != null) {
						byteIs.close();
					}
					if (ois != null) {
						ois.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public Map<String, ?> getAll() {
		// TODO Auto-generated method stub
		return sp.getAll();
	}
}
