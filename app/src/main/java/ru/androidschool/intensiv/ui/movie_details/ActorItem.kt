package ru.androidschool.intensiv.ui.movie_details

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.actor_view.*
import kotlinx.android.synthetic.main.item_with_text.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Actor

class ActorItem(
    private val content: Actor
) : Item() {

    override fun getLayout() = R.layout.actor_view

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.actor_name.text = content.name?.toUpperCase()

        // TODO Получать из модели
        Picasso.get()
            .load("https://www.kinopoisk.ru/images/film_big/1143242.jpg")
            .into(viewHolder.actor_photo)
    }
}
