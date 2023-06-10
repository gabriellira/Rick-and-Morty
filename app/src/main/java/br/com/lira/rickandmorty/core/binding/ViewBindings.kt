package br.com.lira.rickandmorty.core.binding

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import br.com.lira.rickandmorty.R
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso

@BindingAdapter("present")
fun View.setPresent(value: Boolean?) {
    visibility = if (value == true) View.VISIBLE else View.GONE
}

@BindingAdapter("img")
fun ImageView.loadImage(url: String?) {
    Picasso.get()
        .load(url)
        .error(R.drawable.ic_round_account_circle)
        .into(this)
}

@BindingAdapter("bgColor")
fun Chip.setBgColor(value: Int?) {
    value?.let {
        setChipBackgroundColorResource(it)
    }
}

@BindingAdapter("textRes")
fun TextView.setTextRes(value: Int?) {
    value?.let {
        runCatching {
            text = context.getText(it)
        }
    }
}

@BindingAdapter("srcRes")
fun ImageView.setSrcRes(value: Int?) {
    value?.let {
        runCatching {
            setImageResource(it)
        }
    }
}
