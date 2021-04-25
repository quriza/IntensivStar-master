package ru.androidschool.intensiv.ui

import android.view.View
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

fun ShapeableImageView.load(file: String?) {
    if (file !== null) {
        Picasso.get()
            .load(file)
            .into(this)
    } else {
        this.visibility = View.GONE
    }
}
