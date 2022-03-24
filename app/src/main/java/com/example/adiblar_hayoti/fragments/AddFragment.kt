package com.example.adiblar_hayoti.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.adiblar_hayoti.BuildConfig
import com.example.adiblar_hayoti.HolderActivity
import com.example.adiblar_hayoti.R
import com.example.adiblar_hayoti.databinding.FragmentAddBinding
import com.example.adiblar_hayoti.databinding.FragmentTabHolderBinding
import com.example.adiblar_hayoti.models.Adib
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {
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
    val category = arrayOf("Mumtoz adabiyoti","O'zbek adabiyoti","Jahon adabiyoti")

    lateinit var photoURI: Uri
    lateinit var currentImagePath: String
    lateinit var binding: FragmentAddBinding


    lateinit var list: ArrayList<Adib>
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var reference: StorageReference
    private val TAG = "AddFragment"

    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var referenceRealtime: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(layoutInflater,container,false)

        firebaseStorage = FirebaseStorage.getInstance()
        reference = firebaseStorage.getReference("images")



        firebaseDatabase = FirebaseDatabase.getInstance()
        referenceRealtime = firebaseDatabase.getReference("poets")


        setSpinner()

        binding.addImage.setOnClickListener {
            dialog()
        }

        return binding.root
    }

    private fun dialog() {
        val picturesDialog = AlertDialog.Builder(binding.root.context)
        val dialog = picturesDialog.create()
        picturesDialog.setNegativeButton("Bekor qilish",{ dialogInterFace: DialogInterface, i: Int ->
            dialog.dismiss()
        })
        picturesDialog.setTitle("Kamera yoki Gallerreyani tanlang")
        val DialogItems = arrayOf("Galereyadan rasm tanlash", "Kamera orqali rasmga olish")
        picturesDialog.setItems(DialogItems){
                dialog, which ->
            when(which){
                0 -> permission_gallery()
                1 -> permission_camera()
            }
        }
        picturesDialog.show()
    }

    private fun permission_gallery() {
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            //all permissions already granted or just granted
            //your action
            Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
            pickImageFromGallery()

        }
            .onDeclined { e ->
                if (e.hasDenied()) {
                    android.app.AlertDialog.Builder(binding.root.context)
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            Toast.makeText(binding.root.context, "Rad etildi", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss();
                        }
                        .show();
                }
                if (e.hasForeverDenied()) {

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }

            }}

    private fun pickImageFromGallery() {
        getImageContent.launch("image/*")
    }
    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri ?: return@registerForActivityResult
            binding.profilePic.setImageURI(uri)



            binding.save.setOnClickListener {
                val m = System.currentTimeMillis()
                val uploadTask = reference.child(m.toString()).putFile(uri)


                uploadTask.addOnSuccessListener {
                    if (it.task.isSuccessful) {
                        val downloadUrl = it.metadata?.reference?.downloadUrl
                        downloadUrl?.addOnSuccessListener { imagUri->
                            var imgUrl = imagUri.toString()
                            var name = binding.name.text.toString()
                            var birth = binding.date.text.toString()
                            var deathh = binding.death.text.toString()
                            var type = binding.turi.selectedItem.toString()
                            var descriptiona = binding.description.text.toString()
                            val adib = Adib(imgUrl,name,birth,deathh,type,descriptiona,false)
                            if (name.isNotEmpty()&&birth.isNotEmpty()&&deathh.isNotEmpty()&&type.isNotEmpty()&&descriptiona.isNotEmpty()){
                                referenceRealtime.child(name).setValue(adib)

                            }else{
                                Toast.makeText(binding.root.context, "Ma'lumotlarni to'liq kiritng", Toast.LENGTH_SHORT).show()
                            }


                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, ":${it.message}")
                }
                findNavController().popBackStack()
            }



        }








    private fun permission_camera() {
        askPermission(Manifest.permission.CAMERA) {
            //all permissions already granted or just granted
            //your action
            Toast.makeText(binding.root.context, "Granted", Toast.LENGTH_SHORT).show()
            val imageFile = createImageFile()
            photoURI = FileProvider.getUriForFile(binding.root.context, BuildConfig.APPLICATION_ID,imageFile)
            getTakeImageContent.launch(photoURI)

        }
            .onDeclined { e ->
                if (e.hasDenied()) {
                    android.app.AlertDialog.Builder(binding.root.context)
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            Toast.makeText(binding.root.context, "Rad etildi", Toast.LENGTH_SHORT).show()
                            dialog.dismiss();
                        }
                        .show();
                }
                if(e.hasForeverDenied()) {

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
    }

    private val getTakeImageContent =
        registerForActivityResult(ActivityResultContracts.TakePicture()){

            if (it) {
                binding.profilePic.setImageURI(photoURI)
                val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())

                binding.save.setOnClickListener{
                    val m = System.currentTimeMillis()
                    val uploadTask = reference.child(m.toString()).putFile(photoURI)

                    uploadTask.addOnSuccessListener {
                        if (it.task.isSuccessful) {
                            val downloadUrl = it.metadata?.reference?.downloadUrl
                            downloadUrl?.addOnSuccessListener { imagUri->
                                var imgUrlcha = imagUri.toString()
                                var name = binding.name.text.toString()
                                var birth = binding.date.text.toString()
                                var deathh = binding.death.text.toString()
                                var type = binding.turi.selectedItem.toString()
                                var descriptiona = binding.description.text.toString()
                                val adib = Adib(imgUrlcha,name,birth,deathh,type,descriptiona,false)
                                if (name.isNotEmpty()&&birth.isNotEmpty()&&deathh.isNotEmpty()&&type.isNotEmpty()&&descriptiona.isNotEmpty()){
                                    referenceRealtime.child(name).setValue(adib)

                                }else{
                                    Toast.makeText(binding.root.context, "Ma'lumotlarni to'liq kiritng", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(binding.root.context, it.message, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, ":${it.message}")
                    }
                    findNavController().popBackStack()

                }




            }
        }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val format = SimpleDateFormat("yyyyMM_HHmmss", Locale.getDefault()).format(Date())
        val externalFilesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("JPEG_$format",".jpg",externalFilesDir).apply {
            currentImagePath = absolutePath
        }
    }




    private fun setSpinner() {
        val arrayAdapter = ArrayAdapter(binding.root.context,android.R.layout.simple_spinner_dropdown_item,category)
        binding.turi.adapter = arrayAdapter
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
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}