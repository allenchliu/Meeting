package com.jin.calendar;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jin.calendar.controller.AdminController;
import com.jin.calendar.controller.MenuController;
import com.jin.calendar.controller.RoomController;
import com.jin.calendar.controller.RoomScheduleController;
import com.jin.calendar.controller.UserController;
import com.jin.calendar.controller.UserMenuController;
import com.jin.calendar.model.Menu;
import com.jin.calendar.model.Room;
import com.jin.calendar.model.RoomSchedule;
import com.jin.calendar.model.User;
import com.jin.calendar.model.UserMenu;

/**
 * 绯荤粺閰嶇疆绫伙紝缁ф壙JFinalConfig锛岀敤浜庡鏁翠釜椤圭洰杩涜閰嶇疆
 * 
 * @author Jin
 * @datetime 2014骞�1鏈�15鏃� 涓嬪崍9:24:30
 */
public class SysConfig extends JFinalConfig {

    @Override
    public void configConstant(Constants me) {

        // me.setDevMode(true);//閰嶇疆褰撳墠涓哄紑鍙戞ā寮�
        me.setViewType(ViewType.JSP);// 閰嶇疆榛樿瑙嗗浘涓篔SP
        me.setBaseViewPath("/WEB-INF/page");

        loadPropertyFile("jdbcConfig.properties");// 鍔犺浇鏁版嵁搴撹繛鎺ラ厤缃�

    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", RoomScheduleController.class);
        me.add("/user", UserController.class, "/admin");
        me.add("/menu", MenuController.class, "/admin");
        me.add("/userMenu", UserMenuController.class);
        me.add("/room", RoomController.class, "/admin");
        me.add("/roomSchedule", RoomScheduleController.class);
        me.add("/admin", AdminController.class);

    }

    @Override
    public void configPlugin(Plugins me) {
        // 閰嶇疆Druid鏁版嵁搴撹繛鎺ユ睜鎻掍欢
        // DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcURL"),
        // getProperty("jdbcUser"), getProperty("jdbcPassword"));
        DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcURL"), getProperty("jdbcUser"), getProperty("jdbcPassword"));
        me.add(druidPlugin);

        // 閰嶇疆ActiveRecord鎻掍欢
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        me.add(arp);
        // arp.setContainerFactory(new CaseInsensitiveContainerFactory());
        arp.addMapping("menu", Menu.class);
        arp.addMapping("user", User.class);
        arp.addMapping("user_menu", UserMenu.class);
        arp.addMapping("room", Room.class);
        arp.addMapping("room_schedule", RoomSchedule.class);
    }

    @Override
    public void configInterceptor(Interceptors me) {
        // me.add(new LoginInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {
        // TODO Auto-generated method stub

    }

    // public static void main(String[] args) {
    // JFinal.start("WebRoot", 80, "/", 5);
    // }
}
