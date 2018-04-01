package io.bhurling.privatebet.add

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.EditText
import io.bhurling.privatebet.R
import kotterknife.bindView

class AddBetActivity : AppCompatActivity() {

    private val toolbar: Toolbar by bindView(R.id.bets_add_toolbar)
    private val statement: EditText by bindView(R.id.bets_add_statement)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        statement.setHorizontallyScrolling(false)
        statement.maxLines = 100
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
