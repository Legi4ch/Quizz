package ru.fastly.quizz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.fastly.quizz.databinding.FragmentResultScreenBinding
import ru.fastly.quizz.utils.App
import ru.fastly.quizz.utils.Utils
import kotlin.system.exitProcess


class ResultFragmentScreen : Fragment() {

    interface OnQuizzRestartAction {
        fun restart()
    }


    private var rightCount: Int = 0
    private var _binding: FragmentResultScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var listener: OnQuizzRestartAction

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = (context as MainActivity)
        } catch (e: ClassCastException) {
            //
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        rightCount = bundle!!.getInt("RIGHT_COUNT")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultScreenBinding.inflate(inflater, container, false)
        return binding.root
   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resultTextView.text = rightCount.toString()
        binding.commentTextView.text = Utils.getScores(rightCount, App.dataset.getDataCount())
        binding.shareButton.setOnClickListener{
            share()
        }
        binding.exitButton.setOnClickListener {
            exitProcess(0)
        }
        binding.againButton.setOnClickListener{
            listener.restart()
        }
    }

    private fun share() {
        val quizzCount = App.dataset.getDataCount()
        var result:String = rightCount.toString() + "/" + quizzCount.toString() + " " + Utils.getScores(rightCount, quizzCount)
        result += " " + resources.getText(R.string.share_text)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, result)
        startActivity(intent)
    }

}


