package com.example.hongca

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView

class MyDBHelper(val context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSIOM) {
    companion object{
        val DB_NAME = "mydb.db"
        val DB_VERSIOM = 1
        val TABLE_NAME ="products"

        val PID = "pid"
        val PTITLE = "ptitle"
        val PWORD = "pword"
        val PMEANING = "pmeaning"
        val PSTAR = "false"
    }

    fun getRecord(num:Int){
        val temp = TABLE_NAME
        val strsql = "select * from $temp;"

        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)

//        showRecord(cursor)
        cursor.close()
        db.close()
    }

//    fun showRecord(cursor: Cursor){
//        cursor.moveToFirst()
//        val attrcount = cursor.columnCount
//        val activity = context as MainActivity
//        activity.binding.recycler.removeAllViewsInLayout()
//        //타이틀 만들기
//        val tablerow = TableRow(activity)
//        val rowParam = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
//        tablerow.layoutParams = rowParam
//        val viewParam = TableRow.LayoutParams(0, 100, 1f)
//        for(i in 0 until attrcount){
//            val textView = TextView(activity)
//            textView.layoutParams = viewParam
//            textView.text = cursor.getColumnName(i)
//            textView.setBackgroundColor(Color.LTGRAY)
//            textView.textSize = 15.0f
//            textView.gravity = Gravity.CENTER
//            tablerow.addView(textView)
//        }
//        activity.binding.tableLayout.addView(tablerow)
//        if(cursor.count == 0) return
//        // 레코드 추가하기
//        do{
//            val row = TableRow(activity)
//            row.layoutParams = rowParam
//            row.setOnClickListener {
//                for(i in 0 until attrcount){
//                    val textView = row.getChildAt(i) as TextView
//                    when(textView.tag){
//                        0-> activity.binding.pIdEdit.setText(textView.text)
//                        1-> activity.binding.pNameEdit.setText(textView.text)
//                        2-> activity.binding.pQuantityEdit.setText(textView.text)
//                    }
//
//                }
//            }
//            for(i in 0 until attrcount){
//                val textView = TextView(activity)
//                textView.tag = i //뷰 식별자
//                textView.layoutParams = viewParam
//                textView.text = cursor.getString(i)
//                textView.textSize = 13.0f
//                textView.gravity = Gravity.CENTER
//                row.addView(textView)
//            }
//            activity.binding.tableLayout.addView(row)
//        }while (cursor.moveToNext())
//    }


    override fun onCreate(db: SQLiteDatabase?) { //처음 생성될때
        val create_table = "create table if not exists $TABLE_NAME(" +
                "$PID integer primary key autoincrement, " +
                "$PTITLE text, "+
                "$PWORD text, "+
                "$PMEANING text, "+
                "$PSTAR text);"
        db!!.execSQL(create_table) //테이블 생성
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { //버전이 바뀌었을 때
        val drop_table = "drop table if exists $TABLE_NAME;" //테이블 삭제
        db!!.execSQL(drop_table)
        onCreate(db)
    }


    fun insertProduct(product: MyData):Boolean{
        val values = ContentValues()
        values.put(PTITLE, product.title)
        values.put(PWORD, product.word)
        values.put(PMEANING, product.meaning)
        values.put(PSTAR, product.star)

        val db = writableDatabase
        val flag = db.insert(TABLE_NAME, null, values) > 0
        db.close()
        return flag
    }


    //select * from product where name = 'pid'; title 찾기
    fun findProduct(name: String): Boolean {
        val strsql = "select * from $TABLE_NAME where $PTITLE='$name';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
//        showRecord(cursor)
        cursor.close()
        db.close()
        return flag
    }


    //select * from product where pid = 'pid'; 찾고 지워줌
    fun deleteProduct(pid: String): Boolean {
        val strsql = "select * from $TABLE_NAME where $PID='$pid';"
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
        if(flag){
            cursor.moveToFirst()
            db.delete(TABLE_NAME, "$PID=?", arrayOf(pid))
        }
        cursor.close()
        db.close()
        return flag
    }

    fun updateProduct(product: MyData): Boolean {
        val pid = product.id
        val strsql = "select * from $TABLE_NAME where $PID='$pid';"
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
        if(flag){
            cursor.moveToFirst()
            val values = ContentValues()
            values.put(PTITLE, product.title)
            values.put(PWORD, product.word)
            values.put(PMEANING, product.meaning)
            values.put(PSTAR, product.star)
            db.update(TABLE_NAME, values, "$PID=?", arrayOf(pid.toString()))
        }
        cursor.close()
        db.close()
        return flag
    }

    //select * from product where pname like'김%'; 단어찾기
    fun findProduct2(word: String): Boolean{
        val strsql = "select * from $TABLE_NAME where $PWORD like'$word%';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
//        showRecord(cursor)
        cursor.close()
        db.close()
        return flag
    }
}