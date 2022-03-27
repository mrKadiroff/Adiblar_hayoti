package com.example.adiblar_hayoti.bottom_fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.adiblar_hayoti.MainActivity
import com.example.adiblar_hayoti.R
import com.example.adiblar_hayoti.adapters.AdibAdapter
import com.example.adiblar_hayoti.adapters.ViewPagerAdapter
import com.example.adiblar_hayoti.databinding.AdibListBinding
import com.example.adiblar_hayoti.databinding.FragmentAdibBinding
import com.example.adiblar_hayoti.databinding.FragmentSavedBinding
import com.example.adiblar_hayoti.databinding.ItemTabBinding
import com.example.adiblar_hayoti.models.Adib
import com.example.adiblar_hayoti.tab_fragments.TabHolderFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdibFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdibFragment : Fragment() {
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

    val tabArray = arrayOf(
        "Mumtoz adabiyoti",
        "O'zbek adabiyoti",
        "Jahon adabiyoti"
    )

    lateinit var binding: FragmentAdibBinding


    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    var list = ArrayList<Adib>()
    private lateinit var adibAdapter: AdibAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdibBinding.inflate(layoutInflater,container,false)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("poets")

       binding.tooolbar.inflateMenu(R.menu.menu_item)
        binding.tooolbar.setOnMenuItemClickListener {
            if (it.itemId==R.id.search_action){
                var bundle = Bundle()
                bundle.putString("adib","adib")
                findNavController().navigate(R.id.searchFragment,bundle)
            }
            true
        }



        setViewPager()

//        setRv()

        return binding.root
    }

//    private fun setRv() {
//        reference.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                list.clear()
//                val children = snapshot.children
//                for (child in children) {
//                    val value = child.getValue(Adib::class.java)
//                    if (value != null) {
//                        list.add(value)
//                    }
//
//                }
//
//                adibAdapter = AdibAdapter(list,object:AdibAdapter.OnItemClickListener{
//                    override fun onItemFavoriteClick(
//                        adibListBinding: AdibListBinding,
//                        adib: Adib,
//                        position: Int
//                    ) {
//
//                    }
//
//                    override fun onItemClick(adib: Adib, position: Int) {
//                        findNavController().navigate(R.id.adib_ChildFragment)
//                    }
//
//                })
//                binding.rv.adapter = adibAdapter
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })
//    }

    private fun setViewPager() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = ViewPagerAdapter(tabArray,childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val itemTabBinding: ItemTabBinding = ItemTabBinding.inflate(layoutInflater)
            tab.customView = itemTabBinding.root
            itemTabBinding.titleTv.text = tabArray[position]

            if (position == 0) {
                itemTabBinding.titleTv.setBackgroundResource(R.drawable.tab_item_back_selected)
                itemTabBinding.titleTv.setTextColor(Color.WHITE)

            } else {
                itemTabBinding.titleTv.setTextColor(Color.parseColor("#303236"))
                itemTabBinding.titleTv.setBackgroundResource(R.drawable.tab_item_back_unselected)
            }

        }.attach()


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val itemTabBinding = ItemTabBinding.bind(tab?.customView!!)
                itemTabBinding.titleTv.setBackgroundResource(R.drawable.tab_item_back_selected)

                itemTabBinding.titleTv.setTextColor(Color.WHITE)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val itemTabBinding = ItemTabBinding.bind(tab?.customView!!)
                itemTabBinding.titleTv.setTextColor(Color.parseColor("#303236"))
                itemTabBinding.titleTv.setBackgroundResource(R.drawable.tab_item_back_unselected)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })



    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdibFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdibFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}