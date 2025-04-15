package com.example.kjr_app.sampledata;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.kjr_app.DocInfo;
import com.example.kjr_app.UserInfo;

public class UserDbHelper extends SQLiteOpenHelper {
    private static UserDbHelper sHelper;
    private static final String DB_NAME = "kjr_chat_app.db";   // 数据库名
    private static final int VERSION = 1;                // 版本号

    public UserDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public synchronized static UserDbHelper getInstance(Context context) {
        if (sHelper == null) {
            sHelper = new UserDbHelper(context, DB_NAME, null, VERSION);
        }
        return sHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 启用外键约束（SQLite默认关闭，需手动开启）
        db.execSQL("PRAGMA foreign_keys = ON;");

        // 创建患者信息表（patient_info）
        db.execSQL("CREATE TABLE patient_info (" +
                "pat_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_name TEXT NOT NULL DEFAULT '未命名'," +
                "nick_name TEXT NOT NULL DEFAULT '默认'," +
                "phone_number TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "sex TEXT," +
                "email TEXT," +
                "update_time TEXT," +
                "create_time TEXT ," +
                "del_flag TEXT NOT NULL DEFAULT '0'," +
                "avatar TEXT," +
                "remark TEXT," +
                "login_date TEXT," +
                "status TEXT NOT NULL DEFAULT '0'" +
                ")");

        // 创建医生信息表（doc_info）
        db.execSQL("CREATE TABLE doc_info (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_name TEXT NOT NULL DEFAULT 'doc'," +
                "nick_name TEXT NOT NULL DEFAULT 'docName'," +
                "phone_number TEXT NOT NULL," +
                "password TEXT NOT NULL," +

                "sex TEXT," +
                "email TEXT," +
                "speciality TEXT ," +
                "update_time TEXT," +
                "create_time TEXT ," +
                "del_flag TEXT DEFAULT '0'," +
                "avatar TEXT," +
                "remark TEXT," +
                "login_date TEXT," +
                "status TEXT DEFAULT '0'" +
                ")");

        // 创建设备信息表（device_info）
        db.execSQL("CREATE TABLE device_info (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "device_name TEXT NOT NULL," +
                "device_mac TEXT NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "create_time TEXT NOT NULL," +
                "update_time TEXT," +
                "remark TEXT," +
                "FOREIGN KEY (user_id) REFERENCES patient_info(pat_id)" +
                ")");

        // 创建患者手术基础信息表（basic_operation_info）
        db.execSQL("CREATE TABLE basic_operation_info (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "operation_time TEXT NOT NULL," +
                "ethnic_groups TEXT NOT NULL," +
                "height INTEGER," +
                "weight INTEGER," +
                "operation TEXT NOT NULL," +
                "age INTEGER NOT NULL," +
                "smoking TEXT NOT NULL," +
                "address TEXT," +
                "pulse INTEGER NOT NULL," +
                "pain INTEGER NOT NULL," +
                "sbp INTEGER NOT NULL," +
                "dbp INTEGER NOT NULL," +
                "swelling TEXT NOT NULL," +
                "create_time TEXT DEFAULT CURRENT_TIMESTAMP," +
                "update_time TEXT," +
                "remark TEXT," +
                "FOREIGN KEY (user_id) REFERENCES patient_info(pat_id)" +
                ")");

        // 创建温度数据表（temperature）
        db.execSQL("CREATE TABLE temperature (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "temperature NUMERIC NOT NULL," +
                "device_id INTEGER NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "create_time TEXT NOT NULL," +
                "remark TEXT," +
                "FOREIGN KEY (device_id) REFERENCES device_info(id)," +
                "FOREIGN KEY (user_id) REFERENCES patient_info(pat_id)" +
                ")");

        // 创建屈膝度数据表（knee_flexion）
        db.execSQL("CREATE TABLE knee_flexion (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "knee NUMERIC NOT NULL," +
                "device_id INTEGER NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "create_time TEXT NOT NULL," +
                "remark TEXT," +
                "FOREIGN KEY (device_id) REFERENCES device_info(id)," +
                "FOREIGN KEY (user_id) REFERENCES patient_info(pat_id)" +
                ")");

        // 创建行走步数数据表（walking_steps）
        db.execSQL("CREATE TABLE walking_steps (" +
                "walk_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "walk INTEGER NOT NULL," +
                "speed NUMERIC NOT NULL," +
                "distance NUMERIC NOT NULL," +
                "device_id INTEGER NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "create_time TEXT NOT NULL," +
                "remark TEXT," +
                "FOREIGN KEY (device_id) REFERENCES device_info(id)," +
                "FOREIGN KEY (user_id) REFERENCES patient_info(pat_id)" +
                ")");

        // 创建患者医生留言表（pat_doc_message）
        db.execSQL("CREATE TABLE pat_doc_message (" +
                "user_id INTEGER NOT NULL," +
                "doctor_id INTEGER NOT NULL," +
                "content TEXT NOT NULL," +
                "content_type TEXT NOT NULL," +
                "status TEXT NOT NULL DEFAULT '0'," +
                "FOREIGN KEY (user_id) REFERENCES patient_info(pat_id)," +
                "FOREIGN KEY (doctor_id) REFERENCES doc_info(user_id)" +
                ")");

        // 创建医生患者关系表（doctor_patient_relation）
        db.execSQL("CREATE TABLE doctor_patient_relation (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "doc_id INTEGER NOT NULL," +
                "pat_id INTEGER NOT NULL," +
                "create_time TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "UNIQUE (pat_id)," +
                "FOREIGN KEY (doc_id) REFERENCES doc_info(user_id)," +
                "FOREIGN KEY (pat_id) REFERENCES patient_info(pat_id)" +
                ")");

        // 创建症状描述表（symptom_description）
        db.execSQL("CREATE TABLE symptom_description (" +
                "symptom_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "symptom_content TEXT NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "create_time TEXT NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES patient_info(pat_id)" +
                ")");
    }
    /**
     * 登录：根据手机号查找医生
     */
    @SuppressLint("Range")
    public DocInfo doc_login(String phone) {
        SQLiteDatabase db = getReadableDatabase();
        DocInfo docInfo = null;
        // 1. 修正SQL：查询所有UserInfo需要的字段，注意列名与表中实际字段匹配
        String sql = "SELECT " +
                "user_id, " +
                "user_name, " +
                "nick_name, " +
                "phone_number, " +
                "password, " +
                "sex, " +
                "email, " +
                "speciality," +
                "update_time, " +
                "create_time, " +
                "del_flag, " +
                "avatar, " +
                "remark, " +
                "login_date, " +
                "status " +
                "FROM doc_info " +
                "WHERE phone_number = ?";

        String[] selectionArgs = {phone};
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        try { // 使用try-with-resources自动关闭cursor（Android API 14+）
            if (cursor.moveToNext()) {
                // 2. 通过列名获取索引，避免依赖SQL查询顺序
                int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
                String userName = cursor.getString(cursor.getColumnIndex("user_name"));
                String phoneNumber = cursor.getString(cursor.getColumnIndex("phone_number"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String nickName = cursor.getString(cursor.getColumnIndex("nick_name"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String speciality = cursor.getString(cursor.getColumnIndex("speciality"));
                String updateTime = cursor.getString(cursor.getColumnIndex("update_time"));
                String createTime = cursor.getString(cursor.getColumnIndex("create_time"));
                String delFlag = cursor.getString(cursor.getColumnIndex("del_flag"));
                String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                String loginDate = cursor.getString(cursor.getColumnIndex("login_date"));
                String status = cursor.getString(cursor.getColumnIndex("status"));

                // 3. 按UserInfo构造函数参数顺序传参（注意顺序与构造函数一致）
                docInfo = new DocInfo(
                        userId,
                        userName,
                        nickName,
                        phoneNumber,
                        password,
                        sex,
                        email,
                        speciality,
                        updateTime,
                        createTime,
                        delFlag,
                        avatar,
                        remark,
                        loginDate,
                        status
                );
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close(); // 确保cursor关闭，避免内存泄漏
            }
            // 不要手动关闭db！由SQLiteOpenHelper管理，多次关闭会报错
            // db.close(); // 错误：getReadableDatabase()获取的db应在Activity/Fragment中统一关闭
        }
        return docInfo;

    }
    /**
     * 登录：根据手机号查找用户
     */
    @SuppressLint("Range")
    public UserInfo user_login(String phone) {
        SQLiteDatabase db = getReadableDatabase();
        UserInfo userInfo = null;
        // 1. 修正SQL：查询所有UserInfo需要的字段，注意列名与表中实际字段匹配
        String sql = "SELECT " +
                "pat_id, " +
                "user_name, " +
                "phone_number, " +
                "password, " +
                "nick_name, " +
                "sex, " +
                "email, " +
                "update_time, " +
                "create_time, " +
                "del_flag, " +
                "avatar, " +
                "remark, " +
                "login_date, " +
                "status " +
                "FROM patient_info " +
                "WHERE phone_number = ?";

        String[] selectionArgs = {phone};
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        try { // 使用try-with-resources自动关闭cursor（Android API 14+）
            if (cursor.moveToNext()) {
                // 2. 通过列名获取索引，避免依赖SQL查询顺序
                int userId = cursor.getInt(cursor.getColumnIndex("pat_id"));
                String userName = cursor.getString(cursor.getColumnIndex("user_name"));
                String phoneNumber = cursor.getString(cursor.getColumnIndex("phone_number"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String nickName = cursor.getString(cursor.getColumnIndex("nick_name"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String updateTime = cursor.getString(cursor.getColumnIndex("update_time"));
                String createTime = cursor.getString(cursor.getColumnIndex("create_time"));
                String delFlag = cursor.getString(cursor.getColumnIndex("del_flag"));
                String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                String loginDate = cursor.getString(cursor.getColumnIndex("login_date"));
                String status = cursor.getString(cursor.getColumnIndex("status"));

                // 3. 按UserInfo构造函数参数顺序传参（注意顺序与构造函数一致）
                userInfo = new UserInfo(
                        userId,
                        userName,
                        phoneNumber,
                        password,
                        nickName,
                        sex,
                        email,
                        updateTime,
                        createTime,
                        delFlag,
                        avatar,
                        remark,
                        loginDate,
                        status
                );
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close(); // 确保cursor关闭，避免内存泄漏
            }
            // 不要手动关闭db！由SQLiteOpenHelper管理，多次关闭会报错
            // db.close(); // 错误：getReadableDatabase()获取的db应在Activity/Fragment中统一关闭
        }
        return userInfo;

    }
// 用户注册
public Long user_register(String userPhone, String password, String nickName) {
    //获取SQLiteDatabase实例
    SQLiteDatabase db = getWritableDatabase();
    ContentValues values = new ContentValues();
    //填充占位符
    values.put("phone_number", userPhone);
    values.put("password", password);
    values.put("nick_name", nickName);
    // 不要手动关闭db！由SQLiteOpenHelper管理
    // nullColumnHack设为null（ContentValues已有有效字段，无需指定非空列）
    long insertRowId = db.insert("patient_info", null, values);
    return insertRowId; // 返回-1表示插入失败，非-1为rowId
}
    // 医生注册
    public Long doc_register(String userPhone, String password, String nickName) {
        //获取SQLiteDatabase实例
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //填充占位符
        values.put("phone_number", userPhone);
        values.put("password", password);
        values.put("nick_name", nickName);
        // 不要手动关闭db！由SQLiteOpenHelper管理
        // nullColumnHack设为null（ContentValues已有有效字段，无需指定非空列）
        long insertRowId = db.insert("doc_info", null, values);
        return insertRowId; // 返回-1表示插入失败，非-1为rowId
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 版本更新时删除所有表并重建（生产环境需更谨慎的迁移逻辑）

    }
}