package com.app.hihlo.ui.profile.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.hihlo.model.get_profile.Posts
import com.app.hihlo.ui.profile.fragment.profile_view_pager.ProfilePostsFragment
import com.app.hihlo.ui.profile.fragment.profile_view_pager.ProfileReelsFragment

/*
class AdapterProfileMediaViewPager(val reels: Posts, val posts: Posts, val getSelectedPost: (String, String, Posts, Int, Int) -> Unit) :
    RecyclerView.Adapter<AdapterProfileMediaViewPager.ViewHolder>() {
    inner class ViewHolder(val binding: AdapterShowMediaViewPagerBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterShowMediaViewPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if (position==0){
                showMediaRecycler.adapter = AdapterProfileMedia(position, posts){ asset_url, caption, selectedItem ->
                    getSelectedPost(asset_url, caption, posts, 0, selectedItem)
                }
            }else if (position==1){
                showMediaRecycler.adapter = AdapterProfileMedia(position, reels){asset_url, caption, selectedItem ->
                    getSelectedPost(asset_url, caption, reels, 1, selectedItem)
                }
            }
        }
    }
}*/
class AdapterProfileMediaViewPager(
    fragment: Fragment,
    private val reels: Posts,
    private val posts: Posts,
    val isMyProfile: String,
    val userId: String
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfilePostsFragment.newInstance(posts, isMyProfile, userId)
            1 -> ProfileReelsFragment.newInstance(reels, isMyProfile, userId)
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}

