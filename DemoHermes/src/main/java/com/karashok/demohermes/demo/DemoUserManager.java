package com.karashok.demohermes.demo;

import com.karashok.demohermes.ClassId;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-16-2022
 */
@ClassId("com.karashok.demohermes.demo.DemoUserManager")
public class DemoUserManager implements IUserManager{

    private Friend friend;

    private static DemoUserManager instance = new DemoUserManager();

    public static synchronized DemoUserManager getInstance() {
        if (instance == null) {
            instance = new DemoUserManager();
        }
        return instance;
    }

    public DemoUserManager() {
    }


    @Override
    public Friend getFriend() {
        return friend;
    }

    @Override
    public void setFriend(Friend friend) {
        this.friend = friend;
    }

}
