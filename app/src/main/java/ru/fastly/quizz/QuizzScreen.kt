package ru.fastly.quizz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ru.fastly.quizz.databinding.FragmentQuizzScreenBinding
import ru.fastly.quizz.providers.DataProvider
import ru.fastly.quizz.utils.App
import ru.fastly.quizz.view_models.QuizzViewModel


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
        provider = ViewModelProviders.of(this)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setup(model.currentIndex)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionText = binding.questionTextView
        progressText = binding.progressText
        binding.radioGroup.setOnCheckedChangeListener { _, _ -> checkRadioStatus()}
        binding.nextButton.setOnClickListener { next() }
        binding.backButton.setOnClickListener{ back() }
   }

    private fun next() {
        val myData = DataProvider(App.getContext())
        if (isVisited()) {
            //ответ не требуется, уже был просмотр и соответственно ответ
            model.currentIndex++
            setup(model.currentIndex)
        } else if (model.currentIndex+1 < myData.getDataCount() && getRadioIndex() != -1) {
            setAnswer(getRadioIndex()) //пишем ответ
            model.currentIndex++
            setup(model.currentIndex)
        } else if (model.currentIndex+1 == myData.getDataCount() && getRadioIndex() != -1) {
            setAnswer(getRadioIndex()) //пишем ответ
            // теперь надо показать результирующий фрагмент со всеми свистоперделками
            showResult()
            resetQuizz()
        } else {
            Toast.makeText(App.getContext(),resources.getText(R.string.need_answer)  , Toast.LENGTH_SHORT).show()
        }
    }

    private fun back() {
        if (model.currentIndex > 0) {
            model.currentIndex--
            setup(model.currentIndex)
        } else {
            Toast.makeText(App.getContext(),resources.getText(R.string.is_first_question)  , Toast.LENGTH_SHORT).show()
        }
    }

    //устанавливаем вопрос просмотренным и записываем ответ
    private fun setAnswer(id:Int) {
        val myData = DataProvider(App.getContext())
        model.visited.add(model.currentIndex)
        model.answers.add(id)
        if (myData.getRightAnswerIdForQuestion(model.currentIndex) == id) {
            model.correctCount++
        }
    }

    //просмотренный вопрос или нет
    private fun isVisited(): Boolean {
        return model.currentIndex in model.visited
    }

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
        if (isVisited()) {
           Toast.makeText(App.getContext(),resources.getText(R.string.answer_is_exists)  , Toast.LENGTH_SHORT).show()
        }
    }

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
        val myData = DataProvider(App.getContext())
        binding.radioGroup.clearCheck()
        questionText.text = myData.getQuestion(id)
        val answers = myData.getAnswersForQuestion(id)
        binding.r0.text = answers[0].text
        binding.r1.text = answers[1].text
        binding.r2.text = answers[2].text
        binding.r3.text = answers[3].text
        progressText.text = (model.currentIndex+1).toString() + " / " + myData.getDataCount().toString()
        if (isVisited()) {
            setRadioState(model.answers[model.currentIndex])
        }
   }


    //сброс квиза на начало
    private fun resetQuizz() {
        model.clear()
        setup(model.currentIndex)
    }

    private fun showResult() {
        listener.changeFragment(model.correctCount)
    }

}


