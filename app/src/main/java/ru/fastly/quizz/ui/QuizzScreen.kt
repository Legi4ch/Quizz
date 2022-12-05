package ru.fastly.quizz.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ru.fastly.quizz.App
//import androidx.lifecycle.ViewModelProviders
import ru.fastly.quizz.R
import ru.fastly.quizz.databinding.FragmentQuizzScreenBinding
import ru.fastly.quizz.view_models.QuizzViewModel
import ru.fastly.quizz.view_models.QuizzViewModelFactory


class QuizzScreen : Fragment() {

    interface OnFragmentCallAction {
        fun changeFragment(count:Int)
    }

    private var _binding: FragmentQuizzScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var provider: ViewModelProvider
    private val model get() = provider.get(QuizzViewModel::class.java)



    private lateinit var questionText: TextView
    private lateinit var progressText: TextView
    private lateinit var listener: OnFragmentCallAction

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizzScreenBinding.inflate(inflater, container, false)
        provider = ViewModelProviders.of(this, QuizzViewModelFactory(App.dataset))
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setup(model.getCurrentStep())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionText = binding.questionTextView
        progressText = binding.progressText
        binding.radioGroup.setOnCheckedChangeListener { _, _ -> checkRadioStatus()}
        binding.nextButton.setOnClickListener { next() }
        binding.backButton.setOnClickListener{ back() }

        //подписываемся на изменение состояния в модели и рисуем их
        model.state.observe(viewLifecycleOwner) {
            setup(model.getCurrentStep())
        }
   }

    private fun next() {
        if (model.isVisited()) {
            //ответ не требуется, уже был просмотр и соответственно ответ
            model.stepForward(getRadioIndex())
        } else if (!model.isLastQuestion() && getRadioIndex() != -1) {
            model.stepForward(getRadioIndex())
        } else if (model.isLastQuestion() && getRadioIndex() != -1) {
            // теперь надо показать результирующий фрагмент со всеми свистоперделками и сбрасываем квиз
            showResult()
            resetQuizz()
        } else {
            Toast.makeText(this.context,resources.getText(R.string.need_answer)  , Toast.LENGTH_SHORT).show()
        }
    }

    private fun back() {
        if (model.getCurrentStep() > 0) {
            model.stepBackward()
        } else {
            Toast.makeText(this.context,resources.getText(R.string.is_first_question)  , Toast.LENGTH_SHORT).show()
        }
    }

    //получает index выбранной радиокнопки
    private fun getRadioIndex(): Int {
        val checked:Int = binding.radioGroup.checkedRadioButtonId
        if (checked > -1) {
            val elem = view?.findViewById<RadioButton>(checked)
            return binding.radioGroup.indexOfChild(elem)
        }
        else return -1
    }

    //если вопрос уже посещали и соответсвенно отвечали, то не надо в нем тыкать в радио кнопки!
    private fun checkRadioStatus() {
        if (model.isVisited()) {
           Toast.makeText(this.context,resources.getText(R.string.answer_is_exists)  , Toast.LENGTH_SHORT).show()
        }
    }

    //установка кнопок, если был дан ответ, то он будет включен
    private fun setRadioState(id: Int) {
        when (id) {
            0 -> binding.r0.isChecked = true
            1 -> binding.r1.isChecked = true
            2 -> binding.r2.isChecked = true
            3 -> binding.r3.isChecked = true
        }
    }


    //вызывается для установки значений вопроса и подсветки вариантов ответа, если он был дан
    private fun setup(id:Int) {
        binding.radioGroup.clearCheck()
        questionText.text = model.getQuestion(id)
        val answers = model.getAnswers(id)
        binding.r0.text = answers[0].text
        binding.r1.text = answers[1].text
        binding.r2.text = answers[2].text
        binding.r3.text = answers[3].text
        progressText.text = model.getProgress()
        if (model.isVisited()) {
           setRadioState(model.getCurrentAnswerId())
       }
   }


    //сброс квиза на начало
    private fun resetQuizz() {
        model.clear()
        setup(model.getCurrentStep())
    }

    private fun showResult() {
        listener.changeFragment(model.getScores())
    }

}


