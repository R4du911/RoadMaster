package com.example.roadmaster.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    var id: Int = -1,
    var category: String = String(),
    var text: String = String(),
    var answers: MutableList<Pair<String, Boolean>> = mutableListOf(),
    var chosenAnswers: MutableList<Boolean> = mutableListOf()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        mutableListOf<Pair<String, Boolean>>().apply {
            parcel.readList(this, Pair::class.java.classLoader)
        },
        mutableListOf<Boolean>().apply {
            parcel.readList(this, Boolean::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(category)
        parcel.writeString(text)
        parcel.writeList(answers as List<*>)
        parcel.writeList(chosenAnswers as List<*>)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}
