package tabbedactivitytest.mobilesw.kau.time_travel2

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import java.util.HashMap

/**
 * Created by AHYEON on 2017-12-26.
 */
class GetFriendListRequest(userID: String, listener: Response.Listener<String>) : StringRequest(Request.Method.POST, URL, listener, null) {
    private val parameters: MutableMap<String, String>

    init {
        parameters = HashMap()
        parameters.put("userID", userID)
    }

    public override fun getParams(): Map<String, String> {
        return parameters
    }

    companion object {

        private val URL = "http://fjvbn2003.cafe24.com/GetFriendList.php"
    }

}