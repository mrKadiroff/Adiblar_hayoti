package com.example.adiblar_hayoti.models

import java.io.Serializable

class Adib: Serializable {
    var photoUrl: String? = null
    var name: String? = null
    var birth_date: String? = null
    var death_date: String? = null
    var type: String? = null
    var description: String? = null
    var selected: Boolean? = null



    constructor()
    constructor(
        photoUrl: String?,
        name: String?,
        birth_date: String?,
        death_date: String?,
        type: String?,
        description: String?,
        selected: Boolean?
    ) {
        this.photoUrl = photoUrl
        this.name = name
        this.birth_date = birth_date
        this.death_date = death_date
        this.type = type
        this.description = description
        this.selected = selected
    }


}