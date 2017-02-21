package pers.lim95.weather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pers.lim95.weather.model.City;
import pers.lim95.weather.model.County;
import pers.lim95.weather.model.Province;

/**
 * Created by lim9527 on 2/21 0021.
 */

public class WeatherDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "db_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static WeatherDB sWeatherDB;

    private SQLiteDatabase db;

    /**
     * 构造方法私有化
     */
    private WeatherDB(Context context){
        WeatherOpenHelper dbHelper =
                new WeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取 WeatherDB 实例.
     */
    public synchronized static WeatherDB getInstance(Context context){
        if(sWeatherDB == null){
            sWeatherDB = new WeatherDB(context);
        }
        return sWeatherDB;
    }

    /**
     * 将 Province 实例存储到数据库。
     */
    public void saveProvince(Province province){
        if (province != null){
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    /**
     * 从数据库读取全国所有的省份信息。
     */
    public List<Province> loadProvince(){
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(
                        cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(
                        cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 将 City 实例存储到数据库。
     */
    public void saveCity(City city){
        if (city != null){
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    /**
     * 从数据库读取某省下的所有城市信息。
     */
    public List<City> loadCity(int provinceId){
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City", null,
                "province_id = ?", new String[]{String.valueOf(provinceId)},
                null, null, null);
        if (cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(
                        cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(
                        cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 将 County 实例存储到数据库。
     */
    public void saveCounty(County county){
        if (county != null){
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            db.insert("County", null, values);
        }
    }

    /**
     * 从数据库读取某城市下的所有县信息。
     */
    public List<County> loadCounty(int cityId){
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("County", null,
                "city_id = ?", new String[]{String.valueOf(cityId)},
                null, null, null);
        if (cursor.moveToFirst()){
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(
                        cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(
                        cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            }while (cursor.moveToNext());
        }
        return list;
    }
}
