package com.example.sqlitepractice1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context):
    SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
        companion object{
            private val DATABASE_VERSION = 1
            private val DATABASE_NAME = "PRODUCT_DATABASE"
            private val TABLE_NAME = "product_table"
            val KEY_NAME = "name"
            val KEY_WEIGHT = "weight"
            val KEY_PRICE = "price"
        }

    override fun onCreate(db: SQLiteDatabase?) {
        val PRODUCT_TABLE = (
                "CREATE TABLE " + TABLE_NAME + " ("
                        + KEY_NAME + " TEXT,"
                        + KEY_WEIGHT + " TEXT,"
                        + KEY_PRICE + " TEXT" + ")")
        db?.execSQL(PRODUCT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun saveProduct(product: Product){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME,product.productName)
        contentValues.put(KEY_WEIGHT,product.productWeight)
        contentValues.put(KEY_PRICE,product.productPrice)
        db.insert(TABLE_NAME,null,contentValues)
        db.close()
    }

    @SuppressLint("Range", "Recycle")
    fun getProduct():MutableList<Product>{
        val productList:MutableList<Product> = mutableListOf()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor:Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery,null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return productList
        }
        var name:String
        var weight:String
        var price:String
        if(cursor.moveToFirst()){
            do {
                name = cursor.getString(cursor.getColumnIndex("name"))
                weight = cursor.getString(cursor.getColumnIndex("weight"))
                price = cursor.getString(cursor.getColumnIndex("price"))
                val product = Product(productName = name,productWeight = weight,productPrice = price)
                productList.add(product)
            }while (cursor.moveToNext())
        }
        return productList
    }

    fun updateProduct(product: Product){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME,product.productName)
        contentValues.put(KEY_WEIGHT,product.productWeight)
        contentValues.put(KEY_PRICE,product.productPrice)
        db.update(TABLE_NAME,contentValues,"name=" + product.productName, null)
        db.close()
    }

    fun deleteProduct(product: Product){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME,product.productName)
        db.delete(TABLE_NAME,"name=" + product.productName, null)
        db.close()
    }

    fun deleteAll(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,null,null)
    }

}