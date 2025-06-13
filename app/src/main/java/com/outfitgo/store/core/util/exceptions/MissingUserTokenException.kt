package com.outfitgo.store.core.util.exceptions

class MissingUserTokenException() : Exception(){
    override val message: String?
        get() = "User token is missing"
}