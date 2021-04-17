package ru.androidschool.intensiv.ui.movie_details

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.actor_view.*
import kotlinx.android.synthetic.main.item_with_text.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Actor
import ru.androidschool.intensiv.ui.load

class ActorItem(
    private val content: Actor
) : Item() {

    override fun getLayout() = R.layout.actor_view

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.actor_name.text = content.name?.toUpperCase()

        viewHolder.actor_photo.load(content.profilePath)
    }
}
