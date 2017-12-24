package tabbedactivitytest.mobilesw.kau.time_travel2

/**
 * Created by AHYEON on 2017-12-24.
 */
class AccountListViewItem(menu: String, name: String) {
    private val menu: String = menu
    private val name: String = name

    fun getMenu(): String {
        return menu
    }

    fun getName(): String {
        return name
    }

}