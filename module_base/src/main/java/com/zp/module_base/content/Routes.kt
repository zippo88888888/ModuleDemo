package com.zp.module_base.content

/**
 * https://www.jianshu.com/p/6021f3f61fa6
 */
object Routes {

    // App 壳 ===========================================================
    private const val APP_ROOT_ROUTE = "/app/"
    /** App首页 */
    const val APP_ROUTE_MAIN = "${APP_ROOT_ROUTE}MainActivity"

    // 用户模块 =========================================================
    private const val USER_ROOT_ROUTE = "/user/"

    /** 用户首页 */
    const val USER_ROUTE_INFO = "${USER_ROOT_ROUTE}UserInfoActivity"
    /** 购物车 */
    const val USER_ROUTE_CAR = "${USER_ROOT_ROUTE}CarActivity"

    // 文件模块 =========================================================
    private const val FILE_ROOT_ROUTE = "/file/"
    /** 文件首页 */
    const val FILE_ROUTE_MAIN = "${FILE_ROOT_ROUTE}FileMainActivity"


}