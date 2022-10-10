// IDemoAidlInterface.aidl
package com.karashok.demoaidlserver;

// Declare any non-default types here with import statements
import com.karashok.demoaidlserver.IPersonAidlInterface;

interface IDemoAidlInterface {

    void addPerson(in Person person);
    List<Person> getPersonList();
}