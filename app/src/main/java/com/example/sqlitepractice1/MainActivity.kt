package com.example.sqlitepractice1

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private val db = DBHelper(this)
    private var productList = mutableListOf<Product>()
    private var listAdapter:ListAdapter? = null

    private lateinit var toolbarTB:Toolbar
    private lateinit var nameProductET:EditText
    private lateinit var weightProductET:EditText
    private lateinit var priceProductET:EditText
    private lateinit var saveButtonBTN:Button
    private lateinit var listViewLV:ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        saveButtonBTN.setOnClickListener {

            addToBase()

            getAtBase()

            Toast.makeText(this,"Товар добавлен в базу и в чек",Toast.LENGTH_SHORT).show()

            clear()
        }
    }

    private fun getAtBase() {
        productList = db.getProduct()
        listAdapter = ListAdapter(this, productList)
        listViewLV.adapter = listAdapter
        listAdapter?.notifyDataSetChanged()
    }

    private fun addToBase() {
        val name = nameProductET.text.toString()
        val product = Product(name, weightProductET.text.toString(), priceProductET.text.toString())
        db.saveProduct(product)
    }

    private fun clear() {
        nameProductET.text.clear()
        weightProductET.text.clear()
        priceProductET.text.clear()
    }

    private fun init() {
        nameProductET = findViewById(R.id.nameProductET)
        weightProductET = findViewById(R.id.weightProductET)
        priceProductET = findViewById(R.id.priceProductET)
        saveButtonBTN = findViewById(R.id.saveButtonBTN)
        listViewLV = findViewById(R.id.listViewLV)
        toolbarTB = findViewById(R.id.toolbarTB)
        setSupportActionBar(toolbarTB)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.removeBase -> {
                db.deleteAll()
                productList.clear()
                listAdapter?.notifyDataSetChanged()
            }
            R.id.exiItem -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}