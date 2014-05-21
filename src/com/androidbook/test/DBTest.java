package com.androidbook.test;

import com.androidbook.requesthandle.Login;
import com.androidbook.requesthandle.Signin;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by summer on 5/20/14.
 *
 * begin use TDD
 *
 * enjoy it !!
 */
public class DBTest {
    public void mTestSignin(){

        String[] params ={"124","123","123","123","123","123","123"};

        Signin signin = new Signin();

        int a = signin.handleRequest(123,"123",params);

        System.out.println("========mTestSigin======"+a);
        Assert.assertEquals(0,a);
    }


    @Test
    public void mTestLogin(){

        String [] params = {"张宁","123"};

        Login login = new Login();
        int i = login.handleRequest(123,"123",null);
        Assert.assertEquals(0,i);

    }

}
