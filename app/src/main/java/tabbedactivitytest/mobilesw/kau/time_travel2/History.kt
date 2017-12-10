package tabbedactivitytest.mobilesw.kau.time_travel2

/**
 * Created by USER on 2017-12-08.
 */
class History() {
    lateinit var userID :String
    lateinit var historyTitle : String
    lateinit var desc: String
    lateinit var location: String
    lateinit var timeStamp: String
    lateinit var image : String
    constructor (userID : String, historyTitle:String, desc: String, location : String, timeStamp : String, image : String) : this() {
        this.userID =userID
        this.historyTitle = historyTitle
        this.desc = desc
        this.location = location
        this.timeStamp = timeStamp
        this.image = image
    }
}