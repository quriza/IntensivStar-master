package ru.androidschool.intensiv.ui.profile

import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_profile.*
import ru.androidschool.intensiv.R

data class TabData(val count: Int, val position: Int)

class SharedViewModel : ViewModel() {
    val tabData = MutableLiveData<TabData>()

    fun updateTabData(item: TabData) {
        tabData.value = item
    }
}

class ProfileFragment : Fragment() {

    private lateinit var profileTabLayoutTitles: Array<String>
    private val model: SharedViewModel by activityViewModels()

    private var profilePageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {

            Toast.makeText(
                requireContext(),
                "Selected position: $position",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.tabData.observe(viewLifecycleOwner, Observer<TabData> { item ->
            // просто было интересно поиграться с передачей viewModel между фрагментами
            // хотя с точки зрения логики, все-таки надо делать запрос к БД для показа циферек
            setTabText(
                tabLayout.getTabAt(item.position)!!,
                profileTabLayoutTitles[item.position],
                item.count
            )
        })

        Picasso.get()
            .load(R.drawable.ic_avatar)
            .transform(CropCircleTransformation())
            .placeholder(R.drawable.ic_avatar)
            .into(avatar)

        profileTabLayoutTitles = resources.getStringArray(R.array.tab_titles)

        val profileAdapter = ProfileAdapter(
            this,
            profileTabLayoutTitles.size
        )
        doppelgangerViewPager.adapter = profileAdapter

        doppelgangerViewPager.registerOnPageChangeCallback(profilePageChangeCallback)

        TabLayoutMediator(tabLayout, doppelgangerViewPager) { tab, position ->
            setTabText(tab, profileTabLayoutTitles[position], null)
        }.attach()
    }

    fun setTabText(tab: TabLayout.Tab, title: String, newCount: Int?) {
        val number = newCount?.toString() ?: "???"
        val spannableStringTitle = SpannableString(number + "\n " + title)
        spannableStringTitle.setSpan(RelativeSizeSpan(2f), 0, number.count(), 0)

        tab.text = spannableStringTitle
    }
}
