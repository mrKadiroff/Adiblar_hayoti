package com.example.adiblar_hayoti.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment

import com.example.adiblar_hayoti.HolderActivity
import com.example.adiblar_hayoti.R
import com.example.adiblar_hayoti.adapters.AdibAdapter
import com.example.adiblar_hayoti.databinding.AdibListBinding
import com.example.adiblar_hayoti.databinding.FragmentAddBinding
import com.example.adiblar_hayoti.databinding.FragmentSearchBinding
import com.example.adiblar_hayoti.models.Adib
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
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
    lateinit var binding: FragmentSearchBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    var list = ArrayList<Adib>()
    private lateinit var adibAdapter: AdibAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater,container,false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("poets")

        setRv()


        binding.edittext.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                filterr(s.toString())
            }

        })

//        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.tooolbar)
//        (requireActivity() as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)





        return binding.root
    }

    private fun filterr(toString: String) {
        val filteredList: ArrayList<Adib> = ArrayList()
        for (item in list) {
            if (item.name!!.toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(item)
            }
        }
        binding.clear.setOnClickListener {
            binding.edittext.setText("")
        }

       if (toString.isEmpty()){
           binding.clear.visibility = View.GONE
       }else{
           binding.clear.visibility = View.VISIBLE
       }

        adibAdapter.filterList(filteredList)
    }

    private fun setRv() {
        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(Adib::class.java)
                    if (value != null) {
                        list.add(value)
                    }

                }

                adibAdapter = AdibAdapter(list,object:AdibAdapter.OnItemClickListener{
                    var a = 100
                    override fun onItemFavoriteClick(
                        adibListBinding: AdibListBinding,
                        adib: Adib,
                        position: Int
                    ) {
                        if (a==position) {
                            reference.child("${adib.name}/selected").setValue(true)
                            adibListBinding.liner.setBackgroundResource(R.drawable.circle_shape)
                            adibListBinding.collection.setImageResource(R.drawable.saved)
                            a++
                        } else {
                            reference.child("${adib.name}/selected").setValue(false)
                            adibListBinding.liner.setBackgroundResource(R.color.white)
                            adibListBinding.collection.setImageResource(R.drawable.ribbon)

                            a=position
                        }
                    }

                    override fun onItemClick(adib: Adib, position: Int) {

                    }

                })
                binding.rv.adapter = adibAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_search,menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }


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
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}