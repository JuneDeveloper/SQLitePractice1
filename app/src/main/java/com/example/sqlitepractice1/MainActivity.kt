package com.example.sqlitepractice1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private lateinit var changeButtonBTN:Button
    private lateinit var deleteButtonBTN:Button

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

    override fun onResume() {
        super.onResume()
        changeButtonBTN.setOnClickListener {
            changeRecord()

        }
        deleteButtonBTN.setOnClickListener {
            deleteRecord()

        }
    }

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    private fun deleteRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog,null)
        dialogBuilder.setView(dialogView)

        val chooseDeleteName = dialogView.findViewById<EditText>(R.id.deleteNameET)
            dialogBuilder.setTitle("Удалить продукт?")
                .setMessage("введите название ниже:")
                .setPositiveButton("Удалить") {_,_ ->
                    val deleteName = chooseDeleteName.text.toString()
                    val product = Product(deleteName,"","")
                    db.deleteProduct(product)
                    Toast.makeText(this,"Продукт удален",Toast.LENGTH_SHORT).show()
                    getAtBase()

                }
                .setNegativeButton("Отмена"){ _,_ ->
                }
        dialogBuilder.create().show()
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
    private fun changeRecord() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.change_dialog,null)
        dialogBuilder.setView(dialogView)
        val editName = dialogView.findViewById<EditText>(R.id.changeNameET)
        val editWeight = dialogView.findViewById<EditText>(R.id.changeWeightET)
        val editPrice = dialogView.findViewById<EditText>(R.id.changePriceET)

        dialogBuilder.setTitle("Обновить продукт?")
            .setMessage("введите название из списка:")
            .setPositiveButton("Обновить"){ _,_ ->
                val changeName = editName.text.toString()
                val changeWeight = editWeight.text.toString()
                val changePrice = editPrice.text.toString()
                val product = Product(changeName,changeWeight,changePrice)
                db.updateProduct(product)
                Toast.makeText(this,"Продукт обновлен",Toast.LENGTH_SHORT).show()
                getAtBase()
            }
            .setNegativeButton("Отмена"){ _,_ ->
            }
            dialogBuilder.create().show()
    }

    private fun getAtBase() {
        productList = db.getProduct()
        listAdapter = ListAdapter(this, productList)
        listViewLV.adapter = listAdapter
        listAdapter?.notifyDataSetChanged()
    }

    private fun addToBase() {
        val product = Product(
            nameProductET.text.toString(),
            weightProductET.text.toString(),
            priceProductET.text.toString())
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
        changeButtonBTN = findViewById(R.id.changeButtonBTN)
        deleteButtonBTN = findViewById(R.id.deleteButtonBTN)
        listViewLV = findViewById(R.id.listViewLV)
        toolbarTB = findViewById(R.id.toolbarTB)
        setSupportActionBar(toolbarTB)
        getAtBase()
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