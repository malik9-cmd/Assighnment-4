package com.example.androidproject.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "posts_db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "posts"
        private const val COL_ID = "id"
        private const val COL_USERID = "userId"
        private const val COL_TITLE = "title"
        private const val COL_BODY = "body"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY," +
                COL_USERID + " INTEGER," +
                COL_TITLE + " TEXT," +
                COL_BODY + " TEXT" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertPost(post: Post): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, post.id)
        contentValues.put(COL_USERID, post.userId)
        contentValues.put(COL_TITLE, post.title)
        contentValues.put(COL_BODY, post.body)
        
        // Use insertWithOnConflict to avoid duplicates or REPLACE
        return db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getAllPosts(): List<Post> {
        val postList = ArrayList<Post>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val post = Post(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USERID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                    body = cursor.getString(cursor.getColumnIndexOrThrow(COL_BODY))
                )
                postList.add(post)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return postList
    }
    
    fun deletePost(id: Int): Int {
         val db = this.writableDatabase
         return db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(id.toString()))
    }
    
    // Additional CRUD if needed
}
