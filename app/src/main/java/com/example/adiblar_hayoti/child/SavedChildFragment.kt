package com.example.adiblar_hayoti.child

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.adiblar_hayoti.R
import com.example.adiblar_hayoti.databinding.FragmentAdibChildBinding
import com.example.adiblar_hayoti.databinding.FragmentSavedChildBinding
import com.example.adiblar_hayoti.models.Adib
import com.example.adiblar_hayoti.room.Adib_Entity
import com.example.adiblar_hayoti.room.AppDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SavedChildFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedChildFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentSavedChildBinding
    lateinit var appDatabase: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentSavedChildBinding.inflate(layoutInflater,container,false)
        appDatabase = AppDatabase.getInstance(binding.root.context)
        setUI()
        setSearch()

        return binding.root
    }

    private fun setSearch() {
        binding.edittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()){
                    binding.text.setTextToHighlight(s.toString())
                    binding.text.setTextHighlightColor(R.color.purple_200)
                    binding.text.highlight()

                }

            }

        })
    }

    private fun setUI() {
        val adib = arguments?.getSerializable("key") as Adib_Entity
        val position = arguments?.getInt("int")

        binding.liner.setBackgroundResource(R.drawable.circle_shape)
            binding.collection.setImageResource(R.drawable.saved)

        var a=100
        binding.liner.setOnClickListener {
            if (a==position) {
                binding.liner.setBackgroundResource(R.drawable.circle_shape)
                binding.collection.setImageResource(R.drawable.saved)
                val adibEntity = Adib_Entity()
                adibEntity.photoUrl = adib.photoUrl
                adibEntity.name = adib.name
                adibEntity.birth_date = adib.birth_date
                adibEntity.death_date = adib.death_date
                adibEntity.type = adib.type
                adibEntity.description = adib.description
                appDatabase.adibDao().addKurs(adibEntity)
                a++
            } else {
                binding.liner.setBackgroundResource(R.color.white)
                binding.collection.setImageResource(R.drawable.ribbon)
                appDatabase.adibDao().deleteByName(adib.name!!)
                a=position!!
            }
        }

        binding.name.text = adib.name
        binding.years.text = "(${adib.birth_date}-${adib.death_date})"
        binding.text.text = adib.description
        Glide.with(binding.root.context).load(adib.photoUrl).into(binding.image)



        binding.linerSearch.setOnClickListener {
            binding.gang.visibility = View.GONE
            binding.gangster.visibility = View.VISIBLE
        }

        binding.clear.setOnClickListener {
            binding.gang.visibility = View.VISIBLE
            binding.gangster.visibility = View.INVISIBLE
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SavedChildFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SavedChildFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}