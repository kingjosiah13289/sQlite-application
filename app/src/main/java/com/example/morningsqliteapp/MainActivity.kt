package com.example.morningsqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.viewmodel.viewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var edtname : EditText
    lateinit var email : EditText
    lateinit var Idnum : EditText
    lateinit var Save : Button
    lateinit var View : Button
    lateinit var Delete : Button
    lateinit var db : SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtname = findViewById(R.id.mbtn_Name)
        email = findViewById(R.id.mbtn_email)
        Idnum = findViewById(R.id.mbtn_Number)
        Save = findViewById(R.id.btn_save)
        Delete = findViewById(R.id.btn_delete)
        View = findViewById(R.id.btn_view)
        // create database called emobilisDb
        db = openOrCreateDatabase("emobilisDb", Context.MODE_PRIVATE, null)
        // create a table calles users inside the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")

        //start setting onclick listeners to the buttons
        Save.setOnClickListener {
            // receive the data from the user
            var name = edtname.text.toString().trim()
            var email = email.text.toString().trim()
            var Id = Idnum.text.toString().trim()
            // check if the if the user is submitting ana empty field
            if (name.isEmpty()|| email.isEmpty()|| Id.isEmpty()){
                message("EMPTY FIELD", "PLEASE FILL ALL INPUTS")
            }else{
                // procede to the data
                db.execSQL("INSERT INTO users VALUES('"+name+"', '"+email+"', '"+Id+"')")
                clear()
                message("SUCCESS!!!","User saved successfully")
            }
        }

        View.setOnClickListener {
            // use cursor to select all users
            var cursor = db.rawQuery("SELECT * FROM users", null)
            // check if there is any record in the db
            if (cursor.count == 0){
                message("NO USERS WERE FOUND", "Sorry no users were found")

            }else{
                // use string buffer to append all the available records using a loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(0)
                    var retrievedIdNumber = cursor.getString(0)
                    buffer.append(retrievedName+'\n')
                    buffer.append(retrievedEmail+'\n')
                    buffer.append(retrievedIdNumber+'\n')

                }
                message("USERS", buffer.toString())
            }
        }
        Delete.setOnClickListener {
            val idnumber = Idnum.text.toString().trim()
            if (idnumber.isEmpty()){
                message("EMPTY FIELD","please enter an id number")
            }else{
                // use cursor to select all the users
                var cursor = db.rawQuery("SELECT * FROM users WHERE kitambulisho= '"+idnumber+"'",null)
                // check if the record provided with the id exists
                if(cursor.count == 0){
                    message("NO USER", "Sorry, no record with id" +idnumber)
                    // procede to delete the user
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+idnumber+"'")
                    clear()
                }
            }
        }
    }

    fun message(title:String, message:String){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Close", null )
        alertDialog.create().show()
    }
    fun clear(){
        edtname.setText("")
        email.setText("")
        Idnum.setText("")
    }
}