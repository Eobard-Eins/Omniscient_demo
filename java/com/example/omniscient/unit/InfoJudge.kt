package com.example.omniscient.unit

/**
 * error->true
 * no error->false
 */
fun PasswordJudge(it:String):Boolean{
    if(it.isEmpty()) return true
    for (s:Char in it){
        if(!(s.isDigit()||s.isLetter()||s=='_'||s=='.')) return true
    }
    return it.length !in 6..15
}
fun AccountJudge(it:String):Boolean{
    if(it.isEmpty()) return true
    for (s:Char in it){
        if(!s.isDigit()) return true
    }
    return it.length !in 8..11
}
fun UsernameJudge(it:String):Boolean{
    if(it.isEmpty()) return true
    return it.length !in 2..6
}

fun WordNameJudge(it:String):Boolean{
    if(it.isEmpty()) return true
    return it.length !in 1..8
}

fun FavoriteJudge(it:String):Boolean{
    if(it.isEmpty()) return true
    return it.length !in 2..8
}