package ru.fastly.quizz.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import ru.fastly.quizz.R
import ru.fastly.quizz.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), QuizzScreen.OnFragmentCallAction,
    ResultFragmentScreen.OnQuizzRestartAction {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //remove the toolbar
        try {
            supportActionBar?.hide()
        } catch (_: NullPointerException){

        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun changeFragment(count: Int) {
        val resultFragment = ResultFragmentScreen()
        val args = Bundle()
        args.putInt("RIGHT_COUNT", count)
        resultFragment.arguments = args
        supportFragmentManager.beginTransaction().replace(R.id.myScreen, resultFragment).commit()
    }

    override fun restart() {
        val quizzFragment = QuizzScreen()
        supportFragmentManager.beginTransaction().replace(R.id.myScreen, quizzFragment).commit()
    }


}