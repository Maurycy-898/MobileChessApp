package com.mobile.chessapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.mobile.chessapp.R
import com.mobile.chessapp.backend.database.DatabaseHandler
import com.mobile.chessapp.backend.game.boardUtils.PieceColor
import com.mobile.chessapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var whitePlayer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_archive,
                R.id.navigation_notifications,
                R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun play(view: View) {
        when (view.id) {
            R.id.offline_button -> {
                val playIntent = Intent(this, GameActivity::class.java)
                playIntent.putExtra("mode", 0)
                startActivity(playIntent)
            }
            R.id.online_button -> {
                Log.i("isWhite", whitePlayer.toString())
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                builder.setMessage("Waiting for opponent...")
                builder.setPositiveButton("Cancel") { _, _ ->
                    DatabaseHandler.database.reference.child("player").removeValue()
                    whitePlayer = false
                }
                val dialog = builder.create()
                DatabaseHandler.database.reference.child("player").get().addOnSuccessListener {
                    if (!it.exists()) {
                        whitePlayer = true
                        DatabaseHandler.database.reference.child("player").setValue(false)
                        dialog.show()
                    }
                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
                DatabaseHandler.database.reference.child("player").addValueEventListener(DataChangeListener(this, dialog))
            }
            else -> {
                val playIntent = Intent(this, GameActivity::class.java)
                playIntent.putExtra("mode", 2)
                startActivity(playIntent)
            }
        }
    }

    inner class DataChangeListener(var context: Context, private var dialog: AlertDialog): ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val player = snapshot.getValue<Boolean>()
            if (player == true && whitePlayer) {
                dialog.hide()
                whitePlayer = false
                DatabaseHandler.database.reference.child("player").removeValue()
                DatabaseHandler.database.reference.child("player").removeEventListener(this)
                val playIntent = Intent(context, GameActivity::class.java)
                playIntent.putExtra("mode", 1)
                playIntent.putExtra("color", PieceColor.WHITE.name)
                startActivity(playIntent)
            } else if (player == false && !whitePlayer) {
                DatabaseHandler.database.reference.child("player").setValue(true)
                DatabaseHandler.database.reference.child("player").removeEventListener(this)
                val playIntent = Intent(context, GameActivity::class.java)
                playIntent.putExtra("mode", 1)
                playIntent.putExtra("color", PieceColor.BLACK.name)
                startActivity(playIntent)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(
                "Database:player",
                "loadPlayer:onCancelled",
                databaseError.toException()
            )
        }
    }
}