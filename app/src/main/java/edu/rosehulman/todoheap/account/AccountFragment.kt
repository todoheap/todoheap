package edu.rosehulman.todoheap.account

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.account.view.AccountViewModel
import edu.rosehulman.todoheap.main.MainActivity
import edu.rosehulman.todoheap.data.Database
import edu.rosehulman.todoheap.databinding.FragmentAccountBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.URL

class AccountFragment : Fragment() {
    lateinit var activity: MainActivity;
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        activity = requireContext() as MainActivity
        val binding = DataBindingUtil.inflate<FragmentAccountBinding>(inflater, R.layout.fragment_account, container, false)
        binding.controller = activity.accountController
        val name = Database.auth.currentUser?.displayName
        val model = AccountViewModel(name?:"", ResourcesCompat.getDrawable(activity.resources, R.drawable.ic_account, null)!!)
        binding.model = model
        Database.auth.currentUser?.photoUrl?.let{
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val imageStream = URL(it.toString()).openStream()
                    val drawable = BitmapDrawable(requireContext().resources, BitmapFactory.decodeStream(imageStream))
                    withContext(Dispatchers.Main){
                        model.profilePicture = drawable
                        binding.model = model
                    }
                }catch(e: Exception){
                    Log.e(Constants.TAG,"Error reading image: $e")
                }

            }
        }



        return binding.root
    }
}