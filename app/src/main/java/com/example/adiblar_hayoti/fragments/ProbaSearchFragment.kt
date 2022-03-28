package com.example.adiblar_hayoti.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.adiblar_hayoti.R
import com.example.adiblar_hayoti.adapters.AdibAdapter
import com.example.adiblar_hayoti.adapters.FavoriteAdapter
import com.example.adiblar_hayoti.databinding.AdibListBinding
import com.example.adiblar_hayoti.databinding.FragmentAdibBinding
import com.example.adiblar_hayoti.databinding.FragmentProbaSearchBinding
import com.example.adiblar_hayoti.models.Adib
import com.example.adiblar_hayoti.room.Adib_Entity
import com.example.adiblar_hayoti.room.AppDatabase
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProbaSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProbaSearchFragment : Fragment() {
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
    lateinit var binding: FragmentProbaSearchBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    var list = ArrayList<Adib>()
    lateinit var tepmArraylist:ArrayList<Adib>
    private lateinit var adibAdapter: AdibAdapter

    lateinit var appDatabase: AppDatabase
    lateinit var favoritelist:ArrayList<Adib_Entity>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProbaSearchBinding.inflate(layoutInflater,container,false)
        appDatabase = AppDatabase.getInstance(binding.root.context)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("poets")
//        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.)
//        (requireActivity() as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)



        favoritelist = appDatabase.adibDao().getAllAdib() as ArrayList<Adib_Entity>

        binding.searchAdib.setQuery("",true);
        binding.searchAdib.setFocusable(true);
        binding.searchAdib.setIconified(false);
        binding.searchAdib.requestFocusFromTouch();
        setRv()

        binding.searchAdib.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var list2 = ArrayList<Adib>()
                for (adib in list) {
                    for (i in 0 until adib.name?.length!!){
                        if (adib.name?.subSequence(0,i).toString()
                                .lowercase(Locale.getDefault()) == newText?.lowercase()){
                            list2.add(adib)
                        }
                    }
                }
                adibAdapter = AdibAdapter(list2,favoritelist,object : AdibAdapter.OnItemClickListener{
                    var a = 100
                    override fun onItemFavoriteClick(
                        adibListBinding: AdibListBinding,
                        adib: Adib,
                        position: Int
                    ) {
                
                    }

                    override fun onItemClick(adib: Adib, position: Int) {
                        var bundle = Bundle()
                        bundle.putSerializable("key",adib)
                        bundle.putInt("int",position)
                        findNavController().navigate(R.id.adib_ChildFragment,bundle)
                    }

                })


                binding.rvSearch.adapter = adibAdapter
                return true
            }

        })


        return binding.root
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



                adibAdapter = AdibAdapter(list,favoritelist,object:AdibAdapter.OnItemClickListener{
                    var a = 100
                    override fun onItemFavoriteClick(
                        adibListBinding: AdibListBinding,
                        adib: Adib,
                        position: Int
                    ) {
                        if (a==position) {
                            adibListBinding.liner.setBackgroundResource(R.drawable.circle_shape)
                            adibListBinding.collection.setImageResource(R.drawable.saved)
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
                            adibListBinding.liner.setBackgroundResource(R.color.white)
                            adibListBinding.collection.setImageResource(R.drawable.ribbon)
                            appDatabase.adibDao().deleteByName(adib.name!!)
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
                binding.rvSearch.adapter = adibAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.menu_search,menu)
//        val item = menu?.findItem(R.id.search_action_best)
//        val searchView = item?.actionView as SearchView
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//
//
//                tepmArraylist.clear()
//                val searchText = newText!!.toLowerCase(Locale.getDefault())
//                if (searchText.isNotEmpty()){
//                    list.forEach{
//                        if ("${it.name}"?.toLowerCase(Locale.getDefault())!!.contains(searchText)){
//                            tepmArraylist.add(it)
//                        }
//                    }
//                    binding.pasportRv.adapter!!.notifyDataSetChanged()
//                }else{
//                    tepmArraylist.clear()
//                    tepmArraylist.addAll(list)
//                    binding.pasportRv.adapter!!.notifyDataSetChanged()
//                }
//                return false
//            }
//
//        })
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProbaSearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProbaSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}