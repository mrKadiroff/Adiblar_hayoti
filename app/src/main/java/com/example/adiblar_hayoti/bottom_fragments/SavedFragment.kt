package com.example.adiblar_hayoti.bottom_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.adiblar_hayoti.R
import com.example.adiblar_hayoti.adapters.AdibAdapter
import com.example.adiblar_hayoti.databinding.AdibListBinding
import com.example.adiblar_hayoti.databinding.FragmentAdibBinding
import com.example.adiblar_hayoti.databinding.FragmentSavedBinding
import com.example.adiblar_hayoti.models.Adib
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SavedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedFragment : Fragment() {
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
    lateinit var binding: FragmentSavedBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    var list = ArrayList<Adib>()
    private lateinit var adibAdapter: AdibAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedBinding.inflate(layoutInflater,container,false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("poets")

        reference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(Adib::class.java)
                    if (value!!.selected == true){
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
                        var bundle = Bundle()
                        bundle.putSerializable("key",adib)
                        bundle.putInt("int",position)
                        findNavController().navigate(R.id.adib_ChildFragment,bundle)
                    }

                })
                binding.rv.adapter = adibAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SavedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SavedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}