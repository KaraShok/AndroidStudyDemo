package com.karashok.demohermes.demo;

import com.karashok.demohermes.ClassId;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-16-2022
 */
@ClassId("com.karashok.demohermes.demo.DemoUserManager")
public interface IUserManager {

    public Friend getFriend();

    public void setFriend(Friend friend);
}
