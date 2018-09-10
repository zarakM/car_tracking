package pk.zarsh.car_tracking

/**
 * Created by HP on 7/11/18.
 */
class location constructor(lat:String,lon:String,id:String){
    var lat: String
    var lon: String
    var id :String
    init {
        this.lat = lat
        this.lon = lon
        this.id = id
    }
}