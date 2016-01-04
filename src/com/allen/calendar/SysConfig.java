package com.allen.calendar;

import com.allen.calendar.controller.RoomScheduleController;
import com.allen.calendar.model.RoomSchedule;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;

public class SysConfig extends JFinalConfig {

    @Override
    public void configConstant(Constants me) {

        // me.setDevMode(true);
        me.setViewType(ViewType.JSP);
        me.setBaseViewPath("/WEB-INF/page");

        loadPropertyFile("jdbcConfig.properties");

    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", RoomScheduleController.class);
    }

    @Override
    public void configPlugin(Plugins me) {
        // DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcURL"),
        // getProperty("jdbcUser"), getProperty("jdbcPassword"));
        DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcURL"), getProperty("jdbcUser"), getProperty("jdbcPassword"));
        me.add(druidPlugin);

        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        me.add(arp);
        // arp.setContainerFactory(new CaseInsensitiveContainerFactory());
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
