package io.bhurling.privatebet.add

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import io.bhurling.privatebet.R
import kotterknife.bindView

class AddBetActivity : AppCompatActivity() {

    private val toolbar: Toolbar by bindView(R.id.bets_add_toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
