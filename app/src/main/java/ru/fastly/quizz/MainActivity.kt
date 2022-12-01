package ru.fastly.quizz


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.fastly.quizz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), QuizzScreen.OnFragmentCallAction,
    ResultFragmentScreen.OnQuizzRestartAction {
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //remove the toolbar
        try {
            supportActionBar?.hide()
        } catch (e: NullPointerException){

        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun changeFragment(count: Int) {
        val resultFragment: ResultFragmentScreen = ResultFragmentScreen()
        var args: Bundle = Bundle()
        args.putInt("RIGHT_COUNT", count)
        resultFragment.arguments = args
        val fm = supportFragmentManager.beginTransaction().replace(R.id.myScreen, resultFragment).commit()
    }

    override fun restart() {
        val quizzFragment: QuizzScreen = QuizzScreen()
        val fm = supportFragmentManager.beginTransaction().replace(R.id.myScreen, quizzFragment).commit()
    }


}