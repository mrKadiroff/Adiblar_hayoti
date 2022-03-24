package com.example.adiblar_hayoti.child

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet


import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.example.adiblar_hayoti.R
import com.example.adiblar_hayoti.databinding.FragmentAdibChildBinding
import com.example.adiblar_hayoti.databinding.FragmentSearchBinding
import android.view.InflateException

import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.example.adiblar_hayoti.HolderActivity
import com.example.adiblar_hayoti.adapters.AdibAdapter
import com.example.adiblar_hayoti.models.Adib
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Adib_ChildFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Adib_ChildFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    lateinit var binding: FragmentAdibChildBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    var list = ArrayList<Adib>()
    private lateinit var adibAdapter: AdibAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentAdibChildBinding.inflate(layoutInflater,container,false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("poets")

        setUi()
        setSave()
        setSearch()



        return binding.root
    }

    private fun setSearch() {
        binding.edittext.addTextChangedListener(object :TextWatcher{
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

    private fun setSave() {
        var a = 100

        binding.liner.setOnClickListener {
            val adibcha = arguments?.getSerializable("key") as Adib
            val position = arguments?.getInt("int")
            if (a==position) {
                reference.child("${adibcha.name}/selected").setValue(true)
                binding.liner.setBackgroundResource(R.drawable.circle_shape)
                binding.collection.setImageResource(R.drawable.saved)
                a++
            } else {
                reference.child("${adibcha.name}/selected").setValue(false)
                binding.liner.setBackgroundResource(R.drawable.circle_white)
                binding.collection.setImageResource(R.drawable.ribbon)

                a=position!!
            }
        }
    }

    private fun setUi() {
        val adibcha = arguments?.getSerializable("key") as Adib
        val position = arguments?.getInt("int")


        if (adibcha.selected == true){
            binding.liner.setBackgroundResource(R.drawable.circle_shape)
            binding.collection.setImageResource(R.drawable.saved)
        }else{
            binding.liner.setBackgroundResource(R.drawable.circle_white)
            binding.collection.setImageResource(R.drawable.ribbon)
        }

        binding.name.text = adibcha.name
        binding.years.text = "(${adibcha.birth_date}-${adibcha.death_date})"
        binding.text.text = adibcha.description
        Glide.with(binding.root.context).load(adibcha.photoUrl).into(binding.image)



        binding.linerSearch.setOnClickListener {
            binding.gang.visibility = View.GONE
            binding.gangster.visibility = View.VISIBLE
        }

        binding.clear.setOnClickListener {
            binding.gang.visibility = View.VISIBLE
            binding.gangster.visibility = View.INVISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as HolderActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        (activity as HolderActivity).showBottomNavigation()
        super.onDetach()

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Adib_ChildFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Adib_ChildFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}