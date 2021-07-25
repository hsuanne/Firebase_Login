package com.example.application_login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val name: TextView = view.findViewById(R.id.google_name)
        val email: TextView = view.findViewById(R.id.google_email)
        val pic:ImageView = view.findViewById(R.id.profile_pic)
        val logout: Button = view.findViewById(R.id.logout_button)

        val signInAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(activity)
        if (signInAccount != null){
            name.text = signInAccount.displayName
            email.text = signInAccount.email
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user!=null){
            var provider=user.providerData[0].providerId
            var glideUrl=""
            if (user.providerData.size>1) {
                provider = user.providerData[1].providerId
            }

            when(provider){
                "google.com" -> {
                    name.text = String.format(resources.getString(R.string.user_name), user.displayName)
                    email.text = String.format(resources.getString(R.string.user_email), user.email)
                    val newUrl = user.photoUrl.toString()
                    println("Original photoUrl: $newUrl")
                    glideUrl = newUrl.substring(0, newUrl.length - 5) + "s400-c"
                    println("New photoUrl: $glideUrl")
                }
                "facebook.com" -> {
                    name.text = String.format(resources.getString(R.string.user_name), user.displayName)
                    email.text = String.format(resources.getString(R.string.user_email), user.email)
                    val newUrl = user.photoUrl.toString()
                    println("Original photoUrl: $newUrl")
                    glideUrl = "$newUrl?height=500"
                    println("New photoUrl: $glideUrl")
                }
                else -> {
                    name.text = String.format(resources.getString(R.string.user_name), user.uid)
                    email.text = String.format(resources.getString(R.string.user_email), user.email)
                    pic.setImageResource(R.drawable.ic_baseline_person_24)
                }
            }

            if (user.photoUrl!=null){
                Glide.with(requireContext()).load(glideUrl).into(pic)
            } else {
                pic.setImageResource(R.drawable.ic_baseline_person_24)
            }
        }

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, MainActivity::class.java))
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}